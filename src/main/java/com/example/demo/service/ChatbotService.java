package com.example.demo.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.ChatbotMapper;
import com.example.demo.model.Chatbot;

@Service
public class ChatbotService {
    @Autowired
    private ChatbotMapper chatbotMapper;

    public void saveMessage(String clientId, String sender, String message) {
        Chatbot chat = new Chatbot();
        chat.setClientId(clientId);
        chat.setSender(sender);
        chat.setMessage(message);
        chatbotMapper.insertChat(chat);
        
        System.out.println("clientId = " + clientId);
    }

    public List<Chatbot> getChatHistory(String clientId) {
        return chatbotMapper.findChatsByClientId(clientId);
    }

    //예약시간 불러오기 
	public List<String> getReservationTimes(String clientId) {
		return chatbotMapper.findReservationTimesByClientId(clientId);
	}
}

    
