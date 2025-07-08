package com.example.demo.model;

import lombok.Data;

@Data
public class SymptomAvgScoreDTO {
    private String symptomName;  // 예: "우울", "불안", "자존감"
    private double avgScore;     // 해당 그룹의 평균 점수
}
