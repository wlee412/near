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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MentalHealthStatsService {

    private final MentalHealthMapper mapper;
    private final RestTemplate restTemplate = new RestTemplate();

    // 최신 서비스키 및 API 주소
    private final String SERVICE_KEY = "hWh3Syd%2B%2BiSx6gGoilIpTCIHKPDTfvhAjZccjvrt8cJ2jxJ1bm2fZJ79V6eePpDouFTqkiwXnQjCu8QrtFIBGQ%3D%3D";
    private final String BASE_URL = "https://apis.data.go.kr/1383000/yhis/YouthCnslCohortClsMtlService/getYouthCnslCohortClsMtlList";

    public String getMentalHealthDataAll() {
        int pageNo = 1;
        int numOfRows = 100;  // 한 페이지당 최대 100개 권장
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
//                        mapper.insert(mh);
//                        totalCount++;
//                        pageItemCount++;
                    	
                        // 중복 여부 확인
                        boolean isExists = mapper.exists(mh.getChtTtlNm(), mh.getChtSeNm(), mh.getChtXCn(), mh.getChtYCn());

                        if (!isExists) {
                            mapper.insert(mh);
                            totalCount++;
                            
                            Thread.sleep(100);   // 0.1초 동안 블락상태
                        }                    	
                    	
                    }
                }

                // 마지막 페이지면 종료
                if (pageItemCount < numOfRows) {
                    break;
                }

                pageNo++;
            }

            return "총 저장된 건수: " + totalCount + "건";

        } catch (Exception e) {
            e.printStackTrace();
            return "에러 발생: " + e.getMessage();
        }
    }

    	//데이터 받기
    public List<MentalHealthItem> selectAll() {
        return mapper.selectAll();  // MyBatis 매퍼에서 정의한 쿼리 호출
    }
    
    public List<MentalHealthItem> selectYoungOnly() {
        return mapper.selectYoungOnly();
    }

	public List<MentalHealthItem> selectOldOnly() {
		return mapper.selectOldOnly();
	}
	// 연령대 + 대학생 + 청소년아님 평균 조회
	public List<Map<String, Object>> selectAvgByAgeGroup() {
	    return mapper.selectAvgByAgeGroup();
	}


    
}
