package com.example.demo.controller;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.demo.config.WebCounselorConfig;
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

    private final HttpFirewall allowDoubleSlashFirewall;

	@Autowired
	private CounselorService counselorService;

//    CounselorController(HttpFirewall allowDoubleSlashFirewall) {
//        this.allowDoubleSlashFirewall = allowDoubleSlashFirewall;
//    }

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
//	@GetMapping("/{part}")
//	public String section(@PathVariable String part, HttpSession session, Model model) {
//		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
//		if (loginCounselor == null) {
//			return "redirect:/counselor/login";
//		}
//		model.addAttribute("counselor", loginCounselor);
//
//		switch (part) {
//		case "profile":
//			return "counselor/mypageProfile";
//
//		case "time":
//			List<CounselAvailable> times = counselorService.getAvailableTimes(loginCounselor.getCounselorId());
//			model.addAttribute("availableTimes", times);
//			return "counselor/mypageTime";
//
//		case "reservation":
//			return "counselor/mypageReservation";
//
//		case "room":
//			return "counselor/mypageRoom";
//
//		default:
//			return "error/404";
//		}
//	}

	@GetMapping("/mypage")
	public String loadMypage(Model model, HttpSession session) {
		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
		if (loginCounselor == null)
			return "redirect:/counselor/login";

		model.addAttribute("counselor", loginCounselor);

		// 오늘 접수된 상담건수
		int todayCount = counselorService.getTodayReservationCount(loginCounselor.getCounselorId());
		model.addAttribute("todayCount", todayCount);

		return "counselor/mypage";
	}

	@GetMapping("/profile")
	public String loadCounselorProfile(Model model, HttpSession session) {
		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
		if (loginCounselor == null)
			return "redirect:/counselor/login";

		model.addAttribute("counselor", loginCounselor);
		return "counselor/mypageProfile";
	}
	@GetMapping("/time")
	public String loadAvailableTime(Model model, HttpSession session) {
		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
		if (loginCounselor == null)
			return "redirect:/counselor/login";

		// 문제 발생 부분
		List<CounselAvailable> times = counselorService.getAvailableTimes(loginCounselor.getCounselorId());

		model.addAttribute("availableTimes", times);
		model.addAttribute("counselor", loginCounselor);
		return "counselor/mypageTime";
	}


	@GetMapping("/reservationDetail")
	public String showCounselorPage() {
	    return "counselor/reservationDetail"; 
	}


	// 예약 가능 시간 저장 (중복 제거 포함)
//	@PostMapping("/save")
//	@ResponseBody
//	public String saveAvailableTimes(@RequestBody Map<String, Object> requestData, HttpSession session) {
//
//	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
//	    if (loginCounselor == null) {
//	        return "unauthorized";
//	    }
//
//	    String selectedDate = (String) requestData.get("selectedDate");
//
//	    // selectedTimes를 null 체크하고, null인 경우 빈 리스트로 초기화
//	    List<String> selectedTimes = (List<String>) requestData.get("selectedTimes");
//	    if (selectedTimes == null) {
//	        selectedTimes = new ArrayList<>();
//	    }
//	    
//	    System.out.println("날짜: " + selectedDate);                         // ✅ 로그 찍기
//	    System.out.println("시간들: " + selectedTimes);
//	    
//
//	    // selectedTimes가 List<String> 형태로 제대로 전달되지 않는 경우 처리
//	    if (!(selectedTimes instanceof List)) {
//	        return "error: invalid selectedTimes format";
//	    }
//
//	    boolean success = counselorService.saveAvailableTimes(loginCounselor.getCounselorId(), selectedDate, selectedTimes);
//
//	    return success ? "success" : "error";
//	}


	// 예약 가능 시간 저장 (중복 제거 포함)
	@PostMapping("/save")
	@ResponseBody
	public String saveAvailableTimes(@RequestBody Map<String, Object> requestData, HttpSession session) {
	    Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
	    if (loginCounselor == null) {
	        return "unauthorized";
	    }

	    // selectedDate와 selectedTimes 추출
	    String selectedDate = (String) requestData.get("selectedDate");
	    List<String> selectedTimes = (List<String>) requestData.get("selectedTimes");

	    if (selectedTimes == null) {
	        selectedTimes = new ArrayList<>();
	    }

	    boolean success = counselorService.saveAvailableTimes(loginCounselor.getCounselorId(), selectedDate, selectedTimes);

	    return success ? "success" : "error";
	}

	
	@GetMapping("/existing/selectedDate/{selectedDate}")
	@ResponseBody
	public List<String> getExistingAvailableTimes(@PathVariable("selectedDate") String selectedDate,
												  HttpSession session) {
		Counselor loginCounselor = (Counselor) session.getAttribute("loginCounselor");
		if (loginCounselor == null)
			return List.of();

		List<CounselAvailable> list = counselorService.getAvailableTimes(loginCounselor.getCounselorId(), selectedDate);

		return list.stream()
			.map(item -> item.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
			.toList();
	}

	// 예약 상세 보기 페이지
	
	
	@GetMapping("/reservation/detail/{reservationNo}")
	public String getReservationDetail(@PathVariable("reservationNo") int reservationNo, Model model) {
		// 예약 상세 데이터 조회
		CounselorReservation reservation = counselorService.getReservationDetail(reservationNo);

		// 피드백 내용 처리
		String feedback = reservation.getFeedback();
		if (feedback == null || feedback.isBlank()) {
			feedback = "피드백 데이터가 없습니다.";
		}

		// ChatGPT로 심리상태 요약
		String prompt = String.format("아래는 내담자의 최근 심리설문 피드백 내용을 바탕으로 내담자의 심리상태와 상담 방향을 10줄 이내로 요약해 주세요.",
				feedback.replaceAll("\\r?\\n", " "));
		String gptSummary = callChatGPT(prompt); // GPT 호출

		// 모델에 예약 데이터와 GPT 분석 결과 추가
		model.addAttribute("reservation", reservation);
		model.addAttribute("gptSummary", gptSummary);

		// 예약 상세 페이지로 이동
		return "counselor/reservationDetail"; // JSP 또는 HTML 페이지로 이동
	}
	
	@Autowired
	private WebClient webClient;
	
	private String callChatGPT(String prompt) {
	try {
		Map<String, Object> requestBody = Map.of("model", "gpt-3.5-turbo", "messages",
				List.of(Map.of("role", "user", "content", prompt)));
		Map response = webClient.post().uri("/chat/completions").contentType(MediaType.APPLICATION_JSON)
				.bodyValue(requestBody).retrieve().bodyToMono(Map.class).block();

		Map message = (Map) ((Map) ((List) response.get("choices")).get(0)).get("message");
		return message.get("content").toString().trim();
	} catch (Exception e) {
		return "죄송합니다. 답변 중 오류가 발생했습니다.";
	}
}


	// 예약취소
	@PostMapping("/reservation/cancel")
	public String cancelSelectedReservations(@RequestParam("reservationNos") List<Integer> reservationNos,
			RedirectAttributes redirectAttributes) {
		System.out.println("선택된 예약 번호들: " + reservationNos); // 디버그용 로그

		boolean allCanceled = counselorService.cancelReservationsByCounselor(reservationNos);

		if (allCanceled) {
			redirectAttributes.addFlashAttribute("message", "선택된 예약이 모두 취소되었습니다.");
		} else {
			redirectAttributes.addFlashAttribute("error", "일부 예약 취소에 실패했습니다.");
		}

		return "redirect:/counselor/reservation"; // 예약 현황 페이지로 리다이렉트
	}

	// 페이징처리
	@GetMapping("/reservation")
	public String reservationList(@RequestParam(name = "page", defaultValue = "1") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, HttpSession session, Model model) {
		// 페이지 처리 로직
		Counselor counselor = (Counselor) session.getAttribute("loginCounselor");
		if (counselor == null) {
			return "redirect:/counselor/login";
		}

		List<CounselorReservation> list = counselorService
				.getReservationsByCounselorWithPaging(counselor.getCounselorId(), page, size);
		
		// 로그를 찍어 start 값을 확인
	    for (CounselorReservation reservation : list) {
	        System.out.println("Reservation start: " + reservation.getStart());
	    }
		int totalCount = counselorService.getTotalReservations(counselor.getCounselorId());
		int totalPages = (int) Math.ceil((double) totalCount / size);

		model.addAttribute("reservationList", list);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);

		return "counselor/mypageReservation";
	}


	@GetMapping("/counselor/reservation")
	public String getReservationList(
	    @RequestParam(defaultValue = "1") int page,
	    @RequestParam(defaultValue = "10") int size,
	    @RequestParam(defaultValue = "start") String sortColumn,
	    @RequestParam(defaultValue = "DESC") String sortOrder,
	    HttpSession session,
	    Model model
	) {
	    Counselor counselor = (Counselor) session.getAttribute("loginCounselor");
	    if (counselor == null) return "redirect:/counselor/login";

	    // SQL용 정렬 컬럼 변수 따로 분리
	    String sqlSortColumn;
	    if ("start".equals(sortColumn)) {
	        sqlSortColumn = "a.start";
	    } else if ("state".equals(sortColumn)) {
	        sqlSortColumn = "r.state";
	    } else {
	        sqlSortColumn = "a.start";
	    }

	    // 데이터 조회
	    List<CounselorReservation> list = counselorService.getPagedReservations(
	        counselor.getCounselorId(), page, size, sqlSortColumn, sortOrder
	    );
	    int totalCount = counselorService.getTotalReservations(counselor.getCounselorId());
	    int totalPages = (int) Math.ceil((double) totalCount / size);

	    // JSP에서 필요한 model 값 모두 전달!
	    model.addAttribute("reservationList", list);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("sortColumn", sortColumn); // JSP 조건 비교용
		model.addAttribute("sortOrder", sortOrder);

		return "counselor/mypageReservation";
	}
	
}
