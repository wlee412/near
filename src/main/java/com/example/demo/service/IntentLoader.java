package com.example.demo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class IntentLoader {

    // 인텐트 이름 → 응답 구조 저장
    private final Map<String, Map<String, Object>> intentResponses = new HashMap<>();

    @PostConstruct
    public void loadIntents() throws IOException {
    	intentResponses.clear();
    	
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("classpath:intents/*.json");
        ObjectMapper mapper = new ObjectMapper();

        for (Resource resource : resources) {
            String filename = resource.getFilename();
            if (filename == null || !filename.endsWith(".json")) continue;

            String intentName = filename.replace(".json", "");
            JsonNode root = mapper.readTree(resource.getInputStream());

            JsonNode responses = root.path("responses");
            if (!responses.isArray() || responses.size() == 0) continue;

            JsonNode messages = responses.get(0).path("messages");

            for (JsonNode msg : messages) {
                int type = msg.path("type").asInt();

                // ✅ 일반 텍스트 응답
                if (type == 0) {
                    JsonNode textArray = msg.path("text");
                    if (textArray.isArray() && textArray.size() > 0) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("type", "text");
                        response.put("content", textArray.get(0).asText());
                        intentResponses.put(intentName, response);
                        System.out.println("✅ Loaded intent (text): " + intentName);
                        break;
                    }
                }

                // ✅RichContent 응답
                else if (type == 4) {
                    JsonNode rich = msg.path("payload").path("richContent");
                    if (rich.isArray() && rich.size() > 0) {
                        Map<String, Object> response = new HashMap<>();
                        response.put("type", "richContent");
                        response.put("content", mapper.convertValue(rich, Object.class)); // 구조 유지
                        intentResponses.put(intentName, response);
                        System.out.println("✅ Loaded intents: " + intentResponses.keySet());
                        break;
                    }
                }
            }
        }

        System.out.println("✅ Loaded intents: " + intentResponses.keySet());
    }

    // ✅ 응답 가져오기 (type, content 포함 Map 형태로)
    public Map<String, Object> getResponse(String intentName) {
        return intentResponses.getOrDefault(intentName, null);
    }
}
