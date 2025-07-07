package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.SurveyResultMapper;

@Service
public class SurveyResultService {
	
	@Autowired
	SurveyResultMapper surveyResultMapper;
	
	
	public void saveResult(int surveyId, int qNum, String clientId, int score) {
		surveyResultMapper.saveResult(surveyId, qNum, clientId, score);
	}

}
