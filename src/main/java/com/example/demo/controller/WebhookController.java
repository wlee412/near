package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.demo.model.Chatbot;
import com.example.demo.model.Client;
import com.example.demo.service.ChatbotService;
//import com.example.demo.service.IntentLoader;
import com.example.demo.service.IntentLoader;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final ChatbotService chatbotService;
    private final WebClient webClient;
    private final IntentLoader intentLoader;

    public WebhookController(@Value("${openai.api.key}") String apiKey,
                              ChatbotService chatbotService,
                              IntentLoader intentLoader) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        this.chatbotService = chatbotService;
        this.intentLoader = intentLoader;
    }

    @GetMapping("/chatbot")
    public String showChatPage(HttpSession session, Model model) {
        Client loginClient = (Client) session.getAttribute("loginClient");
        if (loginClient != null) {
            String clientId = loginClient.getClientId();
            List<Chatbot> chatList = chatbotService.getChatHistory(clientId);
            model.addAttribute("chatList", chatList);
        }
        return "chatbot/chatbot";
    }

    @PostMapping
    public Map<String, Object> handleWebhook(@RequestBody Map<String, Object> request, HttpSession session) {
        System.out.println("📥 request: " + request);

        try {
            // ✅ JS에서 직접 보낸 요청 처리
//            if (request.containsKey("message")) {
//                String userMessage = (String) request.get("message");
//                if (userMessage == null || userMessage.trim().isEmpty()) {
//                    userMessage = "사용자 메시지가 없습니다.";
//                }
//
//                String reply = callChatGPT(userMessage);
//
//                Client loginClient = (Client) session.getAttribute("loginClient");
//                if (loginClient != null) {
//                    String clientId = loginClient.getClientId();
//                    chatbotService.saveMessage(clientId, "user", userMessage);
//                    chatbotService.saveMessage(clientId, "bot", reply);
//                }
//
//                return Map.of("reply", reply);
//            }

            // ✅ Dialogflow webhook 요청 처리
            Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
            if (queryResult == null) {
                return Map.of("fulfillmentText", "잘못된 요청입니다. 다시 시도해주세요.");
            }

            String userMessage = (String) queryResult.get("queryText");
            if (userMessage == null || userMessage.trim().isEmpty()) {
                userMessage = "사용자 메시지가 없습니다.";
            }

            Map<String, Object> intentMap = (Map<String, Object>) queryResult.get("intent");
            String intentName = (String) intentMap.get("displayName");
            

         // ✅ 예약시간 조회 intent 처리
         if ("ask-reservation-time".equals(intentName)) {
             Client loginClient = (Client) session.getAttribute("loginClient");
             if (loginClient == null) {
                 return Map.of("fulfillmentText", "먼저 로그인 해주세요.");
             }

             String clientId = loginClient.getClientId();
             List<String> times = chatbotService.getReservationTimes(clientId);

             System.out.println("📥 Dialogflow request: " + request);
             
             if (times == null || times.isEmpty()) {
                 return Map.of("fulfillmentText", "예약 정보가 없습니다.");
             }

             String message = "예약하신 시간은 " + String.join(", ", times) + " 입니다.";
             chatbotService.saveMessage(clientId, "user", userMessage);
             chatbotService.saveMessage(clientId, "bot", message);

             return Map.of("fulfillmentText", message);
         }
         
         
            Map<String, Object> intentResponse = intentLoader.getResponse(intentName);

            if (intentResponse == null || intentResponse.isEmpty()) {
                // ➤ intentLoader에 응답이 없을 경우 GPT로 바로 처리
                String gptReply = callChatGPT(userMessage);

                // DB 저장
                Client loginClient = (Client) session.getAttribute("loginClient");
                if (loginClient != null) {
                    String clientId = loginClient.getClientId();
                    chatbotService.saveMessage(clientId, "user", userMessage);
                    chatbotService.saveMessage(clientId, "bot", gptReply);
                }

                return Map.of("fulfillmentMessages", List.of(
                    Map.of("text", Map.of("text", List.of(gptReply)))
                ));
            }


            String type = (String) intentResponse.get("type");
            Object content = intentResponse.get("content");

            // ✅ DB 저장
            Client loginClient = (Client) session.getAttribute("loginClient");
            if (loginClient != null) {
                String clientId = loginClient.getClientId();
                chatbotService.saveMessage(clientId, "user", userMessage);
                chatbotService.saveMessage(clientId, "bot", content.toString());
            }

            // ✅ 응답 생성
            if ("richContent".equals(type)) {
                return Map.of(
                    "fulfillmentMessages", List.of(
                        Map.of("payload", Map.of("richContent", content))
                    )
                );
            } else {
                // 👉 일반 텍스트 + GPT 이어붙이기
                String gptReply = callChatGPT(userMessage);
                String combined = content.toString() + "\n\n" + gptReply;

                return Map.of(
                    "fulfillmentMessages", List.of(
                        Map.of("text", Map.of("text", List.of(combined)))
                    )
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("fulfillmentText", "응답 처리 중 오류가 발생했습니다.");
        }
    }


    private String callChatGPT(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                    Map.of("role", "system", "content",
                        "당신은 'n:ear'의 심리상담 챗봇입니다. 사용자가 상담에 대한 정보를 요청하면, 필요한 링크를 제공하거나 '상단 메뉴에서 확인하세요'라고 간단히 안내해주세요. 사용자가 기분 전환을 원할 때는 '심리검사'또는 '게임' 등의 제안을 해주세요. 불필요한 반복을 피하고, 적절하게 대화 흐름을 유지하세요."),
                    Map.of("role", "user", "content", prompt)
                )
            );

            Map response = webClient.post()
                .uri("/chat/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

            if (response != null && response.containsKey("choices") && !((List) response.get("choices")).isEmpty()) {
                Map message = (Map) ((Map) ((List) response.get("choices")).get(0)).get("message");
                return message.get("content").toString().trim();
            } else {
                return "상담이 필요하면 언제든지 상담을 요청해주세요.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "GPT 응답에 문제가 발생했습니다. 상담 관련 키워드를 다시 입력해주세요.";
        }
    }
}