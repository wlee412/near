package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.config.WebCounselorConfig;
import com.example.demo.model.Client;
import com.example.demo.model.Counselor;

import jakarta.servlet.http.HttpSession;
@Controller
public class MainController {

//    @GetMapping("/")  
//    public String root() {
//        return "main"; 
//    }

    @GetMapping("/main")  
    public String main() {
        return "main";  
    }
    
    @Autowired
    private WebCounselorConfig webCounselorConfig;  // WebCounselorConfig 주입

    @GetMapping("/")
    public String showMainPage(Model model) {
        model.addAttribute("agentId", webCounselorConfig.getAgentId());  // 서버에서 받은 agent-id를 전달
        return "main";  // main.jsp로 반환
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
    public String reservation(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Client client = (Client) session.getAttribute("loginClient");
        Counselor counselor = (Counselor) session.getAttribute("loginCounselor");

        // 상담사 접근 차단
        if (counselor != null) {
            redirectAttributes.addFlashAttribute("message", "상담 예약은 내담자만 이용 가능합니다.");
            return "redirect:/";
        }

        // 내담자 로그인 안 된 경우
        if (client == null) {
            return "redirect:/client/login";
        }

        // ✅ 이제 null이 아니므로 안전하게 호출 가능
        System.out.println("clientId - 예약: " + client.getClientId());

        model.addAttribute("loginClient", client);
        return "reservation";
    }

    


    // 상담하기 페이지
    @GetMapping("/room/door")
    public String consultationRoom() {
        return "room/door";
    }
    
//    @GetMapping("/room/door")
//    public String consultationRoom(HttpSession session, RedirectAttributes redirectAttributes) {
//        if (session.getAttribute("loggedInClient") == null && session.getAttribute("loggedInCounselor") == null) {
//        	redirectAttributes.addFlashAttribute("message", "로그인 회원만 접근 가능합니다.");
//        	return "redirect:/" ;
//    }
//         return "room/door"; 
//    }
        
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
        return "mental/mentalDashboard";   // 어디로 이동해야할까요
    }

    @GetMapping("/mypage")
    public String mypage() {
        return "mypage/mypage"; 
    }

  
}