package com.example.demo.controller;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@Autowired
	private CounselorService counselorService;

	@GetMapping("/login")
	public String loginForm() {
		return "client/login"; // 상담자 로그인 JSP
	}

	@PostMapping("/login")
	public String login(@ModelAttribute Counselor loginCounselor, Model model, HttpSession session) {

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

			return "redirect:/"; // 로그인 성공 후 메인으로
		} else {
			model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
			model.addAttribute("role", "counselor");
			return "client/login"; // 실패 시 로그인 페이지
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

//	@GetMapping("/mypage")
//	public String counselorMypage() {
//		return "counselor/mypage";
//	}

	// 마이페이지 내 각 섹션 요청
	@GetMapping("/{part}")
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

	@GetMapping("/mypage")
	public String loadMypage(Model model, HttpSession session) {
	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
	    if (loginCounselor == null) return "redirect:/counselor/login";

	    model.addAttribute("counselor", loginCounselor);

	    // 오늘 접수된 상담건수
	    int todayCount = counselorService.getTodayReservationCount(loginCounselor.getCounselorId());
	    model.addAttribute("todayCount", todayCount);

	    return "counselor/mypage";
	}

	
	@GetMapping("/profile")
	public String loadCounselorProfile(Model model, HttpSession session) {
	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
	    if (loginCounselor == null) return "redirect:/counselor/login";

	    model.addAttribute("counselor", loginCounselor);
	    return "counselor/mypageProfile";  
	}


	@GetMapping("/time")
	public String loadAvailableTime(Model model, HttpSession session) {
		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
		if (loginCounselor == null)
			return "redirect:/counselor/login";

		List<CounselAvailable> times = counselorService.getAvailableTimes(loginCounselor.getCounselorId());
		model.addAttribute("availableTimes", times);
		model.addAttribute("counselor", loginCounselor);
		return "counselor/mypageTime";
	}

	// 상담 예약 현황
	@GetMapping("/reservation")
	public String loadReservation(Model model, HttpSession session) {
		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
		if (loginCounselor == null)
			return "redirect:/counselor/login";
		
		// 상담 건수 가져오기
	    int reservationCount = reservationService.getReservationCount(loginCounselor.getCounselorId());
	    model.addAttribute("reservationCount", reservationCount);
		
		return "counselor/mypageReservation";
	}

	// 상담 방 개설
	@GetMapping("/room")
	public String loadRoom(Model model, HttpSession session) {
		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
		if (loginCounselor == null)
			return "redirect:/counselor/login";
		return "counselor/mypageRoom";
	}

	// 예약가능시간 저장
//	@PostMapping("/save")
//	public String saveAvailableTimes(@RequestParam("selectedDate") String selectedDate,
//			@RequestParam(value = "selectedTimes", required = false) List<String> selectedTimes, HttpSession session,
//			RedirectAttributes redirectAttributes) {
//
//		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
//		if (loginCounselor == null)
//			return "redirect:/counselor/login";
//
//		if (selectedTimes != null && !selectedTimes.isEmpty()) {
//			boolean success = counselorService.saveAvailableTimes(loginCounselor.getCounselorId(), selectedDate,
//					selectedTimes);
//			if (success) {
//				redirectAttributes.addFlashAttribute("msg", "예약 가능 시간이 저장되었습니다.");
//			} else {
//				redirectAttributes.addFlashAttribute("msg", "저장 중 오류가 발생했습니다.");
//			}
//		} else {
//			redirectAttributes.addFlashAttribute("msg", "선택된 시간이 없습니다.");
//		}
//
//		return "redirect:/counselor/time";
//	}

	// 예약 가능 시간 저장 (중복 제거 포함)
	@PostMapping("/save")
	@ResponseBody
	public String saveAvailableTimes(@RequestBody Map<String, Object> requestData,
	                                 HttpSession session) {

	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
	    if (loginCounselor == null) {
	        return "unauthorized";
	    }

	    String selectedDate = (String) requestData.get("selectedDate");
	    List<String> selectedTimes = (List<String>) requestData.get("selectedTimes");

	    boolean success = counselorService.saveAvailableTimes(loginCounselor.getCounselorId(), selectedDate, selectedTimes);

	    return success ? "success" : "error";
	}


	@GetMapping("/existing")
	@ResponseBody
	public List<String> getExistingAvailableTimes(HttpSession session) {
	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
	    if (loginCounselor == null) return List.of();

	    List<CounselAvailable> list = counselorService.getAvailableTimes(loginCounselor.getCounselorId());

	    // 문자열 리스트로 변환: "2025-07-24 09:00"
	    return list.stream()
	            .map(item -> item.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
	            .toList();
	}

	// 예약 현황
	@GetMapping("/reservation/count")
	@ResponseBody
	public List<Map<String, Object>> getReservationCount(HttpSession session) {
	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
	    if (loginCounselor == null) return Collections.emptyList();

	    return counselorService.getReservationCountByDate(loginCounselor.getCounselorId());
	}


}
