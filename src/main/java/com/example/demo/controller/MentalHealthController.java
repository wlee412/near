package com.example.demo.controller;

import com.example.demo.service.MentalHealthStatsService;

import com.example.demo.model.MentalHealthItem;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mental")
public class MentalHealthController {

    private final MentalHealthStatsService service;

    // 1. API → DB 저장
    @GetMapping("/api/mental-health")
    public void getMentalHealth() {
        String data = service.getMentalHealthDataAll();
        System.out.println("data: " + data);
    }

    // 2. 청소년 상세
    @GetMapping("/chart-data/young")
    public List<MentalHealthItem> getYoungChartData() {
        List<MentalHealthItem> list = service.selectYoungOnly();

        Map<String, Double> grouped = new LinkedHashMap<>();

        for (MentalHealthItem item : list) {
            String level = item.getChtXCn(); // 초등 1, 초등 2, 대학생 ...
            double value = Double.parseDouble(item.getChtVl());

            String ageGroup = "기타";
            if (level.contains("초등")) ageGroup = "초등학생";
            else if (level.contains("중학")) ageGroup = "중학생";
            else if (level.contains("고등")) ageGroup = "고등학생";
            else if (level.contains("미취학")) ageGroup = "미취학";
            else if (level.contains("대학")) ageGroup = "대학생";
            else if (level.contains("청소년 아님")) ageGroup = "청소년 아님";

            grouped.put(ageGroup, grouped.getOrDefault(ageGroup, 0.0) + value);
        }

        // 정렬 순서 지정 (원하는 대로)
        List<String> order = Arrays.asList("미취학", "초등학생", "중학생", "고등학생", "대학생", "청소년 아님");

        return order.stream()
                .filter(grouped::containsKey)
                .map(ageGroup -> new MentalHealthItem(ageGroup, String.valueOf(grouped.get(ageGroup))))
                .collect(Collectors.toList());
    }



    // 3. 대학생 이상
    @GetMapping("/chart-data/old")
    public List<MentalHealthItem> getOldData() {
        return service.selectOldOnly();
    }

    // 4. 전체 테스트용
    @GetMapping("/chart-data/test")
    public List<MentalHealthItem> testAll() {
        return service.selectAll();
    }


    }


