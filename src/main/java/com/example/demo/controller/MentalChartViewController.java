package com.example.demo.controller;

import com.example.demo.model.MentalHealthItem;
import com.example.demo.service.MentalHealthStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MentalChartViewController {

    private final MentalHealthStatsService mentalHealthStatsService;

    // mentalDashboard.jsp 반환 (차트 페이지)
    @GetMapping("/mental/chart")
    public String chartPage() {
        return "mental/mentalDashboard";  // /WEB-INF/views/mental/mentalDashboard.jsp
    }

    
}
