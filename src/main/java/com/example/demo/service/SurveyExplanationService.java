package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyExplanationService {

//    @Autowired
//    private SurveyExplanationCacheRepository cacheRepo;
//
//    @Autowired
//    private ChatGptService chatGptService;
//
//    public String getOrGenerateExplanation(String clientId, String interest, Long surveyId, String surveyTitle, String surveyDesc) {
//        // 1. 캐시 확인
//        SurveyExplanationCache cached = cacheRepo
//            .findByClientIdAndInterestAndSurveyId(clientId, interest, surveyId);
//        if (cached != null) {
//            return cached.getExplanation();
//        }
//
//        // 2. ChatGPT로 설명 생성
//        String explanation = chatGptService.getExplanation(interest, surveyTitle, surveyDesc);
//
//        // 3. DB에 저장
//        SurveyExplanationCache newEntry = new SurveyExplanationCache();
//        newEntry.setClientId(clientId);
//        newEntry.setInterest(interest);
//        newEntry.setSurveyId(surveyId);
//        newEntry.setExplanation(explanation);
//        cacheRepo.save(newEntry);
//
//        return explanation;
//    }
}

