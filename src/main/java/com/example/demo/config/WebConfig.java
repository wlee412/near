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
	                .addPathPatterns(
	                		         "/mypage/mypageDelete",
	                		         "/mypage/mypagePharmFav",
	                		         "/mypageProfile",
	                		         "/mypage/mypageReport",
	                		         "/mypage/mypageReservation",
	                		         "/survey/**", 
	                		         "/reservation/**"
	                		         ) // 차단설정
	                .excludePathPatterns(
	                		"/mental/**",
	                		"/hospitalMap/**",
	                		"/survey/**",
	                		"/mypage/mypageUpdate", 
	                        "/mypage/update", 
	                        "mypage/mypageCancelReservation",
	                        "/static/**", 
	                        "/css/**", 
	                        "/js/**", 
	                        "/images/**"); // 예외설정
	    }
}
