package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.service.AdminService;

import jakarta.servlet.http.HttpSession;

	@Controller
	@RequestMapping("/admin")
	public class AdminController {

	    @Autowired
	    private AdminService adminService;

	    // 관리자 로그인폼 이동
	    @GetMapping("/adminLogin")
	    public String loginPage() {
	        return "admin/adminLogin";
	    }

	    // 관리자 로그인
	    @PostMapping("/login")
	    public String login(@RequestParam("id") String id,
	                        @RequestParam("password") String password,
	                        HttpSession session,
	                        Model model) {
	        if (adminService.login(id, password)) {
	            System.out.println("✅ 로그인 성공: " + id);
	            session.setAttribute("admin", id);
	            return "redirect:/admin/adminHome";
	        }
	        model.addAttribute("loginError", "아이디 또는 비밀번호가 올바르지 않습니다.");
	        return "admin/adminLogin";
	    }
	    
	    // 로그아웃
	    @GetMapping("/logout")
	    public String logout(HttpSession session) {
	        session.invalidate(); // 세션 전체 삭제 (로그아웃 처리)
	        return "redirect:/admin/adminLogin";
	    }
	    
	    // 홈화면으로 이동
	    @GetMapping("/adminHome")
	    public String home() {
			 System.out.println("✅ 홈 화면 진입");
	        return "admin/adminHome"; // home.jsp로 이동
	    }
		
	}
