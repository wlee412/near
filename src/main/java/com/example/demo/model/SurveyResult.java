package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResult {

    private int surveyId;         // 설문지 ID
    private int qNum;             // 문항 번호
    private String clientId;      // 응답자 ID (로그인 사용자)
    private int answerNum;        // 선택한 보기의 점수 (0~4)
    private String answerText;    // 선택한 보기의 텍스트 (예: "보통이다")
}
