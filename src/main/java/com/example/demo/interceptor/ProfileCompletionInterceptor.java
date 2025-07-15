package com.example.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.demo.model.Client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
@Component
public class ProfileCompletionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
    	System.out.println("인터셉터 작동함: " + request.getRequestURI());
        HttpSession session = request.getSession();
        Client client = (Client) session.getAttribute("loginClient");

        // 1. 로그인 여부 체크
        if (client == null) {
            response.sendRedirect("/client/login"); // 로그인 페이지로 이동
            return false;
        }
        
        // 2. 회원 정보 누락 여부 체크
        boolean incomplete = 
        	       isNullOrEmpty(client.getName())
        	       || isNullOrEmpty(client.getPhone())
        	       || isNullOrEmpty(client.getGender())
        	       || client.getBirth() == null
        	       || isNullOrEmpty(client.getInterest())
        	       || isNullOrEmpty(client.getAddrBase())
        	       || isNullOrEmpty(client.getAddrDetail())
        	       || isNullOrEmpty(client.getZipcode());
        	
        System.out.println("client.getName(): " + client.getName());
        System.out.println("client.getPhone(): " + client.getPhone());
        System.out.println("client.getGender(): " + client.getGender());
        System.out.println("client.getBirth(): " + client.getBirth());
        System.out.println("client.getInterest(): " + client.getInterest());
        System.out.println("client.getAddrBase(): " + client.getAddrBase());
        System.out.println("client.getAddrDetail(): " + client.getAddrDetail());
        System.out.println("client.getZipcode(): " + client.getZipcode());
        
        if (incomplete) {
            // 회원정보 수정 페이지로 리다이렉트
            response.sendRedirect("/mypage/mypageUpdate?alert=needUpdate");
            return false; // ❌ 요청 중단
        }

        return true; // ✅ 통과
    }
    
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
