package com.example.demo.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Client;
import com.example.demo.model.Survey;
import com.example.demo.model.SurveyFeedback;
import com.example.demo.model.SurveyFeedbackJoin;
import com.example.demo.service.ChatGptService;
import com.example.demo.service.ClientService;
import com.example.demo.service.SurveyFeedbackService;
import com.example.demo.service.SurveyService;
import com.example.demo.service.VerifyService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/client")
public class ClientController {


	private final PasswordEncoder passwordEncoder;
	
	ClientController(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	@Autowired
	private ClientService clientService;

	@Autowired
	private VerifyService verifyService;
	
	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private ChatGptService chatGptService;
	
	@Autowired
	private SurveyFeedbackService surveyFeedbackService;

	// 회원가입 화면
	@GetMapping("/register")
	public String register() {
		return "client/register";
	}

	// 아이디 중복 확인
	@ResponseBody
	@GetMapping("/check-id")
	public String checkId(@RequestParam("id") String id) {
		boolean exists = clientService.checkIdExists(id);
		return exists ? "duplicate" : "ok";
	}

	// 이메일 중복 확인
	@PostMapping("/checkEmailDuplicate")
	@ResponseBody
	public boolean checkEmailDuplicate(@RequestBody Map<String, String> data) {
		String email = data.get("email");
		return clientService.findByEmailForRegister(email) != null;
	}

	//회원가입 폼
	@PostMapping("/register")
	public String register(Client client, 
						   @RequestParam("pwConfirm") String pwConfirm, 
						   @RequestParam(value = "interestList", required = false) List<String> interestList,
						   Model model,
						   RedirectAttributes redirectAttributes) {

		// ===== 2. 비밀번호확인 =====
		if (!client.getPassword().equals(pwConfirm)) {
			model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
			return "client/register";
		}

		// ===== 3. emailVerified 기본값 처리 =====
		client.setVerified("N");

		// 정상회원 상태값 명시적 0으로 설정
		client.setState(0); //
		
		if (interestList != null && !interestList.isEmpty()) {
		    String interestStr = String.join(",", interestList);
		    client.setInterest(interestStr);  // 이제 정상 작동
		} else {
		    client.setInterest(""); // 선택 안 했을 경우
		}
		
		// ===== 4. 비밀번호 암호화 =====
		client.setPassword(passwordEncoder.encode(client.getPassword()));

		// ===== 5. 회원가입 처리 =====
		client.setVerified("Y");
		boolean result = clientService.registerClient(client);
		
		// ===== 6. 결과 처리 =====
		if (result) {
			 verifyService.insertClientIdByPhone(client.getPhone(), client.getClientId());
			redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다!");
			return "redirect:/client/login";
		} else {
			model.addAttribute("error", "회원가입 중 오류가 발생했습니다.");
			return "client/register";
		}
	}

	@GetMapping("/login")
	public String login() {
		return "client/login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute Client login,
				        @RequestParam(name = "reclientMe", required = false) String reclientMe, 
				        Model model, 
				        HttpSession session,
				        HttpServletResponse response) {
		System.out.println("입력 ID: " + login.getClientId());
		System.out.println("입력 PW: " + login.getPassword());
		Client client = clientService.login(login);
		System.out.println("DB에서 찾은 Client: " + client);
		if (client != null) {
//			if ("N".equals(client.getVerified())) {
//	            model.addAttribute("error", "휴대폰 인증이 완료되지 않았습니다.");
//	            return "client/login";
//	        }
			session.setAttribute("loginClient", client);
			session.setAttribute("id", client.getClientId());
			if (reclientMe != null) {
				Cookie cookie = new Cookie("reclientId", client.getClientId());
				cookie.setMaxAge(60 * 60 * 24 * 3);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
			String redirect = (String) session.getAttribute("redirectAfterLogin");
			if (redirect != null) {
				session.removeAttribute("redirectAfterLogin");
				return "redirect:" + redirect;
			}

			System.out.println("로그인 성공!");
			return "redirect:/"; // 로그인 성공 시 메인 페이지로 이동
		} else {
			model.addAttribute("error", "아이디 또는 비밀번호가 틀렸습니다.");
			return "client/login"; // 로그인 실패 시 로그인 화면으로
		}
	}


	@GetMapping("/find")
	public String find() {
		return "client/find";
	}

	// 추가함 25.06.20 (영교님꺼)
	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletResponse response) {
		session.invalidate();
		Cookie cookie = new Cookie("reclientId", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		return "redirect:/login";
	}

//******************--------------------------------    

// 마이페이지 추가

	@GetMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
	    Client client = (Client) session.getAttribute("loginClient");
	    model.addAttribute("client", client);

	    // 관심사 유효성 체크
	    if (client == null || client.getInterest() == null || client.getInterest().isEmpty()) {
	        model.addAttribute("recommendExplanation", "관심사가 등록되지 않았습니다.");
	        return "client/mypage";
	    }

	    String interestStr = client.getInterest();
	    String[] interestArr = interestStr.split(",");

	    // 설문 추천
	    List<Survey> recommendedSurveys = surveyService.getRecommendedSurveys(interestArr);
	    model.addAttribute("recommendedSurveys", recommendedSurveys);

	    // GPT 설명 캐시 로직
	    if (interestArr.length > 0 && !recommendedSurveys.isEmpty()) {
	        try {
	            String explanation = chatGptService.getOrCreateSurveyExplanation(
	                client.getClientId(), interestArr, recommendedSurveys
	            );
	            model.addAttribute("recommendExplanation", explanation);
	        } catch (Exception e) {
	            e.printStackTrace();
	            model.addAttribute("recommendExplanation", "GPT 설명 생성 실패");
	            System.out.println("GPT 오류 응답 내용: " + e.getMessage());
	        }
	    }

	    // 가입일로부터 D+ 계산
	    if (client.getRegDate() != null) {
	        LocalDate joinDate = client.getRegDate().toLocalDateTime().toLocalDate();
	        LocalDate today = LocalDate.now(ZoneId.systemDefault());
	        long dday = ChronoUnit.DAYS.between(joinDate, today);
	        model.addAttribute("dDay", dday);
	    }

	    return "client/mypage";
	}

	// 마이페이지 프로필 화면
	@GetMapping("/mypageProfile")
	public String mypageProfile(HttpSession session, Model model) {
		Client client = (Client) session.getAttribute("loginClient");
		model.addAttribute("client", client);
		return "client/mypageProfile"; // /WEB-INF/views/client/mypageProfile.jsp
	}
	
	// 설문조사 결과
	@GetMapping("/mypageReport")
	public String mypageReport(HttpSession session, Model model) {
	    Client client = (Client) session.getAttribute("loginClient");

	    if (client == null) {
	        return "redirect:/login"; // 비로그인 사용자는 로그인 페이지로
	    }

	    String clientId = client.getClientId();
	    List<SurveyFeedbackJoin> feedbackList = surveyFeedbackService.getFeedbackByClientId(clientId);

	    model.addAttribute("client", client);
	    model.addAttribute("feedbackList", feedbackList);
	    return "client/mypageReport";
	}


	// 마이페이지 회원 정보 수정 화면
	@GetMapping("/mypageUpdate")
	public String mypageUpdate(HttpSession session, Model model) {
	    Client sessionClient = (Client) session.getAttribute("loginClient");
	    if (sessionClient == null) {
	        return "redirect:/login"; 
	    }
	    Client client = clientService.getClientById(sessionClient.getClientId());

	    String interestStr = client.getInterest();
	    String[] interestArr = interestStr != null ? interestStr.split(",") : new String[0];
	    System.out.println("mypageUpdate - client: " + client.toString());
	    model.addAttribute("interestList", Arrays.asList(interestArr));
	    model.addAttribute("client", client);

	    return "client/mypageUpdate";
	}

	// 마이페이지 비밀번호 변경 화면
	@GetMapping("/mypagePassword")
	public String mypagePassword(HttpSession session, Model model) {
		Client client = (Client) session.getAttribute("loginClient");
		model.addAttribute("loginClient", client);
		return "client/mypagePassword";
	}

	// 마이페이지 회원 탈퇴 화면
	@GetMapping("/mypageDelete")
	public String mypageDelete(HttpSession session, Model model) {
		Client client = (Client) session.getAttribute("loginClient");
		model.addAttribute("loginClient", client);
		return "client/mypageDelete";
	}

	// 회원 수정 폼
	@GetMapping("/update")
	public String showUpdateForm(HttpSession session, Model model) {
		Client loginClient = (Client) session.getAttribute("loginClient");

		if (loginClient == null) {
			return "redirect:/client/login"; // 로그인 안 했으면 로그인으로
		}

		model.addAttribute("client", loginClient); // JSP에 회원 정보 넘겨줌
		return "client/update"; // 수정 폼 화면 반환
	}

	// 회원 정보 수정 처리
	@PostMapping("/update")
	public String updateClient(@ModelAttribute Client client, 
			  				   @RequestParam(value = "interestList", required = false) List<String> interestList,
							   HttpSession session,
			                   RedirectAttributes redirectAttributes) {
		
		Client loginClient = (Client) session.getAttribute("loginClient");

		if (loginClient == null) {
			return "redirect:/client/login"; // 로그인 안 한 경우 login폼으로 이동
		}

		// 세션의 ID로 고정 (보안상 중요!)
		client.setClientId(loginClient.getClientId());
		

	    // ✅ 일반 로그인 사용자는 성별 수정 불가 (기존 값 유지)
	    if (!"N".equals(loginClient.getGender())) {
	        client.setGender(loginClient.getGender());
	    }

	    // ✅ 관심사 체크박스를 문자열로 병합
	    if (interestList != null && !interestList.isEmpty()) {
	        client.setInterest(String.join(",", interestList));
	    } else {
	        client.setInterest(""); // 아무것도 선택 안 한 경우
	    }


		boolean result = clientService.updateClient(client);
		
		if (result) {
			 // ✅ DB에서 최신 회원 정보 가져옴
		    Client refreshed = clientService.getClientById(client.getClientId());

		    // ✅ 로그로 관심사 확인
		    System.out.println("[DEBUG] 수정 후 DB에서 가져온 관심사: " + refreshed.getInterest());

		    // ✅ 세션에 최신 정보 저장
		    session.setAttribute("loginClient", refreshed);

		    redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
		    return "redirect:/client/mypageProfile";
		} else {
			// 실패 메시지 추가
			redirectAttributes.addFlashAttribute("error", "회원 정보 수정에 실패했습니다.");

			// 수정 페이지로 리다이렉트
			return "redirect:/client/mypageUpdate";
		}
	}

	// 비밀번호 변경 폼
	@GetMapping("/changePassword")
	public String changePasswordForm(HttpSession session, Model model) {
		Client client = (Client) session.getAttribute("loginClient");

		// null 체크
		if (client == null) {
			return "redirect:/client/login"; // 로그인 안돼있으면 로그인 페이지로 보내기
		}

		model.addAttribute("loginClient", client);
		return "client/mypagePassword";
	}

	// 비밀번호 변경 처리
	@PostMapping("/changePassword")
	public String changePassword(@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
			HttpSession session, RedirectAttributes redirectAttributes) {

		Client client = (Client) session.getAttribute("loginClient");

		// 현재 비밀번호 확인
		if (!passwordEncoder.matches(currentPassword, client.getPassword())) {
			redirectAttributes.addFlashAttribute("error", "❌ 현재 비밀번호가 일치하지 않습니다.");
			return "redirect:/client/mypagePassword";
		}

		// 새 비밀번호 유효성 검사
		String pwRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?`~]).{8,}$";
		if (!newPassword.matches(pwRegex)) {
			redirectAttributes.addFlashAttribute("error", "❌ 비밀번호는 8자 이상, 영문+숫자+특수문자를 포함해야 합니다.");
			return "redirect:/client/changePassword";
		}

		// 새 비밀번호 일치 여부 확인
		if (!newPassword.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("error", "❌ 새 비밀번호가 서로 다릅니다.");
			return "redirect:/client/mypagePassword";
		}

		// 비밀번호 업데이트
		client.setPassword(passwordEncoder.encode(newPassword));
		clientService.updatePassword(client);

		redirectAttributes.addFlashAttribute("success", " 비밀번호가 성공적으로 변경되었습니다. ");
		return "redirect:/client/mypage";
	}

	// ✅ 현재 비밀번호 일치 확인용 (AJAX 요청 처리)
	@PostMapping("/checkPassword")
	@ResponseBody
	public Map<String, Boolean> checkPassword(@RequestBody Map<String, String> requestBody,
			@SessionAttribute("loginClient") Client loginClient) {
		String currentPassword = requestBody.get("currentPassword");
		boolean match = passwordEncoder.matches(currentPassword, loginClient.getPassword());

		Map<String, Boolean> result = new HashMap<>();
		result.put("match", match);
		return result;
	}
	
	// 비번찾기 - 새비번설정
	@PostMapping("/resetPassword")
	@ResponseBody
	public Map<String, Object> resetPassword(@RequestBody Map<String, String> data) {
		String clientId = data.get("clientId");
		String newPassword = data.get("newPassword");

		Map<String, Object> result = new HashMap<>();

		Client client = clientService.findById(clientId);
		if (client == null) {
			result.put("success", false);
			result.put("message", "회원 정보 없음");
			return result;
		}

		// 비밀번호 암호화
		String encodedPw = passwordEncoder.encode(newPassword);

		// Map으로 전달
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("clientId", clientId);
		paramMap.put("newPw", encodedPw);

		boolean updated = clientService.updatePasswordForFind(paramMap); // service 메서드도 새로 만들어야 함

		if (updated) {
			result.put("success", true);
		} else {
			result.put("success", false);
			result.put("message", "DB 업데이트 실패");
		}

		return result;
	}

	// 탈퇴 폼 GET
	@GetMapping("/delete")
	public String deleteForm(HttpSession session, Model model) {
		Client client = (Client) session.getAttribute("loginClient");
		model.addAttribute("loginClient", client);
		return "client/delete";
	}
	//예약확인 
	@GetMapping("/mypageReservation")
	public String clientReservation(HttpSession session, Model model) {
		Client client = (Client) session.getAttribute("loginClient");
//		ClientReservation reservation = clientReservationService.getReservationList();
		model.addAttribute("loginClient", client);
		return "client/mypageReservation";
	}

	// ✅ 탈퇴 처리 POST
	@PostMapping("/delete")
	public String deleteClient(@RequestParam("id") String id, 
							   @RequestParam("pw") String pw,
							   @RequestParam("pwConfirm") String pwConfirm, 
							   HttpSession session, 
							   RedirectAttributes redirectAttributes) {
		Client loginClient = (Client) session.getAttribute("loginClient");

		// 비밀번호 일치 확인
		if (!pw.equals(pwConfirm)) {
			redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
			return "redirect:/client/mypageDelete";
		}

		// 실제 비밀번호 비교 (암호화된 비밀번호와 비교)
		if (!passwordEncoder.matches(pw, loginClient.getPassword())) {
			redirectAttributes.addFlashAttribute("error", "비밀번호가 올바르지 않습니다.");
			return "redirect:/client/mypageDelete";
		}

		// 탈퇴처리
		boolean result = clientService.deleteClient(loginClient.getClientId());
		if (!result) {
			redirectAttributes.addFlashAttribute("error", "회원 탈퇴에 실패했습니다.");
			return "redirect:/client/mypage";
		}

		// 탈퇴 성공시 세션 끊고 메인으로 이동하기
		session.invalidate();
		return "redirect:/main";
	}


}