package com.example.demo.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.Client;
import com.example.demo.model.PharmFavorite;
import com.example.demo.model.Survey;
import com.example.demo.model.SurveyFeedbackJoin;
import com.example.demo.service.ChatGptService;
import com.example.demo.service.ClientService;
import com.example.demo.service.PharmFavoriteService;
import com.example.demo.service.SurveyFeedbackService;
import com.example.demo.service.SurveyService;
import com.example.demo.service.VerifyService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/mypage")
public class MypageController {
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private ChatGptService chatGptService;
	
	@Autowired
	private SurveyFeedbackService surveyFeedbackService;
	
	@Autowired
	private PharmFavoriteService pharmFavoriteService;
	
	@Autowired
	private VerifyService verifiedService;

	
	@GetMapping("/mypage")
	public String mypage(HttpSession session, Model model) {
	    Client client = (Client) session.getAttribute("loginClient");
	    model.addAttribute("client", client);

	    // 관심사 유효성 체크
	    if (client == null || client.getInterest() == null || client.getInterest().isEmpty()) {
	        model.addAttribute("recommendExplanation", "관심사가 등록되지 않았습니다.");
	        return "/mypage/mypage";
	    }

	    String interestStr = client.getInterest();
	    String[] interestArr = interestStr.split(",");

	    // 설문 추천
	    List<Survey> recommendedSurveys = surveyService.getRecommendedSurveys(interestArr);
	    model.addAttribute("recommendedSurveys", recommendedSurveys);

	    // GPT 설명 캐시 로직
	    if (interestArr.length > 0 && !recommendedSurveys.isEmpty()) {
	        try {
	            String explanation = chatGptService.getOrCreateSurveyExplanation(
	                client.getClientId(), interestArr, recommendedSurveys
	            );
	            model.addAttribute("recommendExplanation", explanation);
	        } catch (Exception e) {
	            e.printStackTrace();
	            model.addAttribute("recommendExplanation", "GPT 설명 생성 실패");
	            System.out.println("GPT 오류 응답 내용: " + e.getMessage());
	        }
	    }

	    // 가입일로부터 D+ 계산
	    if (client.getRegDate() != null) {
	        LocalDate joinDate = client.getRegDate().toLocalDateTime().toLocalDate();
	        LocalDate today = LocalDate.now(ZoneId.systemDefault());
	        long dday = ChronoUnit.DAYS.between(joinDate, today);
	        model.addAttribute("dDay", dday);
	    }

	    return "mypage/mypage";
	}
	
	// 마이페이지 프로필 화면
		@GetMapping("/mypageProfile")
		public String mypageProfile(HttpSession session, Model model) {
			Client client = (Client) session.getAttribute("loginClient");
			model.addAttribute("client", client);
			return "mypage/mypageProfile"; // /WEB-INF/views/client/mypageProfile.jsp
		}
		
		// 설문조사 결과
		@GetMapping("/mypageReport")
		public String mypageReport(HttpSession session, Model model) {
		    Client client = (Client) session.getAttribute("loginClient");

		    if (client == null) {
		        return "redirect:/login"; // 비로그인 사용자는 로그인 페이지로
		    }

		    String clientId = client.getClientId();
		    List<SurveyFeedbackJoin> feedbackList = surveyFeedbackService.getFeedbackByClientId(clientId);
		    model.addAttribute("client", client);
		    model.addAttribute("feedbackList", feedbackList);
		    return "mypage/mypageReport";
		}
		
		// 약국 즐겨찾기
		@GetMapping("/mypagePharmFav")
		public String mypagePharmFav(HttpSession session, Model model) {
			Client client = (Client) session.getAttribute("loginClient");
			
			if (client == null) {
				return "redirect:/login"; // 비로그인 사용자는 로그인 페이지로
			}
			
			String clientId = client.getClientId();
			List<PharmFavorite> favoriteList = pharmFavoriteService.getPharmFavoriteList(clientId);
			model.addAttribute("client", client);
			model.addAttribute("favList", favoriteList);
			return "mypage/mypagePharmFav";
		}
		
		@PostMapping("/pharmDelete")
		@ResponseBody
	    public int pharmDelete(@RequestBody Map<String, String> body,
	                           HttpSession session) {
	        String clientId = body.get("clientId");
	        String pharmId  = body.get("pharmId");
	        return pharmFavoriteService.deletePharmFav(clientId, pharmId);
	    }

		
		@GetMapping("/update")
		public String showUpdateForm(HttpSession session, Model model) {
			Client loginClient = (Client) session.getAttribute("loginClient");

			if (loginClient == null) {
				return "redirect:/client/login"; // 로그인 안 했으면 로그인으로
			}

			model.addAttribute("client", loginClient); // JSP에 회원 정보 넘겨줌
			return "client/update"; // 수정 폼 화면 반환
		}

		// 회원 정보 수정 처리
		@PostMapping("/update")
		public String updateClient(@ModelAttribute Client client, 
				  				   @RequestParam(value = "interestList", required = false) List<String> interestList,
								   HttpSession session,
				                   RedirectAttributes redirectAttributes) {
			
			Client loginClient = (Client) session.getAttribute("loginClient");

			if (loginClient == null) {
				return "redirect:/client/login"; //로그인 안 한 경우 login폼으로 이동
			}

			//세션의 ID로 고정 (보안상 중요!)
			client.setClientId(loginClient.getClientId());
			

		    //일반 로그인 사용자는 성별 수정 불가 (기존 값 유지)
		    if (!"N".equals(loginClient.getGender())) {
		        client.setGender(loginClient.getGender());
		    }

		    //관심사 체크박스를 문자열로 병합
		    if (interestList != null && !interestList.isEmpty()) {
		        client.setInterest(String.join(",", interestList));
		    } else {
		        client.setInterest(""); // 아무것도 선택 안 한 경우
		    }


			boolean result = clientService.updateClient(client);
			
			if (result) {
				 //DB에서 최신 회원 정보 가져옴
			    Client refreshed = clientService.getClientById(client.getClientId());
			    if(refreshed.getVerified().equals("N")) {
			    	verifiedService.updateVerifiedById(refreshed.getClientId());
			    }
			    //로그로 관심사 확인
			    System.out.println("[DEBUG] 수정 후 DB에서 가져온 관심사: " + refreshed.getInterest());

			    //세션에 최신 정보 저장
			    session.setAttribute("loginClient", refreshed);

			    redirectAttributes.addFlashAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
			    return "redirect:/mypage/mypageProfile";
			} else {
				// 실패 메시지 추가
				redirectAttributes.addFlashAttribute("error", "회원 정보 수정에 실패했습니다.");

				// 수정 페이지로 리다이렉트
				return "redirect:/mypage/mypageUpdate";
			}
		}
		
		// 마이페이지 회원 정보 수정 화면
		@GetMapping("/mypageUpdate")
		public String mypageUpdate(HttpSession session, Model model) {
		    Client sessionClient = (Client) session.getAttribute("loginClient");
		    if (sessionClient == null) {
		        return "redirect:/login"; 
		    } 
		    Client client = clientService.getClientById(sessionClient.getClientId());

		    String interestStr = client.getInterest();
		    String[] interestArr = interestStr != null ? interestStr.split(",") : new String[0];
		    System.out.println("mypageUpdate - client: " + client.toString());
		    model.addAttribute("interestList", Arrays.asList(interestArr));
		    model.addAttribute("client", client);

		    return "mypage/mypageUpdate";
		}

		// 마이페이지 비밀번호 변경 화면
		@GetMapping("/mypagePassword")
		public String mypagePassword(HttpSession session, Model model) {
			Client client = (Client) session.getAttribute("loginClient");
			model.addAttribute("loginClient", client);
			return "mypage/mypagePassword";
		}

		// 마이페이지 회원 탈퇴 화면
		@GetMapping("/mypageDelete")
		public String mypageDelete(HttpSession session, Model model) {
			Client client = (Client) session.getAttribute("loginClient");
			model.addAttribute("loginClient", client);
			return "mypage/mypageDelete";
		}
		
		
}
