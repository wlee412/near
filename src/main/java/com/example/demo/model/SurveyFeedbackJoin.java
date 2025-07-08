package com.example.demo.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class SurveyFeedbackJoin {
    private int responseId;
    private String clientId;
    private int surveyId;
    private String surveyName; // survey 테이블에서 가져온 필드
    private String feedback;
    private int score;
    private Timestamp regDate;
}	
