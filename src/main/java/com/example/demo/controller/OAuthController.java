package com.example.demo.controller;


import java.sql.Date;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.mapper.ClientMapper;
import com.example.demo.model.Client;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OAuthController {

    private final HttpSession session;
    private final ClientMapper clientMapper;

    @GetMapping("/oauth2/success")
    public String oauth2Success(@AuthenticationPrincipal OAuth2User oAuth2User, RedirectAttributes redirectAttributes) {
        String socialId;
        String platform;
        
     // 1. 플랫폼 구분 및 ID 추출
        if (oAuth2User.getAttribute("sub") != null) {
            // Google
            socialId = oAuth2User.getAttribute("sub").toString();
            platform = "google";

        } else if (oAuth2User.getAttribute("id") != null) {
            // Naver
            socialId = oAuth2User.getAttribute("id").toString();
            platform = "naver";

        } else if (oAuth2User.getAttribute("kakao_account") != null) {
            // Kakao
            Map<String, Object> kakaoAccount = (Map) oAuth2User.getAttribute("kakao_account");
            socialId = oAuth2User.getAttribute("id").toString(); // 카카오 기본 ID는 최상위
            platform = "kakao";

        } else {
            throw new IllegalArgumentException("지원하지 않는 소셜 로그인입니다.");
        }

        // 2. DB에서 사용자 조회
        Client client = clientMapper.findBySocialIdAndPlatform(socialId, platform);
        System.out.println("client: " + client);
        if(client.getBirth().equals(Date.valueOf("1900-01-01")) || client.getGender().equals("N") || client.getAddrBase().isBlank() || client.getAddrDetail().isBlank()||client.getZipcode().isEmpty() ||client.getInterest().isBlank()) {
        	
        	redirectAttributes.addFlashAttribute("client", client);

        	
        	return "redirect:/client/mypageUpdate";
        }
        // 3. 세션에 사용자 저장
        if (client != null) {
            session.setAttribute("loginClient", client);
            return "redirect:/main";
        } else {
            return "redirect:/main";
        }
    }
}