package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.mapper.ChatbotMapper;
import com.example.demo.model.Chatbot;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatbotService {

    private final ChatbotMapper chatbotMapper;

    public void saveChat(Chatbot chat) {
        chatbotMapper.insertChat(chat);
    }

    public List<Chatbot> getChats(String clientId) {
        return chatbotMapper.getChatsByClientId(clientId);
    }
}
