package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/reservation")
public class ReservationPageController {

    @GetMapping
    public String showReservationForm() {
        return "reservation";  // 실제 경로: /WEB-INF/views/reservation/reserve.jsp
    }
}