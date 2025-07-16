package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.QuestionMapper;
import com.example.demo.model.Question;

@Service
public class QuestionService {
	
	@Autowired
	QuestionMapper questionMapper;
	
	public List<Question> getQuestionsBySurveyId(int surveyId) {
		return questionMapper.getQuestionsBySurveyId(surveyId);
	}

}
