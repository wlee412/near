package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.demo.service.ChatbotService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
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
        this.webClient = WebClient.builder().baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apiKey).build();
        this.chatbotService = chatbotService;
    }

    @PostMapping
    public Map<String, Object> handleWebhook(@RequestBody Map<String, Object> request, HttpSession session) {
        try {
            // 세션에서 client_id 추출
            String clientId = (String) session.getAttribute("client_id");

            // client_id가 없으면 임시 client_id 생성
            if (clientId == null) {
                clientId = UUID.randomUUID().toString();
                session.setAttribute("client_id", clientId); // 임시 client_id 세션에 저장
            }

            // Dialogflow 요청에서 사용자 메시지 추출
            Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
            String userMessage = (String) queryResult.get("queryText");

            // userMessage가 없으면 기본 메시지로 처리
            if (userMessage == null || userMessage.trim().isEmpty()) {
                userMessage = "사용자 메시지가 없습니다.";  // 기본 메시지
            }

            // Intent에 대한 응답이 있을 경우 Dialogflow에서 처리, 없을 경우 ChatGPT로 응답 받기
            String reply = handleIntentOrFallbackToGPT(request, userMessage);

            // 사용자 메시지와 챗봇 응답을 DB에 저장
            chatbotService.saveMessage(clientId, "user", userMessage);  // 사용자 메시지 저장
            chatbotService.saveMessage(clientId, "bot", reply);  // 챗봇 응답 저장

            // Dialogflow에 반환할 응답 생성
            Map<String, Object> fulfillmentText = Map.of("text", List.of(reply));
            Map<String, Object> message = Map.of("text", fulfillmentText);
            Map<String, Object> response = Map.of("fulfillmentMessages", List.of(message));

            return response;

        } catch (Exception e) {
            e.printStackTrace(); // 예외 발생 시 로그 출력
            // 예외 처리 시, ChatGPT의 오류 메시지를 반환
            String errorReply = callChatGPT("이해하지 못했습니다. 다시 말씀해주세요.");
            Map<String, Object> errorResponse = Map.of("fulfillmentText", errorReply);
            return errorResponse; // 오류 발생 시 응답 반환
        }
    }

    // Intent가 있으면 처리하고, 없으면 ChatGPT로 응답을 처리
    private String handleIntentOrFallbackToGPT(Map<String, Object> request, String userMessage) {
        // Intent가 존재하는지 확인
        Map<String, Object> queryResult = (Map<String, Object>) request.get("queryResult");
        if (queryResult != null && queryResult.containsKey("intent")) {
            String intent = queryResult.get("intent").toString();
            if ("some_specific_intent".equals(intent)) {
                // 특정 intent에 대한 처리 로직 추가
                return "이건 특정 Intent의 응답입니다.";
            }
        }

        // Intent가 없거나, 처리할 수 없는 경우 ChatGPT로 응답
        return callChatGPT(userMessage);
    }

    // ChatGPT API를 호출하여 응답을 받는 메서드
    private String callChatGPT(String prompt) {
        try {
            Map<String, Object> requestBody = Map.of("model", "gpt-3.5-turbo", "messages", List.of(
                    Map.of("role", "system", "content", "당신은 'n:ear'의 심리상담 챗봇입니다. 사용자가 상담에 대한 정보를 요청하면, 필요한 링크를 제공하거나 '상단 메뉴에서 확인하세요'라고 간단히 안내해주세요. 사용자가 기분 전환을 원할 때는 '기분 전환' 또는 '게임' 등의 제안을 해주세요. 불필요한 반복을 피하고, 적절하게 대화 흐름을 유지하세요."),
                    Map.of("role", "user", "content", prompt)
            ));

            Map response = webClient.post().uri("/chat/completions").contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody).retrieve().bodyToMono(Map.class).block();

            if (response != null && response.containsKey("choices") && !((List) response.get("choices")).isEmpty()) {
                Map message = (Map) ((Map) ((List) response.get("choices")).get(0)).get("message");
                return message.get("content").toString().trim();
            } else {
                return "상담이 필요하면 언제든지 상담을 요청해주세요. 함께 이야기를 나누며 해결책을 찾아드리겠습니다.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "더 나은 상담을 위해 상담관련 키워드를 입력해주세요.";
        }
    }

}
