package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PharmacyViewController {


    @GetMapping("/pharmacy/map")
    public String showHospitalMap() {
        return "map/pharmacyMap"; // /WEB-INF/views/pharmacyMap.jsp
    }
	
}
