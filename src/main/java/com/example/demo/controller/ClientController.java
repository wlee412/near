package com.example.demo.controller;

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
import com.example.demo.service.ClientService;
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
	
	@ResponseBody
	@GetMapping("/check-phone")
	public String checkPhone(
	    @RequestParam("phone") String phone,
	    HttpSession session) {

	    Client me = (Client) session.getAttribute("loginClient");

	    if (me != null && phone.equals(me.getPhone())) {
	        return "ok";
	    }

	    boolean exists = clientService.checkPhoneExists(phone);
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
				        @RequestParam(name = "rememberMe", required = false) String rememberMe, 
				        Model model, 
				        HttpSession session,
				        HttpServletResponse response) {
		System.out.println("입력 ID: " + login.getClientId());
		System.out.println("입력 PW: " + login.getPassword());
		Client client = clientService.login(login);
		System.out.println("DB에서 찾은 Client: " + client);
		if (client != null) {
			session.setAttribute("loginClient", client);
			session.setAttribute("id", client.getClientId());
			if (rememberMe != null) {
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

	@GetMapping("/logout")
	public String logout(HttpSession session, HttpServletResponse response) {
		session.invalidate();
		Cookie cookie = new Cookie("reclientId", null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
		return "redirect:client/login";
	}

	
	
	

	// 회원 수정 폼
	

	// 비밀번호 변경 폼
	@GetMapping("/changePassword")
	public String changePasswordForm(HttpSession session, Model model) {
		Client client = (Client) session.getAttribute("loginClient");

		// null 체크
		if (client == null) {
			return "redirect:/client/login"; // 로그인 안돼있으면 로그인 페이지로 보내기
		}

		model.addAttribute("loginClient", client);
		return "mypage/mypagePassword";
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
			return "redirect:/mypage/mypagePassword";
		}

		// 새 비밀번호 유효성 검사
		String pwRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?`~]).{8,}$";
		if (!newPassword.matches(pwRegex)) {
			redirectAttributes.addFlashAttribute("error", "❌ 비밀번호는 8자 이상, 영문+숫자+특수문자를 포함해야 합니다.");
			return "redirect:/mypage/changePassword";
		}

		// 새 비밀번호 일치 여부 확인
		if (!newPassword.equals(confirmPassword)) {
			redirectAttributes.addFlashAttribute("error", "❌ 새 비밀번호가 서로 다릅니다.");
			return "redirect:/mypage/mypagePassword";
		}

		// 비밀번호 업데이트
		client.setPassword(passwordEncoder.encode(newPassword));
		clientService.updatePassword(client);

		redirectAttributes.addFlashAttribute("success", " 비밀번호가 성공적으로 변경되었습니다. ");
		return "redirect:/mypage/";
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
	public String deleteClient(
	        @RequestParam(value="social",    required=false) String social,
	        @RequestParam(value="pw",        required=false) String pw,
	        @RequestParam(value="pwConfirm", required=false) String pwConfirm,
	        HttpSession session,
	        RedirectAttributes redirectAttributes) {

	    Client loginClient = (Client) session.getAttribute("loginClient");
	    String clientId = loginClient.getClientId();

	    // ① 소셜 로그인 사용자는 비밀번호 없이 바로 탈퇴/연동 해제
	    if (loginClient.getSocialPlatform() != null || "true".equals(social)) {
	        boolean result = clientService.deleteClient(clientId);
	        if (!result) {
	            redirectAttributes.addFlashAttribute("error", "회원 탈퇴에 실패했습니다.");
	            return "redirect:/mypage";
	        }
	        session.invalidate();
	        return "redirect:/main";
	    }

	    // ② 일반 회원은 기존대로 pw 검사
	    if (pw == null || pwConfirm == null) {
	        redirectAttributes.addFlashAttribute("error", "비밀번호를 입력해 주세요.");
	        return "redirect:/mypage/mypageDelete";
	    }
	    if (!pw.equals(pwConfirm)) {
	        redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");
	        return "redirect:/mypage/mypageDelete";
	    }
	    if (!passwordEncoder.matches(pw, loginClient.getPassword())) {
	        redirectAttributes.addFlashAttribute("error", "비밀번호가 올바르지 않습니다.");
	        return "redirect:/mypage/mypageDelete";
	    }

	    // ③ 일반 회원 탈퇴 처리
	    boolean result = clientService.deleteClient(clientId);
	    if (!result) {
	        redirectAttributes.addFlashAttribute("error", "회원 탈퇴에 실패했습니다.");
	        return "redirect:/mypage/";
	    }
	    session.invalidate();
	    return "redirect:/main";
	}



}