package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SurveyResultMapper {

	void saveResult(int surveyId, int qNum, String clientId, int score);

	

}
