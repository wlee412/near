package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:secret.properties") // secret.properties를 classpath에서 로딩
public class PropertyConfig {
    // 아무 메서드 없어도 이 어노테이션만으로 설정 가능
}
