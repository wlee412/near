package com.example.demo.service;

import com.example.demo.mapper.YoutubeMapper; // ✅ 추가
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.springframework.beans.factory.annotation.Autowired; // ✅ 추가
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * YoutubeService
 * - YouTube Data API를 통해 키워드 기반 영상 검색
 * - videoId 목록을 반환하여 iframe에 활용할 수 있도록 함
 */
@Service
public class YoutubeService {

    // application.properties에서 youtube.api.key 값을 주입받음
    @Value("${youtube.api.key}")
    private String apiKey;

    // DB에서 추천 영상 조회를 위한 Mapper 주입
    @Autowired
    private YoutubeMapper youtubeMapper;

    /**
     * 유튜브 영상 검색 메서드
     * @param keyword 검색 키워드 (예: "스트레스 해소 음악")
     * @return videoId만 담긴 리스트 (iframe으로 삽입 가능)
     * @throws IOException API 통신 중 오류 발생 시
     */
    public List<String> searchVideoIds(String keyword) throws IOException {
        JsonFactory jsonFactory = new JacksonFactory();

        // YouTube API 클라이언트 생성
        YouTube youtube = new YouTube.Builder(
                new NetHttpTransport(),
                jsonFactory,
                request -> {}
        ).setApplicationName("mental-dashboard").build();

        // 검색 요청 구성
        YouTube.Search.List search = youtube.search().list("id,snippet");
        search.setKey(apiKey);         // API 키 설정
        search.setQ(keyword);          // 검색어 설정
        search.setMaxResults(20L);      // 영상 가져오는 최대 개수
        search.setType("video");     // 동영상만 필터링

        // 요청 실행 및 결과 반환
        SearchListResponse response = search.execute();
        List<SearchResult> results = response.getItems();

        List<String> videoIds = new ArrayList<>();

        if (results != null) {
            for (SearchResult item : results) {
                // videoId가 존재하는 경우만 추가
                if (item.getId() != null && item.getId().getVideoId() != null) {
                    videoIds.add(item.getId().getVideoId());
                }
            }
        }

        // 셔플해서 return (컨트롤러에서 자르기 위함)
        java.util.Collections.shuffle(videoIds);
        
        return videoIds;
    }

    /**
     * DB에서 기분(mood)별 추천 영상 3개 랜덤 조회
     * @param mood 사용자의 감정 상태 (예: sad, tired, happy 등)
     * @return videoId 리스트 (iframe 삽입용)
     */
    public List<String> getRecommendedVideosByMood(String mood) {
        return youtubeMapper.selectVideosByMood(mood);
    }
}
