package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.SurveyFeedbackMapper;
import com.example.demo.model.SurveyFeedback;
import com.example.demo.model.SurveyFeedbackJoin;

@Service
public class SurveyFeedbackService {
	
	@Autowired
	SurveyFeedbackMapper surveyFeedbackMapper; 
	
	public void saveFeedback(SurveyFeedback surveyFeedback) {
		surveyFeedbackMapper.saveFeedback(surveyFeedback);
	}

	public List<SurveyFeedbackJoin> getFeedbackByClientId(String clientId) {
		return surveyFeedbackMapper.getFeedbackByClientId(clientId);
	}

}
