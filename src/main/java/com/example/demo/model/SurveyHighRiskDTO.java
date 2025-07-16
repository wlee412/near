package com.example.demo.model;

import lombok.Data;

@Data
public class SurveyHighRiskDTO {
    private int surveyId;           // 설문 ID
    private String surveyName;      // 설문 제목
    private double highRiskRate;    // 고위험군 비율 (%)
}
