package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminLoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        HttpSession session = request.getSession();
        String admin = (String) session.getAttribute("admin");

        if (admin == null) {
            response.sendRedirect("/admin/adminLogin"); // 로그인 페이지로 리디렉션
            return false;
        }
        return true; // 통과
    }
}