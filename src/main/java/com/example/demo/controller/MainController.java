package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")  // 루트 경로
    public String root() {
        return "main";  // /WEB-INF/views/main.jsp
    }

    @GetMapping("/main")  // 명시적 경로
    public String main() {
        return "main";  // main.jsp
    }
    
    @GetMapping("/introduce")
    public String introducePage() {
        return "intro/introduce"; 
    }
    
    //푸터 약관조항
    @Controller
    public class PageController {

        @GetMapping("/terms")
        public String termsPage() {
            return "etc/terms"; 
        }

        @GetMapping("/privacy")
        public String privacyPage() {
            return "etc/privacy"; 
        }
    }
    
}
