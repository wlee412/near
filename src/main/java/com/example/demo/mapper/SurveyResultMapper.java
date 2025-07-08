package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.SurveyResult;

@Mapper
public interface SurveyResultMapper {

	void saveResult(SurveyResult result);


	

}
