package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	
    @GetMapping("/")
    public String main() {
    	System.out.println("main");
        return "main";
    }

    @GetMapping("/main")
    public String mainRedirect() {
    	System.out.println("main");
        return "main";
    }

}