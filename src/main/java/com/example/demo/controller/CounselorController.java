package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.CounselAvailable;
import com.example.demo.model.Counselor;
import com.example.demo.service.CounselorService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/counselor")
@RequiredArgsConstructor
public class CounselorController {

    private final CounselorService counselorService;

    @GetMapping("/login")
    public String loginForm() {
        return "counselor/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Counselor loginCounselor, 
    		       HttpSession session, Model model) {
        Counselor c = counselorService.login(loginCounselor);
        if (c != null) {
            session.setAttribute("loginCounselor", c);
            return "redirect:/counselor/mypage";
        } else {
            model.addAttribute("error", "상담사 ID 또는 비밀번호가 틀렸습니다.");
            return "counselor/login";
        }
    }

    // 상담사 마이페이지 
    @GetMapping("/mypage")
    public String mypage() {
        return "counselor/mypage";
    }

    @GetMapping("/section/{part}")
    public String section(@PathVariable String part) {
        switch (part) {
            case "profile": return "counselor/mypageProfile";
            case "time": return "counselor/mypageTime";
            case "reservation": return "counselor/mypageReservation";
            case "room": return "counselor/mypageRoom";
            default: return "error/404";
        }
    }
    @GetMapping("/section/profile")
    public String loadCounselorProfile(Model model, HttpSession session) {
        Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
        model.addAttribute("counselor", loginCounselor);
        return "counselor/mypageProfile";
    }
//    // 예약가능시간 설정
//    @PostMapping("/available-times")
//    @ResponseBody
//    public ResponseEntity<?> saveAvailableTimes(
//            @RequestBody CounselAvailableRequest request,
//            HttpSession session) {
//
//        // 로그인 상담사 검증
//        Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
//        if (loginCounselor == null) {
//            return ResponseEntity.status(401).body("Unauthorized");
//        }
//
//        // 유효성 검사
//        if (request == null || request.getAvailableTimes() == null || request.getAvailableTimes().isEmpty()) {
//            return ResponseEntity.badRequest().body("No available times provided");
//        }
//
//        // 서비스 호출
//        String counselorId = loginCounselor.getId();
//        counselorService.saveAvailableTimes(counselorId, request.getAvailableTimes());
//
//        return ResponseEntity.ok("Saved successfully");
//    }

    
}
