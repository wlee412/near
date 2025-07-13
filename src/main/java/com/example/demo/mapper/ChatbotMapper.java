package com.example.demo.mapper;


import org.apache.ibatis.annotations.Mapper;
import com.example.demo.model.Chatbot;

@Mapper
public interface ChatbotMapper {

	void insertMessage(Chatbot chatbotMessage);
}