package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.example.demo.model.Client;
import com.example.demo.model.Counselor;
import com.example.demo.service.CounselorService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/counselor")
public class CounselorController {

    @Autowired
    private CounselorService counselorService;

    @GetMapping("/login")
    public String loginForm() {
        return "client/login";  // 상담자 로그인 JSP
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Counselor loginCounselor,
                        Model model,
                        HttpSession session) {


        Counselor counselor = counselorService.loginCounselor(loginCounselor);

        System.out.println("입력 상담사 ID: " + loginCounselor.getCounselorId());
        
        if (counselor != null) {
            session.setAttribute("loginCounselor", counselor);
            session.setAttribute("id", counselor.getCounselorId());
            System.out.println("상담사 로그인 성공!");

            String redirect = (String) session.getAttribute("redirectAfterLogin");
            if (redirect != null) {
                session.removeAttribute("redirectAfterLogin");
                return "redirect:" + redirect;
            }

            return "redirect:/";  // 로그인 성공 후 메인으로
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            model.addAttribute("role", "counselor");
            return "client/login";  // 실패 시 로그인 페이지
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
    
   @GetMapping("/mypage")
   public String mypageCounselor(HttpSession session) {
	   Counselor counselor = (Counselor) session.getAttribute("loginCounselor");
		if (counselor == null) {
			return "redirect:/client/login"; // 로그인 안돼있으면 로그인 페이지로 보내기
		}
       return "counselor/mypage";
   }
   
   @GetMapping("/profile")
   public String profile(HttpSession session) {
	   Counselor counselor = (Counselor) session.getAttribute("loginCounselor");
		if (counselor == null) {
			return "redirect:/client/login"; // 로그인 안돼있으면 로그인 페이지로 보내기
		}
       return "counselor/mypageProfile";
   }
   @GetMapping("/time")
   public String time(HttpSession session) {
	   Counselor counselor = (Counselor) session.getAttribute("loginCounselor");
	   if (counselor == null) {
		   return "redirect:/client/login"; // 로그인 안돼있으면 로그인 페이지로 보내기
	   }
	   return "counselor/mypageTime";
   }
   
   @GetMapping("/reservation")
   public String reservation(HttpSession session) {
	   Counselor counselor = (Counselor) session.getAttribute("loginCounselor");
	   if (counselor == null) {
		   return "redirect:/client/login"; // 로그인 안돼있으면 로그인 페이지로 보내기
	   }
	   return "counselor/mypageReservation";
   }
}
