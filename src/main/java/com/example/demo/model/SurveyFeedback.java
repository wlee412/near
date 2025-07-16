package com.example.demo.model;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyFeedback {

    private int responseId;       // 기본 키 (자동 증가)
    private String clientId;      // 사용자 ID
    private int surveyId;         // 설문 ID
    private String feedback;      // GPT 피드백
    private int score;            // 총 점수
    private Timestamp regDate;    // 등록일 (자동 생성)
}
