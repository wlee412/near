package com.example.demo.mapper;

import com.example.demo.model.SurveyHighRiskDTO;
import com.example.demo.model.SymptomAvgScoreDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SurveyStatsMapper {

    // 설문별 고위험군 통계
    List<SurveyHighRiskDTO> selectHighRiskStats();

    // 증상 선택자 그룹 평균 점수
    List<SymptomAvgScoreDTO> selectSymptomAvgStats();
}
