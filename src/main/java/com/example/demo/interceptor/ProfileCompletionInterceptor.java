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
               client.getName() == null || client.getName().isEmpty()
            || client.getPhone() == null || client.getPhone().isEmpty()
            || client.getGender() == null || client.getGender().isEmpty()
            || client.getBirth() == null
            || client.getInterest() == null || client.getInterest().isEmpty();

        if (incomplete) {
            // 회원정보 수정 페이지로 리다이렉트
            response.sendRedirect("/mypage/mypageUpdate");
            return false; // ❌ 요청 중단
        }

        return true; // ✅ 통과
    }
}
