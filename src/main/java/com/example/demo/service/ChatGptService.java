package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // ✅
import org.springframework.stereotype.Service;

import com.example.demo.mapper.SurveyCacheMapper;
import com.example.demo.model.Survey;
import com.example.demo.model.SurveyCache;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class ChatGptService {

	private static final OkHttpClient client = new OkHttpClient();
	private static final ObjectMapper mapper = new ObjectMapper();

	@Value("${***REMOVED***}")
	private String apiKey;

	@Value("${openai.api.url}")
	private String apiUrl;

	@Autowired
	private SurveyCacheMapper surveyCacheMapper;

	public String getRecommendationExplanation(String[] interests, List<String> surveyNames, List<String> surveyDescs) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("당신은 심리상담 도우미 입니다. " 
				+ "등록된 심리상담 설문지와 설문 id는 아래와 같습니다." 
				+ "설문명	survey_id\r\n"
				+ "ASRS 간이형 (ADHD 6문항)	1001\r\n" 
				+ "Rosenberg 자존감 척도	1002\r\n" 
				+ "ISI (불면증 심각도 지수)	1003\r\n"
				+ "CES-D (우울 척도)	1004\r\n" 
				+ "BAI (벡 불안 척도)	1005\r\n" 
				+ "K-EDI (섭식장애 선별도구, 간이형)	1006\r\n"
				+ "K-PSQI (수면의 질 지수, 간이형)	1007\r\n" 
				+ "ASRS 전체형 (ADHD 18문항)	1008\r\n"
				+ "PSS (스트레스 지각 척도)	1009\r\n" 
				+ "GSE (일반 자기 효능감 척도)	1010" 
				+ "내담자의 (정신/심리)관심사는 다음과 같습니다: ");
		prompt.append(String.join(", ", interests));
		prompt.append(".\n\n");

		prompt.append("아래는 내담자에게 추천된 설문 리스트입니다:\n");
		for (int i = 0; i < surveyNames.size(); i++) {
			prompt.append("- ").append(surveyNames.get(i)).append(": ").append(surveyDescs.get(i)).append("\n");
		}
		prompt.append("\n이 내담자에게 위 설문들이 왜 유용한지 한문단으로 따듯한 말투와 존댓말로 설명해주세요.");

		OkHttpClient client = new OkHttpClient();
		ObjectMapper mapper = new ObjectMapper();

		try {
			// JSON 바디 생성
			Map<String, Object> message1 = Map.of("role", "system", "content", "당신은 내담자의 상태를 해석해주는 전문 심리 상담 도우미입니다.");
			Map<String, Object> message2 = Map.of("role", "user", "content", prompt.toString());
			Map<String, Object> requestMap = Map.of("model", "gpt-3.5-turbo", "messages", List.of(message1, message2),
					"temperature", 0.7);

			String json = mapper.writeValueAsString(requestMap);

			RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
			okhttp3.Request request = new okhttp3.Request.Builder().url(apiUrl)
					.addHeader("Authorization", "Bearer " + apiKey).post(body).build();

			try (Response response = client.newCall(request).execute()) {
				if (response.body() == null) {
					return "GPT 응답이 비어 있습니다.";
				}

				String responseBody = response.body().string();
				JsonNode jsonNode = mapper.readTree(responseBody);

				return jsonNode.get("choices").get(0).get("message").get("content").asText();
			}

		} catch (IOException e) {
			e.printStackTrace();
			return "GPT 설명 생성 중 오류가 발생했습니다.";
		}
	}

	private String normalizeInterests(String[] interests) {
		return Arrays.stream(interests).map(String::trim).sorted() // 순서 정렬해서 비교
				.collect(Collectors.joining(","));
	}

	public String getOrCreateSurveyExplanation(String clientId, String[] interestArr, List<Survey> recommendedSurveys) {
		String currentInterest = normalizeInterests(interestArr);

		// 최신 캐시 가져오기
		SurveyCache latestCache = surveyCacheMapper.findLatestByClientId(clientId);
		if (latestCache != null) {
			String cachedInterest = normalizeInterests(latestCache.getInterest().split(","));
			if (currentInterest.equals(cachedInterest)) {
				return latestCache.getExplanation(); // ✅ 재사용
			}
		}

		// ✅ GPT 호출 및 DB 저장
		List<String> names = new ArrayList<>();
		List<String> descs = new ArrayList<>();
		for (Survey s : recommendedSurveys) {
			names.add(s.getSurveyName());
			descs.add(s.getDesc());
		}

		String explanation = getRecommendationExplanation(interestArr, names, descs);

		// ✅ surveyId 모두 저장
		for (Survey survey : recommendedSurveys) {
			SurveyCache newCache = new SurveyCache();
			newCache.setClientId(clientId);
			newCache.setInterest(currentInterest); // normalize된 값 저장
			newCache.setSurveyId(survey.getSurveyId());
			newCache.setExplanation(explanation);
			surveyCacheMapper.insertSurveyCache(newCache);
		}

		return explanation;
	}

	/**
	 * 비회원 전용: 총점 기반 해석 생성
	 */
	public String generateFeedbackWithTotalScoreOnly(int surveyId, int totalScore) {
		String prompt = "당신은 심리상담 AI입니다.\n\n" 
						+ "등록된 심리상담 설문지와 설문 id는 아래와 같습니다." 
						+ "설문명	survey_id\r\n"
						+ "ASRS 간이형 (ADHD 6문항)	1001\r\n" 
						+ "Rosenberg 자존감 척도	1002\r\n" 
						+ "ISI (불면증 심각도 지수)	1003\r\n"
						+ "CES-D (우울 척도)	1004\r\n" 
						+ "BAI (벡 불안 척도)	1005\r\n" 
						+ "K-EDI (섭식장애 선별도구, 간이형)	1006\r\n"
						+ "K-PSQI (수면의 질 지수, 간이형)	1007\r\n" 
						+ "ASRS 전체형 (ADHD 18문항)	1008\r\n"
						+ "PSS (스트레스 지각 척도)	1009\r\n" 
						+ "GSE (일반 자기 효능감 척도)	1010" 
						+ "내담자가 제출한 설문과 총점은 아래와 같습니다." 
						+ "설문 ID: " + surveyId + "\n" + "응답자의 총 점수는 " + totalScore + "점입니다.\n"
						+ "내담자에게 이 점수를 바탕으로 일반적인 해석을 단정적이지 않고 따뜻한 말투와 존댓말로 해주세요.";
		return callOpenAi(prompt);
	}

	/**
	 * 회원 전용: 문항별 응답 포함된 해석 생성
	 */
	public String generateDetailedFeedback(int surveyId, String surveyName, int totalScore, List<String> userAnswers) {
		StringBuilder prompt = new StringBuilder();
		prompt.append("당신은 심리상담 전문가 AI입니다.\n\n")
			  .append("설문명: ").append(surveyName).append("\n").append("설문 ID: ")
				.append(surveyId).append("\n").append("총 점수: ").append(totalScore).append("점\n\n")
				.append("내담자는 아래와 같은 문항에 대해 다음과 같이 응답했습니다:\n");

		for (String answer : userAnswers) {
			prompt.append("- ").append(answer).append("\n");
		}

		prompt.append("\n이 정보를 기반으로 내담자의 현재 상태를 정리하고, 존댓말로 부드럽고 진정성 있는 말투와 존댓말로 조언을 포함해 해석해주세요.");

		return callOpenAi(prompt.toString());
	}

	/**
	 * 공통 GPT 호출 로직
	 */
	private String callOpenAi(String prompt) {
		try {
			Map<String, Object> message1 = Map.of("role", "system", "content", "당신은 전문적이고 부드러운 심리상담 AI입니다.	");
			Map<String, Object> message2 = Map.of("role", "user", "content", prompt);

			Map<String, Object> requestMap = Map.of("model", "gpt-3.5-turbo", "messages", List.of(message1, message2),
					"temperature", 0.7);

			String json = mapper.writeValueAsString(requestMap);

			RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
			okhttp3.Request request = new okhttp3.Request.Builder().url(apiUrl)
					.addHeader("Authorization", "Bearer " + apiKey).post(body).build();

			try (Response response = client.newCall(request).execute()) {
				if (response.body() == null)
					return "GPT 응답이 비어 있습니다.";

				String responseBody = response.body().string();
				JsonNode jsonNode = mapper.readTree(responseBody);
				return jsonNode.get("choices").get(0).get("message").get("content").asText();
			}

		} catch (IOException e) {
			e.printStackTrace();
			return "GPT 해석을 생성하는 중 오류가 발생했습니다.";
		}
	}
}
