package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    @GetMapping("/")  
    public String root() {
        return "main"; 
    }

    @GetMapping("/main")  
    public String main() {
        return "main";  
    }
    
    @GetMapping("/introduce")
    public String introducePage() {
        return "intro/introduce"; 
    }
    
    //푸터 약관조항
    @Controller
    public class PageController {

        @GetMapping("/terms")
        public String termsPage() {
            return "etc/terms"; 
        }

        @GetMapping("/privacy")
        public String privacyPage() {
            return "etc/privacy"; 
        }
    }
 
    // 상담예약 페이지
    @GetMapping("/reservation")
    public String reservation() {
        return "reservation";  
    }

    // 상담하기 페이지
    @GetMapping("/room/door")
    public String consultationRoom(HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("loggedInClient") == null && session.getAttribute("loggedInCounselor") == null) {
        	redirectAttributes.addFlashAttribute("message", "로그인 회원만 접근 가능합니다.");
        	return "redirect:/" ;
    }
        return "room/door"; 
    }
        
    // 심리검사 페이지
    @GetMapping("/survey/selfSurveyList")
    public String selfSurvey() {
        return "survey/selfSurveyList"; 
    }

    // 병원/약국 찾기 페이지
    @GetMapping("/hospitalMap")
    public String hospitalMap() {
        return "map/hospitalMap";  
    }

    // 멘탈케어 페이지
    @GetMapping("/mental/mentalDashboard")
    public String mentalDashboard() {
        return "mental/mentalDashboard";  // 어디를 첫페이지로 하나요? mem 오타 있음
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage/mypage"; 
    }
}
