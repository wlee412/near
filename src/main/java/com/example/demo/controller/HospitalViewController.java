package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Client;

import jakarta.servlet.http.HttpSession;

@Controller
public class HospitalViewController {


    @GetMapping("/hospital/map")
    public String showHospitalMap(HttpSession session, Model model) {
    	Client client = (Client) session.getAttribute("loginClient");

		// null 체크
		if (client == null) {
			return "redirect:/client/login"; // 로그인 안돼있으면 로그인 페이지로 보내기
		}

		model.addAttribute("loginClient", client);
        return "map/hospitalMap"; // /WEB-INF/views/hospitalMap.jsp
    }
	
}