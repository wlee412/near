package com.example.demo.model;

import lombok.Data;
import java.sql.Timestamp;

@Data
public class Survey {
    private int surveyId;         // 설문 ID (Primary Key)
    private String surveyName;    // 설문 이름
    private String desc;          // 설문 설명
    private Timestamp regDate;    // 등록일시
    private String state;         // 상태 (예: active, inactive)
}