package com.example.demo.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.model.Chatbot;

@Mapper
public interface ChatbotMapper {
	
	  void insertChat(Chatbot chat);

		List<Chatbot> findChatsByClientId(String clientId);

		List<String> findReservationTimesByClientId(String clientId);
}