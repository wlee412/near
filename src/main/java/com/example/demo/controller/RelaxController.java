package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RelaxController {
	
    @GetMapping("/relax/typing")
    public String showTypingGame() {
        return "relax/typedownGame"; // ✅ 여기!
    }

    @GetMapping("/relax/matching")
    public String showMatchingGame() {
        return "relax/emojipuzzleGame";
    }

    @GetMapping("/relax/balloon")
    public String showWeatherGame() {
        return "relax/balloonGame";
    }
}
