package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;
import com.example.demo.model.Chatbot;
import com.example.demo.service.ChatbotService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/save")
    public void saveMessage(@RequestBody Chatbot chat) {
        chatbotService.saveChat(chat);
    }

    @GetMapping("/history/{clientId}")
    public List<Chatbot> getChatHistory(@PathVariable String clientId) {
        return chatbotService.getChats(clientId);
    }
}
