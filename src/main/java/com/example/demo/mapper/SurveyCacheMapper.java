package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.SurveyCache;


@Mapper
public interface SurveyCacheMapper {

	SurveyCache findLatestByClientId(String clientId);

	void insertSurveyCache(SurveyCache newCache);
	
	
}
