package com.example.demo.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wb")
public class WhiteboardController {

	@MessageMapping("")
	@SendTo("/topic/whiteboard")
	public String handleDraw(String message) {
		return message; // 받은 메시지를 그대로 브로드캐스트
	}
}
