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

@RestController
@RequestMapping("/chat")
public class ChatbotController {

    private final ChatbotService chatbotService;

    @Autowired
    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping("/chatMessage")
    public String handleChatMessage(@RequestBody String userMessage, HttpSession session) {
        // 세션에서 client_id 추출
        String clientId = (String) session.getAttribute("client_id");

        // client_id가 없으면 대화 저장하지 않음
        if (clientId == null) {
            return "대화 저장을 원하시면 로그인이 필요합니다.";
        }

        // 사용자 메시지와 챗봇 응답을 DB에 저장
        chatbotService.saveMessage(clientId, "user", userMessage);  // 사용자 메시지 저장
        String botReply = "챗봇의 응답: " + userMessage;  // 예시 챗봇 응답
        chatbotService.saveMessage(clientId, "bot", botReply); // 챗봇 응답 저장

        return botReply;
    }
}
