package com.example.demo.controller;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.model.AccountVerification;
import com.example.demo.model.Client;
import com.example.demo.service.ClientService;
import com.example.demo.service.SmsService;
import com.example.demo.service.VerifyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sms")
public class SmsController {

    @Autowired private SmsService smsService;
    @Autowired private VerifyService verifyService;
    @Autowired private ClientService clientService;

    private String generateCode() {
        SecureRandom random = new SecureRandom();
        return String.valueOf(random.nextInt(900000) + 100000); // 6자리
    }

    private Timestamp getExpiryTime() {
        return new Timestamp(System.currentTimeMillis() + (3 * 60 * 1000)); // 3분 후
    }

    // 📌 [1] 회원가입용 SMS 인증 요청
    @PostMapping("/send/join")
    public ResponseEntity<?> sendJoinCode(@RequestBody Map<String, String> data) {
        String phone = data.get("phone");

        // 이미 회원인 경우 차단
        if (clientService.findByPhone(phone) != null) {
            return ResponseEntity.status(409).body("이미 등록된 번호입니다.");
        }

        String code = generateCode(); // 공통으로 사용

        AccountVerification existing = verifyService.findByPhoneAndType(phone, "MEMBER_JOIN");
        if (existing != null && existing.getVerified() == 'N') {
            existing.setCode(code);
            existing.setExpiresAt(getExpiryTime());
            existing.setVerified('N');
            existing.setUsedAt(null);
            verifyService.updateVerificationForJoin(existing);
        } else {
            AccountVerification verification = new AccountVerification();
            verification.setPhone(phone);
            verification.setCode(code);
            verification.setType("MEMBER_JOIN");
            verification.setExpiresAt(getExpiryTime());
            verification.setVerified('N');
            verification.setUsedAt(null);

            verifyService.insertVerification(verification);
        }

        // SMS 발송은 여기서 공통 처리
        smsService.sendSms(phone, code);

        return ResponseEntity.ok("인증번호가 전송되었습니다.");
    }

    // 📌 [2] 아이디 찾기용 SMS 인증 요청
    @PostMapping("/send/find-id")
    public ResponseEntity<?> sendFindIdCode(@RequestBody Map<String, String> data) {
    	String phone = data.get("phone");
        Client client = clientService.findByPhone(phone);
        if (client == null) {
            return ResponseEntity.status(404).body("해당 번호로 가입된 사용자가 없습니다.");
        }

        String code = generateCode();
        smsService.sendSms(phone, code);

        AccountVerification verification = new AccountVerification();
        verification.setPhone(phone);
        verification.setClientId(client.getClientId());
        verification.setCode(code);
        verification.setType("FIND_ID");
        verification.setExpiresAt(getExpiryTime());
        verification.setVerified('N');
        verification.setUsedAt(null);
        
        System.out.println("insert 전에 phone 값: " + verification.getPhone());


        verifyService.insertVerification(verification);
        return ResponseEntity.ok("success");
    }

    // 📌 [3] 비밀번호 찾기용 SMS 인증 요청
    @PostMapping("/send/find-pw")
    public ResponseEntity<?> sendFindPwCode(@RequestBody Map<String, String> data) {
    	String clientId = data.get("clientId");
    	String phone = data.get("phone");
        Client client = clientService.findByPhone(phone);
        if (client == null || !client.getClientId().equals(clientId)) {
            return ResponseEntity.status(404).body("아이디와 휴대폰 번호가 일치하지 않습니다.");
        }

        String code = generateCode();
        smsService.sendSms(phone, code);

        AccountVerification verification = new AccountVerification();
        verification.setPhone(phone);
        verification.setClientId(clientId);
        verification.setCode(code);
        verification.setType("FIND_PASSWORD");
        verification.setExpiresAt(getExpiryTime());
        verification.setVerified('N');
        verification.setUsedAt(null);


        verifyService.insertVerification(verification);
        return ResponseEntity.ok("success");
    }

    // 📌 [공통] 인증번호 검증
    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestBody Map<String, String> data) {
        String phone = data.get("phone");
        String code = data.get("code");
        String type = data.get("type");

        Map<String, Object> result = new HashMap<>();
        AccountVerification verification = verifyService.findByPhoneAndType(phone, type);

        if (verification == null) {
            result.put("success", false);
            result.put("message", "인증 요청이 없습니다.");
            return ResponseEntity.badRequest().body(result);
        }

        if (verification.getExpiresAt().before(new Timestamp(System.currentTimeMillis()))) {
            result.put("success", false);
            result.put("message", "인증번호가 만료되었습니다.");
            return ResponseEntity.status(410).body(result);
        }

        if (!verification.getCode().equals(code)) {
            result.put("success", false);
            result.put("message", "인증번호가 일치하지 않습니다.");
            return ResponseEntity.status(401).body(result);
        }

        if (verification.getVerified() == 'Y') {
            result.put("success", false);
            result.put("message", "이미 인증된 코드입니다.");
            return ResponseEntity.status(409).body(result);
        }

        verifyService.updateVerificationTable(code, type);
        result.put("success", true);
        result.put("message", "인증 성공");
        result.put("clientId", verification.getClientId());
        return ResponseEntity.ok(result);
    }
}
