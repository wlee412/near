package com.example.demo.controller;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.CounselAvailable;
import com.example.demo.model.CounselReservation;
import com.example.demo.model.Counselor;
import com.example.demo.model.CounselorReservation;
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
//	@GetMapping("/reservation")
//	public String loadReservation(Model model, HttpSession session) {
//		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
//		if (loginCounselor == null)
//			return "redirect:/counselor/login";
//		
//		// 상담 건수 가져오기
//		int reservationCount = counselorService.getReservationCount(loginCounselor.getCounselorId());
//	    model.addAttribute("reservationCount", reservationCount);
//		
//		return "counselor/mypageReservation";
//	}

	// 상담 예약 현황
	@GetMapping("/reservation")
	public String reservationList(HttpSession session, Model model) {
	    Counselor counselor = (Counselor) session.getAttribute("loginCounselor");
	    if (counselor == null) return "redirect:/counselor/login";

	    List<CounselorReservation> list = counselorService.getReservationsByCounselor(counselor.getCounselorId());
	    model.addAttribute("reservationList", list);
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

//	// 예약 현황
//	@GetMapping("/reservation/count")
//	@ResponseBody
//	public List<Map<String, Object>> getReservationCount(HttpSession session) {
//	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
//	    if (loginCounselor == null) return Collections.emptyList();
//
//	    return counselorService.getReservationCountByDate(loginCounselor.getCounselorId());
//	}

//
//	// 상담 예약 상세
//	@GetMapping("/reservation/detail")
//	@ResponseBody
//	public CounselorReservation getReservationDetail(@RequestParam("no") int reservationNo) {
//	    CounselorReservation res = counselorService.getReservationDetail(reservationNo);
//
//	    String gptResult = counselorService.analyzeClient(res);
//	    res.setGptSummary(gptResult);
//
//	    return res;
//	}
//
//	// 날짜별 상담 예약 리스트
//	@GetMapping("/reservation/list")
//	@ResponseBody
//	public List<CounselorReservation> getReservationList(@RequestParam("date") String date, HttpSession session) {
//	    Counselor counselor = (Counselor) session.getAttribute("loginCounselor");
//	    if (counselor == null) return List.of();
//
//	    System.out.println("상담사 ID: " + counselor.getCounselorId());
//	    System.out.println("요청 날짜: " + date);
//
//	    List<CounselorReservation> list = counselorService.getReservationsByDate(date, counselor.getCounselorId());
//	    System.out.println("예약 수: " + list.size());
//	    
//	    for (CounselorReservation res : list) {
//	        String summary = counselorService.analyzeClient(res);
//	        res.setGptSummary(summary);
//	    }
//
//	    return list;
//	}

	    // WebClient는 반드시 인스턴스로 선언! (Bean 주입 또는 직접 생성)
	    @Autowired
	    private WebClient webClient;

	    @GetMapping("/reservation/detail")
	    @ResponseBody
	    public Map<String, Object> getReservationDetail(@RequestParam("no") int reservationNo) {
	        CounselorReservation res = counselorService.getReservationDetail(reservationNo);

	        String feedback = res.getFeedback();
	        if (feedback == null || feedback.isBlank()) feedback = "피드백 데이터가 없습니다.";

	        String prompt = String.format(
	            "아래는 내담자의 최근 심리설문 피드백 내용입니다 이 피드백을 바탕으로 내담자의 심리상태와 상담 방향을 10줄 이내로 요약해 주세요.",
	            feedback.replaceAll("\\r?\\n", " ") 
	        );

	        String gptSummary = callChatGPT(prompt);

	        Map<String, Object> map = new HashMap<>();
	        map.put("start", res.getStart());
	        map.put("clientId", res.getClientId());
	        map.put("name", res.getName());
	        map.put("birth", res.getBirth());
	        map.put("gender", res.getGender());
	        map.put("phone", res.getPhone());
	        map.put("address", res.getAddress());
	        map.put("state", res.getState());
	        map.put("gptSummary", gptSummary);
	        map.put("interest", res.getInterest());
	        return map;
	    }

	    private String callChatGPT(String prompt) {
	        try {
	            Map<String, Object> requestBody = Map.of(
	                "model", "gpt-3.5-turbo",
	                "messages", List.of(
	                    Map.of("role", "user", "content", prompt)
	                )
	            );
	            Map response = webClient.post()
	                .uri("/chat/completions")
	                .contentType(MediaType.APPLICATION_JSON)
	                .bodyValue(requestBody)
	                .retrieve().bodyToMono(Map.class).block();

	            Map message = (Map) ((Map) ((List) response.get("choices")).get(0)).get("message");
	            return message.get("content").toString().trim();
	        } catch (Exception e) {
	            return "죄송합니다. 답변 중 오류가 발생했습니다.";
	        }
	    }
	    
	}


