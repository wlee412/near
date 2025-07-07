package com.example.demo.mapper;


import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.Survey;

@Mapper
public interface SurveyMapper {

	Survey getSurveyById(int surveyId);

	List<Survey> getSurveysByIds(List<Integer> ids);
	

}
