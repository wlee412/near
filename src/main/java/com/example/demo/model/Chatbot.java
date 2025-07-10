package com.example.demo.model;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class Chatbot {
    private int pk;
    private String clientId;
    private String sender;
    private String message;
    private Timestamp regDate;
}
