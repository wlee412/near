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
                .addPathPatterns("/mypage/mypageUpdage",
                		         "/mypage/mypageDelete",
                		         "/mypage/mypagePharmFav",
                		         "/mypageProfile",
                		         "/mypage/mypageReport",
                		         "/mypage/mypageReservation",
                		         "/survey/**", 
                		         "/reservation/**") // 차단설정
                .excludePathPatterns( 
                		"/mypage/mypageUpdate", // 예외로 등록
                        "/mypage/update",       // 업데이트 요청도 예외로 등록
                        "/static/**", 
                        "/css/**", 
                        "/js/**", 
                        "/images/**"); // 예외설정
    }
}
