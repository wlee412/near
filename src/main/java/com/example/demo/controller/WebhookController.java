package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import com.example.demo.model.Chatbot;
import com.example.demo.model.Client;
import com.example.demo.service.ChatbotService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final ChatbotService chatbotService;
    private final WebClient webClient;

    public WebhookController(@Value("${openai.api.key}") String apiKey, ChatbotService chatbotService) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        this.chatbotService = chatbotService;
    }
    
    @GetMapping("/chat")
    public String showChatPage(HttpSession session, Model model) {
        Client loginClient = (Client) session.getAttribute("loginClient");
        if (loginClient != null) {
            String clientId = loginClient.getClientId();
            List<Chatbot> chatList = chatbotService.getChatHistory(clientId);
            model.addAttribute("chatList", chatList);
        }
        return "chatbot/chat"; // JSP 파일명
    }

    @PostMapping
    public Map<String, Object> handleWebhook(@RequestBody Map<String, Object> request, HttpSession session) {
        System.out.println("📥 request: " + request);

        try {
            // ✅ JS에서 직접 보낸 요청 처리 (예: { "message": "..." })
            if (request.containsKey("message")) {
                String userMessage = (String) request.get("message");
                if (userMessage == null || userMessage.trim().isEmpty()) {
                    userMessage = "사용자 메시지가 없습니다.";
                }

                // GPT 응답 생성
                String reply = callChatGPT(userMessage);

                // 로그인 사용자면 DB 저장
                Client loginClient = (Client) session.getAttribute("loginClient");
                if (loginClient != null) {
                    String clientId = loginClient.getClientId();
                    chatbotService.saveMessage(clientId, "user", userMessage);
                    chatbotService.saveMessage(clientId, "bot", reply);
                }

                // JS fetch용 응답
                return Map.of("reply", reply);
            }

            // ✅ Dialogflow webhook 요청 처리 (queryResult가 있는 경우)
            Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
            if (queryResult == null) {
                System.out.println("❌ queryResult가 null입니다.");
                return Map.of("fulfillmentText", "잘못된 요청입니다. 다시 시도해주세요.");
            }

            String userMessage = (String) queryResult.get("queryText");
            if (userMessage == null || userMessage.trim().isEmpty()) {
                userMessage = "사용자 메시지가 없습니다.";
            }

            String reply = handleIntentOrFallbackToGPT(request, userMessage);

            Client loginClient = (Client) session.getAttribute("loginClient");
            if (loginClient != null) {
                String clientId = loginClient.getClientId();
                chatbotService.saveMessage(clientId, "user", userMessage);
                chatbotService.saveMessage(clientId, "bot", reply);
            }

            //  Dialogflow 응답용
            Map<String, Object> fulfillmentText = Map.of("text", List.of(reply));
            Map<String, Object> message = Map.of("text", fulfillmentText);
            return Map.of("fulfillmentMessages", List.of(message));

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("reply", "응답을 받지 못했습니다.");
        }
    }


    private String handleIntentOrFallbackToGPT(String intentName, String userMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	// Intent 분석 → GPT 대체
    private String handleIntentOrFallbackToGPT(Map<String, Object> request, String userMessage) {
        Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
        Map<String, Object> intentMap = (Map<String, Object>) queryResult.get("intent");
        String intentName = (String) intentMap.get("displayName");

        // Intent 별 분기
        switch (intentName) {
            case "상담예약":
                return callChatGPT("사용자가 상담 예약을 원해요. 아래 메시지로 응답해주세요:\n" + userMessage);
            case "우울":
                return callChatGPT("상담예약을 유도해줘 " + userMessage);
            case "기분전환":
                return "기분 전환을 원하시면 '심리 게임', '설문 조사', 또는 '짧은 호흡 운동'을 추천드릴게요.";
            default:
                return callChatGPT(userMessage); // fallback
        }
    }
    
    // 🔁 ChatGPT API 호출
    private String callChatGPT(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                    Map.of("role", "system", "content", "당신은 'n:ear'의 심리상담 챗봇입니다. 사용자가 상담에 대한 정보를 요청하면, 필요한 링크를 제공하거나 '상단 메뉴에서 확인하세요'라고 간단히 안내해주세요. 사용자가 기분 전환을 원할 때는 '기분 전환' 또는 '게임' 등의 제안을 해주세요. 불필요한 반복을 피하고, 적절하게 대화 흐름을 유지하세요."),
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
