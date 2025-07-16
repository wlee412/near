package com.example.demo.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

public class EmailTemplate {
	private final String emailTpl; // email 템플릿 html 문자열

	public EmailTemplate(String fileName) throws IOException {
		ClassPathResource resource = new ClassPathResource("static/mail/" + fileName); // 예: "template.html"
		emailTpl = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
	}

	// Map<"변수", "내용">
	public String loadEmailTemplate(Map<String, String> variables) {
		String content = emailTpl;
		for (Map.Entry<String, String> entry : variables.entrySet()) {
			content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
		}

		return content;
	}
}
