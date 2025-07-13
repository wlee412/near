package com.example.demo.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CounselorMapper;
import com.example.demo.model.CounselAvailable;
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
//	public boolean saveAvailableTimes(String counselorId, String selectedDate, List<String> selectedTimes) {
//		try {
//			// 1. DB에서 기존 시간 조회 (날짜별)
//			List<CounselAvailable> existingList = counselorMapper.findAvailableTimesByDate(counselorId, selectedDate);
//			List<String> existingTimes = existingList.stream()
//					.map(av -> av.getStart().format(DateTimeFormatter.ofPattern("HH:mm"))).collect(Collectors.toList());
//
//			// 2. 삭제 대상 = 기존 - 현재 선택
//			List<String> timesToDelete = new ArrayList<>(existingTimes);
//			timesToDelete.removeAll(selectedTimes);
//
//			// 3. 추가 대상 = 현재 선택 - 기존
//			List<String> timesToInsert = new ArrayList<>(selectedTimes);
//			timesToInsert.removeAll(existingTimes);
//
//			// 4. 삭제
//			if (!timesToDelete.isEmpty()) {
//				counselorMapper.deleteAvailableTimesByTimes(counselorId, selectedDate, timesToDelete);
//			}
//
//			// 5. 저장
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//			for (String time : timesToInsert) {
////				String full = selectedDate + " " + time;
//				LocalDateTime start = LocalDateTime.parse(time, formatter); // LocalDateTime으로 파싱
////				Timestamp timestampStart = Timestamp.valueOf(start);
//			
//				counselorMapper.insertAvailableTime(counselorId, start);
//			}
//
//			return true;
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}

//	 // 예약 가능 시간 저장
//    public boolean saveAvailableTimes(String counselorId, String selectedDate, List<String> selectedTimes) {
//        try {
//            // 날짜와 시간을 합쳐서 LocalDateTime으로 변환
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//            for (String time : selectedTimes) {
//                String full = selectedDate + " " + time;
//                LocalDateTime start = LocalDateTime.parse(full, formatter);
//                Timestamp timestampStart = Timestamp.valueOf(start);
//
//                // DB에 시간 삽입
//                counselorMapper.insertAvailableTime(counselorId, timestampStart);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }


	public void deleteAvailableTimesByIds(List<Integer> counselNos) {
		if (!counselNos.isEmpty()) {
			counselorMapper.deleteAvailableTimesByIds(counselNos);
		}
	}


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
		        "아래는 내담자의 최근 심리설문 피드백 내용을 바탕으로 상담을 어떻게 진행하면 좋을지 내담자의 심리상태와 상담 방향을 10줄 요약해 주세요.\n\n" +
		        "%s",
		        feedback
		    );
		    return prompt;
	}

	// 상담사 ID로 예약 목록 조회
	public List<CounselorReservation> getReservationsByCounselor(String counselorId) {
		return counselorMapper.findReservationsByCounselor(counselorId);
	}
	

	public List<CounselorReservation> getPagedReservations(String counselorId, int page, int size, String sortColumn, String sortOrder) {
	    int offset = (page - 1) * size;
	    Map<String, Object> param = new HashMap<>();
	    param.put("counselorId", counselorId);
	    param.put("offset", offset);
	    param.put("size", size);
	    param.put("sortColumn", sortColumn != null ? sortColumn : "a.start");
	    param.put("sortOrder", sortOrder != null ? sortOrder : "DESC");

	    return counselorMapper.findReservationsByCounselorWithPaging(param);
	}

	public int getTotalReservations(String counselorId) {
		return counselorMapper.countReservationsByCounselor(counselorId);
	}

	//예약취소
	public boolean cancelReservationsByCounselor(List<Integer> reservationNos) {
	    boolean allCanceled = true;

	    for (int reservationNo : reservationNos) {
	        // 각 예약 번호를 처리
	        boolean success = cancelReservationByCounselor(reservationNo);
	        if (!success) {
	            allCanceled = false; // 하나라도 실패하면 false
	        }
	    }

	    return allCanceled; // 모든 예약이 취소되었으면 true 반환
	}


	// 단일 예약 취소 처리
	public boolean cancelReservationByCounselor(int reservationNo) {
	    // 예약 번호로 상태를 변경
	    return counselorMapper.cancelReservationByCounselor(reservationNo) > 0;
	}


	//페이징
	
	public List<CounselorReservation> getPagedReservations(Map<String, String> param) {
	    String counselorId = param.get("counselorId");
	    int page = Integer.parseInt(param.getOrDefault("page", "1"));
	    int size = Integer.parseInt(param.getOrDefault("size", "10"));
	    String sortColumn = param.getOrDefault("sortColumn", "start");
	    String sortOrder = param.getOrDefault("sortOrder", "DESC");

	    return getPagedReservations(counselorId, page, size, sortColumn, sortOrder);  // 위에 있는 실제 구현 호출
	}

	public List<CounselorReservation> getReservationsByCounselorWithPaging(String counselorId, int page, int size) {
	    int offset = (page - 1) * size;
	    Map<String, Object> param = new HashMap<>();
	    param.put("counselorId", counselorId);
	    param.put("offset", offset);
	    param.put("size", size);
	    param.put("sortColumn", "a.start"); // 기본 정렬 컬럼
	    param.put("sortOrder", "DESC");     // 기본 정렬 방향

	    return counselorMapper.findReservationsByCounselorWithPaging(param);
	}
	// 예약 가능 시간 저장 (중복 제거)
	public boolean saveAvailableTimes(String counselorId, String selectedDate, List<String> selectedTimes) {
	    for (String time : selectedTimes) {
	        // 날짜와 시간 결합
	        String formattedTime = selectedDate + " " + time;  // "yyyy-MM-dd HH" 형식으로 변환
	        
	        try {
	            // 시간 형식에 문제가 있는 경우, :00:00을 추가하여 시간 문자열을 완전하게 만듬
	            if (formattedTime.length() == 16) {  // "yyyy-MM-dd HH" 형식일 경우
	                formattedTime += ":00:00";  // :00:00을 추가하여 "yyyy-MM-dd HH:mm:ss" 형식으로 변환
	            }

	            // 변환된 시간 로그 출력
	            System.out.println("Formatted time: " + formattedTime);  // 디버깅 로그

	            // Timestamp 변환
	            Timestamp timestamp = Timestamp.valueOf(formattedTime);  // Timestamp 형식으로 변환

	            // DB에 저장
	            counselorMapper.insertCounselAvailable(counselorId, timestamp);  // 실제로 insert 수행

	            System.out.println("Inserted into DB: " + counselorId + " at " + timestamp);  // 디버깅 로그
	        } catch (Exception e) {
	            // 예외 처리
	            e.printStackTrace();
	            return false;
	        }
	    }
	    return true;
	}

}
