package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.SurveyResultMapper;
import com.example.demo.model.SurveyResult;

@Service
public class SurveyResultService {
	
	@Autowired
	SurveyResultMapper surveyResultMapper;

	public void saveResult(SurveyResult result) {
	    surveyResultMapper.saveResult(result);
	}
	
	

}
