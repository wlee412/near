package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.SurveyMapper;
import com.example.demo.model.Survey;

@Service
public class SurveyService {
	
	@Autowired
	SurveyMapper surveyMapper;
	
	
	public Survey getSurveyById(int surveyId) {
		return surveyMapper.getSurveyById(surveyId);
	}


	public List<Survey> getRecommendedSurveys(String[] interestArr) {
		
		Map<String, List<Integer>> interestMap = new HashMap<>();

		interestMap.put("불안", Arrays.asList(1005));
		interestMap.put("자존감", Arrays.asList(1002, 1010));
		interestMap.put("수면 문제", Arrays.asList(1003, 1007));
		interestMap.put("대인관계", Arrays.asList(1004, 1009));
		interestMap.put("섭식 문제", Arrays.asList(1006));
		interestMap.put("ADHD", Arrays.asList(1001, 1008));
		interestMap.put("스트레스", Arrays.asList(1009));
		interestMap.put("자기 효능감", Arrays.asList(1010));
		
		  // 추천 설문 ID 수집
        Set<Integer> surveyIdSet = new HashSet<>(); // 중복 제거
        for (String interest : interestArr) {
            List<Integer> ids = interestMap.get(interest.trim());
            if (ids != null) {
                surveyIdSet.addAll(ids);
            }
        }

        if (surveyIdSet.isEmpty()) {
            return new ArrayList<>(); // 아무 관심사도 없을 경우 빈 리스트 반환
        }

        // 설문 ID 리스트를 Mapper로 전달하여 DB에서 가져오기
        return surveyMapper.getSurveysByIds(new ArrayList<>(surveyIdSet));
	}
	
	

}
