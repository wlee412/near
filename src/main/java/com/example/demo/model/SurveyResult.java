package com.example.demo.model;

import lombok.Data;

@Data
public class SurveyResult {
    private int surveyId;     // 설문 ID (복합 PK)
    private int qNum;         // 문항 번호 (복합 PK)
    private String clientId;  // 응답자 ID (복합 PK)
    private Integer score;    // 응답 점수 (nullable 가능)
}
