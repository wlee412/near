package com.example.demo.controller;

import com.example.demo.model.SurveyHighRiskDTO;
import com.example.demo.model.SymptomAvgScoreDTO;
import com.example.demo.service.SurveyStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class SurveyStatsController {

    private final SurveyStatsService statsService;

    /**
     * 관리자 설문 통계 페이지 진입
     * - 전체 설문 고위험군 비율
     * - 증상 선택 그룹별 평균 점수
     */
    @GetMapping("/surveyStats")
    public String surveyStatsPage(Model model) {
        // 설문별 고위험군 비율 (예: ASRS, 자존감, 우울 등 전체)
        List<SurveyHighRiskDTO> highRiskStats = statsService.getSurveyHighRiskStats();

        // 관심 증상 선택자 그룹 평균 점수 (예: 불안 선택자 그룹의 평균 GAD-7 점수)
        List<SymptomAvgScoreDTO> symptomAvgStats = statsService.getSymptomAvgStats();

        // JSP로 데이터 전달
        model.addAttribute("highRiskStats", highRiskStats);
        model.addAttribute("symptomAvgStats", symptomAvgStats);

        return "admin/surveyStats";
    }
}
