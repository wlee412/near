package com.example.demo.config; // ← 패키지 경로는 프로젝트에 맞게!

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@PropertySource("classpath:secret.properties")
public class WebCounselorConfig {
	 
    @Bean
    public WebClient webClient(@Value("${openai.api.key}") String apiKey) {
        return WebClient.builder()
            .baseUrl("https://api.openai.com/v1")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
            .build();
    }

    @Value("${dialogflow.agent.id}")
    private String agentId;
    
    public String getAgentId(){
    	return agentId;
    }

    
    @Value("${openai.api.key}")
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }
    
}
