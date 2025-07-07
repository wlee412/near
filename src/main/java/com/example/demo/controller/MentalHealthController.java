package com.example.demo.controller;

import com.example.demo.model.MentalHealthItem;
import com.example.demo.service.MentalHealthStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mental")
public class MentalHealthController {

    private final MentalHealthStatsService service;

    // 1. 공공 API → DB 저장
    @GetMapping("/api/mental-health")
    public void getMentalHealth() {
        String data = service.getMentalHealthDataAll();
        System.out.println("data: " + data);
    }

    // 2. 청소년 + 대학생 + 청소년 아님 차트용 요약 데이터
    @GetMapping("/chart-data/total")
    public List<MentalHealthItem> getTotalChartData() {
        List<MentalHealthItem> list = new ArrayList<>();
        list.addAll(service.selectYoungOnly());
        list.addAll(service.selectOldOnly());

        Map<String, Double> grouped = new LinkedHashMap<>();

        for (MentalHealthItem item : list) {
            String level = item.getChtXCn();
            double value = Double.parseDouble(item.getChtVl());

            String ageGroup = "기타";
            if (level.contains("미취학")) ageGroup = "미취학";
            else if (level.contains("초등")) ageGroup = "초등학생";
            else if (level.contains("중학")) ageGroup = "중학생";
            else if (level.contains("고등")) ageGroup = "고등학생";
            else if (level.contains("대학")) ageGroup = "대학생";
            else if (level.contains("청소년아님")) ageGroup = "청소년아님";

            grouped.put(ageGroup, grouped.getOrDefault(ageGroup, 0.0) + value);
        }

        List<String> order = Arrays.asList("미취학", "초등학생", "중학생", "고등학생", "대학생", "청소년아님");

        return order.stream()
                .filter(grouped::containsKey)
                .map(ageGroup -> new MentalHealthItem(ageGroup, String.valueOf(grouped.get(ageGroup))))
                .collect(Collectors.toList());
    }

    // 3. 대학생/청소년 아님 원본 데이터 그대로 보기
    @GetMapping("/chart-data/old")
    public List<MentalHealthItem> getOldData() {
        return service.selectOldOnly();
    }

    // 4. 전체 데이터 원본 조회 (테스트용)
    @GetMapping("/chart-data/test")
    public List<MentalHealthItem> testAll() {
        return service.selectAll();
    }
}
