package com.example.demo.controller;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.AccountVerification;
import com.example.demo.model.Client;
import com.example.demo.service.ClientService;
import com.example.demo.service.EmailService;
import com.example.demo.service.VerifyService;


@Controller
@RequestMapping("/verify")
public class VerifyController {

	@Autowired
	private VerifyService verifyService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ClientService clientService;
	

//------------------------코드 입력후 작동되는 영역------------------------	
	//(이메일찾기) 유저가 입력한 코드값과 DB에저장된 코드값이 일치하는지 확인 
	@PostMapping("/verifyCodeForId")
	@ResponseBody
	public Map<String, Object> verifyCodeForId(
	    @RequestParam("email") String email,
	    @RequestParam("code") String code) {

	    Map<String, Object> response = new HashMap<>();

	    // FIND_ID 고정
	    String type = "FIND_ID";

	    AccountVerification verification = verifyService.findByEmailAndType(email, type);

	    if (verification == null) {
	        response.put("success", false);
	        response.put("message", "인증 기록이 존재하지 않습니다.");
	        return response;
	    }

	    // 유효기간 만료 체크
	    if (verification.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))) {
	        response.put("success", false);
	        response.put("message", "인증 시간이 만료되었습니다.");
	        return response;
	    }

	    // 코드 일치 확인
	    if (!verification.getCode().equals(code)) {
	        response.put("success", false);
	        response.put("message", "인증번호가 일치하지 않습니다.");
	        return response;
	    }

	    // 인증 성공: VERIFIED, USED_AT 업데이트
	    verifyService.updateVerificationTable(code, type);

	    Client client = clientService.findByEmail(email);
	    if (client == null) {
	        response.put("success", false);
	        response.put("message", "회원 정보가 존재하지 않습니다.");
	        return response;
	    }

	    response.put("success", true);
	    response.put("userId", client.getClientId());
	    return response;
	}
	
	@PostMapping("/verifyCodeForPw")
	@ResponseBody
	public Map<String, Object> verifyCodeForPw(@RequestParam("code") String code,
	                                           @RequestParam("email") String email) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	        AccountVerification verification = verifyService.findByEmailAndType(email, "FIND_PASSWORD");

	        if (verification == null || !verification.getCode().equals(code)) {
	            result.put("success", false);
	            result.put("message", "인증번호가 일치하지 않습니다.");
	            return result;
	        }

	        // 만료 시간 확인
	        if (verification.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))) {
	            result.put("success", false);
	            result.put("message", "인증번호가 만료되었습니다.");
	            return result;
	        }

	        // 이미 인증된 경우
	        if ("Y".equals(verification.getVerified())) {
	            result.put("success", false);
	            result.put("message", "이미 인증이 완료된 코드입니다.");
	            return result;
	        }

	        //DB 상태 업데이트 (VERIFIED = Y, USED_AT = NOW)
	        int updated = verifyService.updateVerificationTable(code, "FIND_PASSWORD");

	        if (updated > 0) {
	            result.put("success", true);
	            result.put("message", "인증 성공");
	        } else {
	            result.put("success", false);
	            result.put("message", "DB 업데이트 실패");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        result.put("success", false);
	        result.put("message", "서버 오류가 발생했습니다.");
	    }

	    return result;
	}
	
	//새로운 회원 이메일 인증 요청 받는곳
		@GetMapping("/verifyCode")
		public String verifyCode(@RequestParam("code") String code, @RequestParam("type") String type, Model model) {
			int result;
			AccountVerification verification = verifyService.findByCode(code, type);

			// 해당코드가 없을때 or 이미 확인 되었을때 -> 인증실패
			if (verification == null || "Y".equals(verification.getVerified())) {
				result = 0;
				// 만료시간이 현재시간보다 이전이면 -> 인증시간 초과
			} else if (verification.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))) {
				result = -1;
				// 인증 성공 -> ACCOUNT_VERIFICATION 테이블 인증성공 여부 & 인증시간 UPDATE
			} else {
				result = verifyService.updateVerificationTable(code, type);
						 verifyService.updateClientTable(verification.getClientId());
			}

			// 결과 값 VIEW 페이지로 넘겨주기
			model.addAttribute("result", result);
			model.addAttribute("type", type);

			// 성공결과 VIEW 페이지로 이동
			return "verify/verifyResult";
		}

		
//---------------------회원가입,아이디찾기,비밀번호찾기 이메일 전송 ------------------------		
	

	// 아이디 찾기 인증번호 전송
	@RequestMapping("/sendFindIdCode")
	@ResponseBody
	public Map<String, Object> sendFindIdCode(@RequestParam("email") String email) {
		  Map<String, Object> result = new HashMap<>();

		    try {
		        SecureRandom secureRandom = new SecureRandom();
		        String code = String.format("%06d", secureRandom.nextInt(1_000_000));

		        Client client = verifyService.findIdClient(email);
		        if (client == null) {
		            result.put("success", false);
		            result.put("message", "존재하지 않는 이메일입니다.");
		            return result;
		        }

		        AccountVerification verification = new AccountVerification();
		        verification.setClientId(client.getClientId());
		        verification.setCode(code);
		        verification.setType("FIND_ID");
		        verification.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)));

		        verifyService.insertVerification(verification);
		        emailService.sendFindIdEmail(client.getEmailId() + "@" + client.getEmailDomain(), code);
		        
		        result.put("success", true);
		        result.put("code", code);
		        result.put("id", client.getClientId());
		        return result;

		    } catch (Exception e) {
		        result.put("success", false);
		        result.put("message", "서버 오류가 발생했습니다.");
		        return result;
		    }
	}

	// 비밀번호 찾기 인증번호 전송
	@PostMapping("/sendFindPwCode")
	@ResponseBody
	public Map<String, Object> sendFindPwCode(@RequestParam("email") String email) {
	    Map<String, Object> result = new HashMap<>();

	    try {
	        SecureRandom secureRandom = new SecureRandom();
	        String code = String.format("%06d", secureRandom.nextInt(1_000_000));

	        Client client = verifyService.findIdClient(email);
	        if (client == null) {
	            result.put("success", false);
	            result.put("message", "가입된 이메일이 아닙니다.");
	            return result;
	        }

	        AccountVerification verification = new AccountVerification();
	        verification.setClientId(client.getClientId());
	        verification.setCode(code);
	        verification.setType("FIND_PASSWORD");
	        verification.setExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)));
	        verification.setUsedAt(null);
	        
	        verifyService.insertVerification(verification);
	        emailService.sendFindPwEmail(client.getEmailId() + "@" + client.getEmailDomain(), code);

	        result.put("success", true);
	        result.put("code", code);
	        return result;

	    } catch (Exception e) {
	        result.put("success", false);
	        result.put("message", "서버 오류가 발생했습니다.");
	        return result;
	    }
	}
	


}	


