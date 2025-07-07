package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // 로그인 폼
    @GetMapping("/login")
    public String loginForm() {
        return "counselor/login";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(@ModelAttribute Counselor loginCounselor,
                        HttpSession session, Model model) {
        Counselor c = counselorService.login(loginCounselor);
        if (c != null) {
            session.setAttribute("loginCounselor", c);
            return "redirect:/counselor/mypage";
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
            return "client/login";
        }
    }

    // 마이페이지 진입
    @GetMapping("/mypage")
    public String mypage() {
        return "counselor/mypage";
    }

    // 마이페이지 내 각 섹션 요청
    @GetMapping("/section/{part}")
    public String section(@PathVariable String part, HttpSession session, Model model) {
        Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
        if (loginCounselor == null) {
            return "redirect:/counselor/login";
        }
        model.addAttribute("counselor", loginCounselor);

        switch (part) {
            case "profile":
                return "counselor/mypageProfile";

            case "time":
                List<CounselAvailable> times = counselorService.getAvailableTimes(loginCounselor.getCounselorId());
                model.addAttribute("availableTimes", times);
                return "counselor/mypageTime";

            case "reservation":
                return "counselor/mypageReservation";

            case "room":
                return "counselor/mypageRoom";

            default:
                return "error/404";
        }
    }

	@GetMapping("/section/profile")
	public String loadCounselorProfile(Model model, HttpSession session) {
		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
		model.addAttribute("counselor", loginCounselor);
		return "counselor/mypageProfile";
	}
	
	@GetMapping("/section/time")
	public String loadAvailableTime(Model model, HttpSession session) {
	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
	    if (loginCounselor == null) return "redirect:/counselor/login";

	    List<CounselAvailable> times = counselorService.getAvailableTimes(loginCounselor.getCounselorId());
	    model.addAttribute("availableTimes", times);
	    model.addAttribute("counselor", loginCounselor);
	    return "counselor/mypageTime";
	}

	// 상담 예약 현황
	@GetMapping("/section/reservation")
	public String loadReservation(Model model, HttpSession session) {
	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
	    if (loginCounselor == null) return "redirect:/counselor/login";
	    // 예약 정보 model.addAttribute(...) 필요 시 추가
	    return "counselor/mypageReservation";
	}

	// 상담 방 개설
	@GetMapping("/section/room")
	public String loadRoom(Model model, HttpSession session) {
	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
	    if (loginCounselor == null) return "redirect:/counselor/login";
	    return "counselor/mypageRoom";
	}
	
    //예약가능시간 저장
    @PostMapping("/time/save")
    public String saveAvailableTimes(@RequestParam("selectedDate") String selectedDate,
                                     @RequestParam(value = "selectedTimes", required = false) List<String> selectedTimes,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
        if (loginCounselor == null) return "redirect:/counselor/login";

        if (selectedTimes != null && !selectedTimes.isEmpty()) {
            boolean success = counselorService.saveAvailableTimes(
                loginCounselor.getCounselorId(), selectedDate, selectedTimes
            );
            if (success) {
                redirectAttributes.addFlashAttribute("msg", "예약 가능 시간이 저장되었습니다.");
            } else {
                redirectAttributes.addFlashAttribute("msg", "저장 중 오류가 발생했습니다.");
            }
        } else {
            redirectAttributes.addFlashAttribute("msg", "선택된 시간이 없습니다.");
        }

        return "redirect:/counselor/section/time";
    }



}
