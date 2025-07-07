package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HospitalViewController {


    @GetMapping("/hospital/map")
    public String showHospitalMap() {
        return "map/hospitalMap"; // /WEB-INF/views/hospitalMap.jsp
    }
	
}
