//package com.example.demo.controller;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping; 
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.service.ChatbotService;
//
//import jakarta.servlet.http.HttpSession;
//
//@RestController
//@RequestMapping("/chat")
//public class ChatbotController {
//
//    @Autowired
//    private ChatbotService chatbotService;
//
//    @PostMapping("/save")
//    public ResponseEntity<?> saveChat(@RequestBody Map<String, String> payload, HttpSession session) {
//        String clientId = (String) session.getAttribute("loginClientId"); // 또는 payload에서 받아도 OK
//        String sender = payload.get("sender"); // "user" 또는 "bot"
//        String message = payload.get("message");
//
//        System.out.println("메세지:"+ message);
//        
//        if (clientId == null || sender == null || message == null) {
//            return ResponseEntity.badRequest().body("Missing data");
//            
//            
//        }
//
//        chatbotService.saveMessage(clientId, sender, message);
//        return ResponseEntity.ok().build();
//    }
//    
//}
//
