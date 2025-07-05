package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mental/chart")
public class MentalChartViewController {

    @GetMapping("")
    public String chartView() {
        return "mental/chartView";  // young + old 둘 다 출력
    }
}
