package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.SmsMapper;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Service
public class SmsService {

    private DefaultMessageService messageService;
    

    @Value("${coolsms.api-key}")
    private String apiKey;

    @Value("${coolsms.api-secret}")
    private String apiSecret;

    @Value("${coolsms.from}")
    private String from;

    public SmsService() {
        // 초기화는 생성자 내부에서 못 쓰니 지연 로딩 필요
        this.messageService = null;
    }

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    public void sendSms(String to, String code) {
        Message message = new Message();
        message.setFrom(from);
        message.setTo(to);
        message.setText("[n:ear] 인증번호는 " + code + "입니다.");

        messageService.sendOne(new SingleMessageSendingRequest(message));
    }

}
