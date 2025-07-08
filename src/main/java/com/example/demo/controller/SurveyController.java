package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Client;
import com.example.demo.model.Question;
import com.example.demo.model.Survey;
import com.example.demo.model.SurveyFeedback;
import com.example.demo.model.SurveyResult;
import com.example.demo.service.ChatGptService;
import com.example.demo.service.QuestionService;
import com.example.demo.service.SurveyFeedbackService;
import com.example.demo.service.SurveyResultService;
import com.example.demo.service.SurveyService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/survey")
public class SurveyController {

	@Autowired
	private SurveyService surveyService;

	@Autowired
	private SurveyResultService surveyResultService;

	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private ChatGptService chatGptService;
	
	@Autowired
	private SurveyFeedbackService surveyFeedbackService;
	
	

	@GetMapping("/start")
	public String startSurvey(@RequestParam("surveyId") int surveyId, Model model) {
		Survey survey = surveyService.getSurveyById(surveyId);
		List<Question> questions = questionService.getQuestionsBySurveyId(surveyId);

		model.addAttribute("survey", survey);
		model.addAttribute("questions", questions);

		return "survey/startSurvey";
	}
	
	@GetMapping("/list")
    public String listSurveys(Model model) {
        model.addAttribute("surveyList", surveyService.getAllSurveys());
        return "survey/selfSurveyList";
    }
	
	@PostMapping("/submit")
	public String submitSurvey(
	        HttpServletRequest request,
	        HttpSession session,
	        Model model,
	        RedirectAttributes redirectAttributes) {

	    int surveyId = Integer.parseInt(request.getParameter("surveyId"));
	    Client loginClient = (Client) session.getAttribute("loginClient");

	    List<Question> questions = questionService.getQuestionsBySurveyId(surveyId);
	    String surveyName = surveyService.getSurveyById(surveyId).getSurveyName();

	    int totalScore = 0;

	    // 라벨 맵핑 (0~4 -> 보기 텍스트)
	    Map<Integer, String> answerMap = Map.of(
	        0, "전혀 아니다",
	        1, "아니다",
	        2, "보통이다",
	        3, "그렇다",
	        4, "매우 그렇다"
	    );

	    // ✅ 회원이 선택한 답안 전체를 저장 (로그인 사용자용 GPT 해석용)
	    List<String> userAnswersForGpt = new ArrayList<>();

	    for (Question q : questions) {
	        String paramName = "q_" + q.getQNum(); // ex: q_1
	        String value = request.getParameter(paramName);

	        if (value != null) {
	            int score = Integer.parseInt(value);
	            String answerText = answerMap.get(score);
	            totalScore += score;

	            // 비회원은 저장 안 함
	            if (loginClient != null) {
	            	 SurveyResult surveyResult = new SurveyResult();
	                 surveyResult.setSurveyId(surveyId);
	                 surveyResult.setQNum(q.getQNum());
	                 surveyResult.setClientId(loginClient.getClientId());
	                 surveyResult.setAnswerNum(score);
	                 surveyResult.setAnswerText(answerText);
	                
	                 // DB 저장
	                surveyResultService.saveResult(surveyResult);

	                // GPT용 사용자 응답 저장
	                userAnswersForGpt.add("Q" + q.getQNum() + ": \"" + q.getQuestion() + "\" → [" + answerText + "]");
	            }
	        }
	    }

	    // ✅ 비회원 처리
	    if (loginClient == null) {
	        String feedback = chatGptService.generateFeedbackWithTotalScoreOnly(surveyId, totalScore);

	        model.addAttribute("surveyName", surveyName);
	        model.addAttribute("totalScore", totalScore);
	        model.addAttribute("feedbackText", feedback);
	        return "survey/notClientResult";
	    }

	    // ✅ 로그인 사용자 → 상세 응답 기반 GPT 해석
	    String detailedFeedback = chatGptService.generateDetailedFeedback(surveyId, surveyName, totalScore, userAnswersForGpt);
	    
	 // ✅ feedback 저장
	    SurveyFeedback surveyFeedback = new SurveyFeedback();
	    surveyFeedback.setClientId(loginClient.getClientId());
	    surveyFeedback.setSurveyId(surveyId);
	    surveyFeedback.setFeedback(detailedFeedback);
	    surveyFeedback.setScore(totalScore);
	    surveyFeedbackService.saveFeedback(surveyFeedback);

	    model.addAttribute("surveyName", surveyName);
	    model.addAttribute("totalScore", totalScore);
	    model.addAttribute("feedbackText", detailedFeedback);
	    model.addAttribute("surveyName", surveyName);
	    model.addAttribute("totalScore", totalScore);
	    model.addAttribute("feedbackText", detailedFeedback);
	    return "survey/clientResult";
	}



}
