package com.example.demo.mapper;



import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.SurveyFeedback;
import com.example.demo.model.SurveyFeedbackJoin;

@Mapper
public interface SurveyFeedbackMapper {

	void saveFeedback(SurveyFeedback surveyFeedback);

	List<SurveyFeedbackJoin> getFeedbackByClientId(String clientId);
 

}
