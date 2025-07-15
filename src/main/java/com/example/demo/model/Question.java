package com.example.demo.model;

import lombok.Data;

@Data
public class Question {
    private int surveyId;     // 설문 ID (복합 PK)
    private int qNum;         // 문항 번호 (복합 PK)
    private String question;  // 질문 내용
    private boolean isReverse; // 역문항 여부 (0 또는 1 → boolean으로 처리)
    
}
