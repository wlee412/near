package com.example.demo.service;

import com.example.demo.model.MentalHealthItem;
import com.example.demo.mapper.MentalHealthMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MentalHealthStatsService {

    private final MentalHealthMapper mapper;
    private final RestTemplate restTemplate = new RestTemplate();

    // 최신 서비스키 및 API 주소
    private final String SERVICE_KEY = "hWh3Syd%2B%2BiSx6gGoilIpTCIHKPDTfvhAjZccjvrt8cJ2jxJ1bm2fZJ79V6eePpDouFTqkiwXnQjCu8QrtFIBGQ%3D%3D";
    private final String BASE_URL = "https://apis.data.go.kr/1383000/yhis/YouthCnslCohortClsMtlService/getYouthCnslCohortClsMtlList";

    // 1. 공공 API 전체 데이터 수집 및 DB 저장
    public String getMentalHealthDataAll() {
        int pageNo = 1;
        int numOfRows = 100;
        int totalCount = 0;

        try {
            while (true) {
                URI uri = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                        .queryParam("serviceKey", SERVICE_KEY)
                        .queryParam("pageNo", pageNo)
                        .queryParam("numOfRows", numOfRows)
                        .queryParam("type", "json")
                        .build(true)
                        .toUri();

                String json = restTemplate.getForObject(uri, String.class);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readTree(json);
                JsonNode body = root.path("response").path("body");
                JsonNode items = body.path("items").path("item");

                int pageItemCount = 0;

                if (items.isArray()) {
                    for (JsonNode item : items) {
                        MentalHealthItem mh = objectMapper.treeToValue(item, MentalHealthItem.class);

                        boolean isExists = mapper.exists(
                                mh.getChtTtlNm(), mh.getChtSeNm(), mh.getChtXCn(), mh.getChtYCn()
                        );

                        if (!isExists) {
                            mapper.insert(mh);
                            totalCount++;
                            pageItemCount++;
                            Thread.sleep(100);
                        }
                    }
                }

                if (pageItemCount < numOfRows) break;
                pageNo++;
            }

            return "총 저장된 건수: " + totalCount + "건";

        } catch (Exception e) {
            e.printStackTrace();
            return "에러 발생: " + e.getMessage();
        }
    }

    // 2. 전체 원본
    public List<MentalHealthItem> selectAll() {
        return mapper.selectAll();
    }

    // 3. 미취학 ~ 고등학생
    public List<MentalHealthItem> selectYoungOnly() {
        return mapper.selectYoungOnly();
    }

    // 4. 대학생 이상
    public List<MentalHealthItem> selectOldOnly() {
        return mapper.selectOldOnly();
    }

    // 혼자가 아니에요 카드 클릭 시 호출 (하나의 차트에 통합된 그룹 값)
    public List<MentalHealthItem> getGroupedChartData() {
        List<MentalHealthItem> all = mapper.selectAll();

        Map<String, Double> grouped = new LinkedHashMap<>();

        for (MentalHealthItem item : all) {
            String level = item.getChtXCn();
            double value = 0;
            try {
                value = Double.parseDouble(item.getChtVl().replace(",", ""));
            } catch (Exception ignored) {}

            String group = classifyAgeGroup(level);
            grouped.put(group, grouped.getOrDefault(group, 0.0) + value);
        }

        return grouped.entrySet().stream()
                .map(e -> new MentalHealthItem(e.getKey(), String.valueOf(e.getValue())))
                .collect(Collectors.toList());
    }

    // 공통 그룹화 기준
    private String classifyAgeGroup(String grade) {
        if (grade == null) return "기타";
        if (grade.contains("미취학")) return "미취학";
        if (grade.contains("초등")) return "초등학생";
        if (grade.contains("중학")) return "중학생";
        if (grade.contains("고등")) return "고등학생";
        if (grade.contains("대학") || grade.contains("대")) return "대학생";
        return "청소년 아님";
    }

    private int parseToInt(String val) {
        try {
            return Integer.parseInt(val.replace(",", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
