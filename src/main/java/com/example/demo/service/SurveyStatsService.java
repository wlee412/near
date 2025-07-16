package com.example.demo.service;

import com.example.demo.model.SurveyHighRiskDTO;
import com.example.demo.model.SymptomAvgScoreDTO;
import com.example.demo.mapper.SurveyStatsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyStatsService {

    private final SurveyStatsMapper surveyStatsMapper;

    // 설문별 고위험군 통계
    public List<SurveyHighRiskDTO> getSurveyHighRiskStats() {
        return surveyStatsMapper.selectHighRiskStats();
    }

    // 증상 선택자 그룹별 평균 점수 통계
    public List<SymptomAvgScoreDTO> getSymptomAvgStats() {
        return surveyStatsMapper.selectSymptomAvgStats();
    }
}
