package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CounselorMapper;
import com.example.demo.model.CounselAvailable;
import com.example.demo.model.CounselReservation;
import com.example.demo.model.Counselor;
import com.example.demo.model.CounselorReservation;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounselorService {

	@Autowired
	private CounselorMapper counselorMapper;

	// 상담사 로그인
	public Counselor loginCounselor(Counselor loginCounselor) {
		return counselorMapper.loginCounselor(loginCounselor);
	}

	// 상담사 ID로 예약 가능 시간 조회
	public List<CounselAvailable> getAvailableTimes(String counselorId) {
		return counselorMapper.findAvailableTimesByCounselorId(counselorId);
	}

	// 오늘 예약건수
	public int getTodayReservationCount(String counselorId) {
		return counselorMapper.countTodayReservations(counselorId);
	}

	// 예약 가능 시간 저장 (LocalDateTime 문자열 직접 파싱)
	public boolean saveAvailableTimes(String counselorId, String selectedDate, List<String> selectedTimes) {
		try {
			// 1. DB에서 기존 시간 조회 (날짜별)
			List<CounselAvailable> existingList = counselorMapper.findAvailableTimesByDate(counselorId, selectedDate);
			List<String> existingTimes = existingList.stream()
					.map(av -> av.getStart().format(DateTimeFormatter.ofPattern("HH:mm"))).collect(Collectors.toList());

			// 2. 삭제 대상 = 기존 - 현재 선택
			List<String> timesToDelete = new ArrayList<>(existingTimes);
			timesToDelete.removeAll(selectedTimes);

			// 3. 추가 대상 = 현재 선택 - 기존
			List<String> timesToInsert = new ArrayList<>(selectedTimes);
			timesToInsert.removeAll(existingTimes);

			// 4. 삭제
			if (!timesToDelete.isEmpty()) {
				counselorMapper.deleteAvailableTimesByTimes(counselorId, selectedDate, timesToDelete);
			}

			// 5. 저장
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			for (String time : timesToInsert) {
				String full = selectedDate + " " + time;
				LocalDateTime start = LocalDateTime.parse(full, formatter);
				counselorMapper.insertAvailableTime(counselorId, start);
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public void deleteAvailableTimesByIds(List<Integer> counselNos) {
		if (!counselNos.isEmpty()) {
			counselorMapper.deleteAvailableTimesByIds(counselNos);
		}
	}

//	// 예약현황
//	public List<Map<String, Object>> getReservationCountByDate(String counselorId) {
//		return counselorMapper.countReservationsByDate(counselorId);
//
//	}

//	public int getReservationCount(String counselorId) {
//		return counselorMapper.getReservationCount(counselorId);
//	}

	// 예약 상세 조회
	public CounselorReservation getReservationDetail(int reservationNo) {
		return counselorMapper.findReservationByNo(reservationNo);
	}


//	// GPT 분석 메서드
	public String analyzeClient(CounselorReservation res) {
		
		// 명령 프롬프트
		    String feedback = res.getFeedback();
		    if (feedback == null || feedback.isBlank()) {
		        feedback = "설문 피드백 데이터가 없습니다.";
		    }

		    String prompt = String.format(
		        "아래는 내담자의 최근 심리설문 피드백 내용입니다.\n" +
		        "이 피드백을 바탕으로 내담자의 심리상태와 상담 방향을 10줄 요약해 주세요.\n\n" +
		        "%s",
		        feedback
		    );
		    return "[GPT 요약 예시]\n" + prompt;
	}

	// 상담사 ID로 예약 목록 조회
	public List<CounselorReservation> getReservationsByCounselor(String counselorId) {
		return counselorMapper.findReservationsByCounselor(counselorId);
	}

}
