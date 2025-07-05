package com.example.demo.controller;

import com.example.demo.service.MentalHealthStatsService;
import com.example.demo.model.MentalHealthItem;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mental")
public class MentalHealthController {

    private final MentalHealthStatsService service;

 // API DB 저장
    @GetMapping("/api/mental-health")
    public void getMentalHealth() {
        String data = service.getMentalHealthDataAll();
        System.out.println("data: " + data);
    }

    // 청소년
    @GetMapping("/chart-data/young")
    public List<MentalHealthItem> getYoungChartData() {
        List<MentalHealthItem> list = service.selectYoungOnly();
        System.out.println("청소년 데이터 개수: " + list.size());
        return list;
    }

    // 대학생 이상
    @GetMapping("/chart-data/old")
    public List<MentalHealthItem> getOldData() {
        return service.selectOldOnly();
    }

    // 전체 테스트
    @GetMapping("/chart-data/test")
    public List<MentalHealthItem> testAll() {
        return service.selectAll();
    }

    // 연령대별 평균 (미취학 ~ 청소년아님까지)
    @GetMapping("/chart-data/summary")
    public List<Map<String, Object>> getAgeGroupSummary() {
        return service.selectAvgByAgeGroup();
    }
}