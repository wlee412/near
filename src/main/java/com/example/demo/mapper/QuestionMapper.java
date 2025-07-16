package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.Question;

@Mapper
public interface QuestionMapper {

	List<Question> getQuestionsBySurveyId(int surveyId);


}
