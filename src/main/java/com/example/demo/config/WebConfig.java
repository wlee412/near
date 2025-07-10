package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.interceptor.ProfileCompletionInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ProfileCompletionInterceptor())
                .addPathPatterns("/mypage/**", "/survey/**", "/reservation/**")
                .excludePathPatterns("/mypage/mypageUpdate", "/client/logout"); // 예외도 설정 가능
    }
}
