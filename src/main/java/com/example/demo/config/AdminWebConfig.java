package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class AdminWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminLoginInterceptor())
                .addPathPatterns("/admin/**") // 검사할 경로
                .excludePathPatterns(
                        "/admin/adminLogin",  // 로그인 폼
                        "/admin/login",       // 로그인 요청 처리
                        "/css/**", "/js/**", "/images/**" // 정적 리소스
                );
    }
}