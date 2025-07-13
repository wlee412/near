package com.example.demo.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.ChatbotMapper;
import com.example.demo.model.Chatbot;

@Service
public class ChatbotService {

    private final ChatbotMapper chatbotMapper;

    @Autowired
    public ChatbotService(ChatbotMapper chatbotMapper) {
        this.chatbotMapper = chatbotMapper;
    }

    // 메시지를 DB에 저장
    @Transactional
    public void saveMessage(String clientId, String sender, String message) {
        // Chatbot 객체 생성
        Chatbot chatbotMessage = new Chatbot();
        
        chatbotMessage.setClientId(clientId);
        chatbotMessage.setSender(sender);
        chatbotMessage.setMessage(message);
        chatbotMessage.setRegDate(new Timestamp(System.currentTimeMillis()));  // 현재 시간 설정

        // DB에 메시지 저장
        chatbotMapper.insertMessage(chatbotMessage);
    }
    
}
