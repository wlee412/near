package com.example.demo.controller;

import com.example.demo.service.YoutubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class YoutubeRestController {

    private final YoutubeService youtubeService;

    /**
     * GET /youtube/api/videos?keyword=기분전환
     * 유튜브 영상 ID 리스트를 JSON으로 반환
     */
    @GetMapping("/youtube/api/videos")
    public List<String> getVideos(@RequestParam("keyword") String keyword) throws IOException {
        return youtubeService.searchVideoIds(keyword);
    }

    @GetMapping("/youtube/api/recommend")
    public Map<String, Object> getRecommendationByMood(@RequestParam("mood") String mood) {
        String keyword;
        switch (mood) {
            case "depressed": keyword = "마음 안정 명상"; break;
            case "tired": keyword = "수면 유도 클래식"; break;
            case "exhausted": keyword = "자연 소리 asmr"; break;
            case "happy": keyword = "카페 노래 리스트"; break;
            case "excited": keyword = "pop list"; break;
            default: keyword = "힐링 음악"; break;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("keyword", keyword);

        try {
            List<String> allVideoIds = youtubeService.searchVideoIds(keyword);
            Collections.shuffle(allVideoIds);
            result.put("videoIds", allVideoIds.stream().limit(3).toList());
            result.put("source", "api");  // 어디서 불러온 건지 표시
        } catch (Exception e) {
            List<String> fallbackIds = youtubeService.getRecommendedVideosByMood(mood);
            result.put("videoIds", fallbackIds);
            result.put("source", "db");  // fallback임을 표시
        }

        return result;
    }

    // DB에서 기분 기반 추천 영상 가져오기
    @GetMapping("/recommend")
    public List<String> getRecommendedVideos(@RequestParam String mood) {
        return youtubeService.getRecommendedVideosByMood(mood);
    }
}
