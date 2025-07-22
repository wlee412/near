package com.example.demo.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomOAuth2UserService customOAuth2UserService;
	private final CustomOidcUserService customOidcUserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        	.cors(Customizer.withDefaults()) 
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/client/update", "/", "/client/login", "/css/**", "/js/**", "/images/**", "/icons/**").permitAll()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
            	    .loginPage("/login")      
            	    .permitAll()
             )
            .logout(logout -> logout
                .logoutUrl("/client/logout")
                .logoutSuccessUrl("/client/login?logout")
                .permitAll()
            )
            
            .headers(headers -> headers
                    .frameOptions(frame -> frame.sameOrigin())
                )
            
            .oauth2Login(oauth2 -> oauth2
                    .loginPage("/client/login")
                    .defaultSuccessUrl("/oauth2/success", true)
                    .failureHandler((request, response, exception) -> {
                        exception.printStackTrace(); // 콘솔에 예외 출력
                        response.getWriter().write("OAuth2 인증 실패: " + exception.getMessage());
                        response.sendRedirect("/client/login?error");
                    })
                    .userInfoEndpoint(userInfo -> userInfo
                        .userService(customOAuth2UserService)
                        .oidcUserService(customOidcUserService)

                    )
                );

        return http.build();
    }

    
    @Bean
	 public HttpFirewall allowDoubleSlashFirewall() {
	        StrictHttpFirewall firewall = new StrictHttpFirewall();
	        // double slash 허용
	        firewall.setAllowUrlEncodedDoubleSlash(true); // URL 인코딩된 // 허용 (%2F%2F)
	        return firewall;
	    }
	 
	 @Bean
	    public WebSecurityCustomizer webSecurityCustomizer(HttpFirewall firewall) {
	        return web -> web.httpFirewall(firewall);
	    }
	 
	 @Bean
	 public CorsConfigurationSource corsConfigurationSource() {
	     CorsConfiguration configuration = new CorsConfiguration();
	     configuration.setAllowedOrigins(Arrays.asList("http://localhost:80", "https://js1.jsflux.co.kr/")); // 배포된 도메인
	     configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	     configuration.setAllowedHeaders(Arrays.asList("*"));
	     configuration.setAllowCredentials(true); // 세션 공유 위해 필요

	     UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	     source.registerCorsConfiguration("/**", configuration);
	     return source;
	 }
	 
}

