package com.example.demo.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class SurveyCache {
    private Long no;
    private String clientId;
    private String interest;     // "불안,자존감,수면 문제"
    private int surveyId;    // "1002,1003,1005"
    private String explanation;
    private Timestamp createdAt;
}

