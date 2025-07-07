package com.example.demo.controller;

import java.util.List;

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
import com.example.demo.service.QuestionService;
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

	@GetMapping("/start")
	public String startSurvey(@RequestParam("surveyId") int surveyId, Model model) {
		Survey survey = surveyService.getSurveyById(surveyId);
		List<Question> questions = questionService.getQuestionsBySurveyId(surveyId);

		model.addAttribute("survey", survey);
		model.addAttribute("questions", questions);

		return "survey/startSurvey";
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

	    // 비회원일 경우 점수만 계산
	    if (loginClient == null) {
	        int totalScore = 0;

	        for (Question q : questions) {
	            String paramName = "q_" + q.getQNum();
	            String value = request.getParameter(paramName);

	            if (value != null) {
	                totalScore += Integer.parseInt(value);
	            }
	        }

	        model.addAttribute("score", totalScore);
	        return "survey/nonMemberResult"; 
	    }

	    // 회원일 경우: DB에 설문 결과 저장
	    for (Question q : questions) {
	        String paramName = "q_" + q.getQNum();
	        String value = request.getParameter(paramName);

	        if (value != null) {
	            int score = Integer.parseInt(value);
	            surveyResultService.saveResult(surveyId, q.getQNum(), loginClient.getClientId(), score);
	        }
	    }

	    redirectAttributes.addFlashAttribute("message", "설문이 성공적으로 제출되었습니다.");
	    return "redirect:/client/mypage";
	}


}
