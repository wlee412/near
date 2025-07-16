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

    private final String SERVICE_KEY = "hWh3Syd%2B%2BiSx6gGoilIpTCIHKPDTfvhAjZccjvrt8cJ2jxJ1bm2fZJ79V6eePpDouFTqkiwXnQjCu8QrtFIBGQ%3D%3D";
    private final String BASE_URL = "https://apis.data.go.kr/1383000/yhis/YouthCnslCohortClsMtlService/getYouthCnslCohortClsMtlList";

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

    public List<MentalHealthItem> selectAll() {
        return mapper.selectAll();
    }

    public List<MentalHealthItem> selectYoungOnly() {
        return mapper.selectYoungOnly();
    }

    public List<MentalHealthItem> selectOldOnly() {
        return mapper.selectOldOnly();
    }

    public List<MentalHealthItem> selectAvgByAgeGroup() {
        List<Map<String, Object>> rawList = mapper.selectAvgByAgeGroup();

        return rawList.stream()
                .map(row -> {
                    MentalHealthItem item = new MentalHealthItem();
                    item.setChtXCn(String.valueOf(row.get("chtXCn")));
                    item.setChtVl(String.valueOf(row.get("chtVl")));
                    return item;
                })
                .collect(Collectors.toList());
    }

    // 연령 그룹별 합산 결과 (chart용)
    public List<MentalHealthItem> getGroupedChartData() {
        List<MentalHealthItem> list = mapper.selectAll();

        Map<String, Double> grouped = new LinkedHashMap<>();

        for (MentalHealthItem item : list) {
            String level = item.getChtXCn();
            double value = 0.0;
            try {
                value = Double.parseDouble(item.getChtVl().replace(",", ""));
            } catch (Exception e) {
                continue;
            }

            String ageGroup = "기타";
            if (level.contains("초등")) ageGroup = "초등학생";
            else if (level.contains("중학")) ageGroup = "중학생";
            else if (level.contains("고등")) ageGroup = "고등학생";
            else if (level.contains("미취학")) ageGroup = "미취학";
            else if (level.contains("대학")) ageGroup = "대학생";
            else if (level.contains("청소년 아님")) ageGroup = "청소년 아님";

            grouped.put(ageGroup, grouped.getOrDefault(ageGroup, 0.0) + value);
        }

        List<String> order = Arrays.asList("미취학", "초등학생", "중학생", "고등학생", "대학생", "청소년 아님");

        return order.stream()
                .filter(grouped::containsKey)
                .map(ageGroup -> new MentalHealthItem(ageGroup, String.valueOf(grouped.get(ageGroup))))
                .collect(Collectors.toList());
    }
}
