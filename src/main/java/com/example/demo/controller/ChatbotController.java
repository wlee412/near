package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ChatbotService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/chatbot")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    // Webhook 처리
    @PostMapping("/webhook")
    public ResponseEntity<?> webhook(@RequestBody Map<String, Object> request, HttpSession session) {
        // Dialogflow에서 전달한 데이터 추출
        String clientId = (String) request.get("clientId");  // 클라이언트 ID
        
        // client_id가 없을 경우 임시 client_id 생성
        if (clientId == null) {
            clientId = "guest_" + System.currentTimeMillis();  // 예시: guest_123456789
        }

        String sender = (String) request.get("sender"); // 발신자 (user, bot)
        String message = (String) request.get("message"); // 사용자 메시지

        // DB에 메시지 저장
        chatbotService.saveMessage(clientId, sender, message);

        // Dialogflow에 응답
        Map<String, Object> response = new HashMap<>();
        response.put("fulfillmentText", "Message saved to DB.");

        return ResponseEntity.ok(response);
    }
}

