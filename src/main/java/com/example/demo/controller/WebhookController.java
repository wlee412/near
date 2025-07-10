package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

	private final WebClient webClient;

	public WebhookController(@Value("${openai.api.key}") String apiKey) {
		this.webClient = WebClient.builder().baseUrl("https://api.openai.com/v1")
				.defaultHeader("Authorization", "Bearer " + apiKey).build();
	}

	@PostMapping
	public Map<String, Object> handleWebhook(@RequestBody Map<String, Object> request) {
		String userMessage = ((Map<String, Object>) request.get("queryResult")).get("queryText").toString();
		String reply = callChatGPT(userMessage);

		Map<String, Object> fulfillmentText = Map.of("text", List.of(reply));
		Map<String, Object> message = Map.of("text", fulfillmentText);
		Map<String, Object> response = Map.of("fulfillmentMessages", List.of(message));

		return response;
	}

	private String callChatGPT(String prompt) {
		try {
			Map<String, Object> requestBody = Map.of("model", "gpt-3.5-turbo", "messages", List.of(
					Map.of("role", "system", "content",
							"당신은 'n:ear'라는 이름의 따뜻하고 친절한 심리상담 챗봇입니다. "
									+ "n:ear는 심리상담 화상 플랫폼으로, 사용자가 불안, 우울, 외로움, 스트레스 등 감정을 털어놓으면 공감해주고 위로의 말을 전합니다. "
									+ "필요할 경우 상담사와의 화상상담 예약을 유도해 주세요. "
									+ "상담사 소개, 정보는 상단에 소개 프로필로 들어가시면 확인하실 수 있습니다."
									+ "기본심리검사는 무료로 제공되며 결과는 마이페이지에서 확인할 수 있습니다."
									+ "가볍게 대화를 나누거나 기분 전환이 필요한 경우 '기분 전환'이나 '게임', 같은 키워드에 반응해 적절한 제안을 해 주세요."
									+ "상담예약은 챗봇이 직접하지 않습니다. 상담예약을 말하면 상담예약 링크를 제공해요."
									+ "사용자가 '상담사 소개'나 '상담사 정보'라고 입력하면, 챗봇은 상담사에 대한 상세 설명을 직접 하지 않고, "
									+ "'상단의 소개 메뉴에서 확인하실 수 있습니다'라는 안내만 제공하세요. "
									+ "GPT는 상담사에 대한 세부 정보나 프로필을 생성하지 않습니다."),
					Map.of("role", "user", "content", prompt)));

			Map response = webClient.post().uri("/chat/completions").contentType(MediaType.APPLICATION_JSON)
					.bodyValue(requestBody).retrieve().bodyToMono(Map.class).block();

			Map message = (Map) ((Map) ((List) response.get("choices")).get(0)).get("message");
			return message.get("content").toString().trim();
		} catch (Exception e) {
			return "죄송합니다. 답변 중 오류가 발생했습니다.";
		}
	}
}
