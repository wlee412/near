package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

	// ✅ 상담사 로그인
	public Counselor loginCounselor(Counselor loginCounselor) {
		return counselorMapper.loginCounselor(loginCounselor);
	}

	// ✅ 상담사 ID + 날짜로 예약 가능 시간 조회
	public List<CounselAvailable> getAvailableTimes(String counselorId, String selectedDate) {
		return counselorMapper.findAvailableTimesByCounselorId(counselorId, selectedDate);
	}

	// ✅ 오늘 예약 건수
	public int getTodayReservationCount(String counselorId) {
		return counselorMapper.countTodayReservations(counselorId);
	}

	// ✅ 예약 가능 시간 저장 (중복 제거 + 토글 방식 insert/delete)
	public boolean saveAvailableTimes(String counselorId, String selectedDate, List<String> selectedTimes) {
		try {
			// 1. DB에서 기존 시간 조회
			List<CounselAvailable> existingList = counselorMapper.findAvailableTimesByDate(counselorId, selectedDate);
			List<String> existingTimes = existingList.stream()
					.map(av -> av.getStart().format(DateTimeFormatter.ofPattern("HH:mm"))).collect(Collectors.toList());

			// 2. selectedTimes: "2025-07-14 11:00" → "11:00" 추출
			List<String> selectedOnlyTimes = selectedTimes.stream().map(t -> t.split(" ")[1].substring(0, 5)) // "14:00"
					.collect(Collectors.toList());

			// 3. 삭제 대상: 기존 - 선택된 시간
			List<String> timesToDelete = new ArrayList<>(existingTimes);
			timesToDelete.removeAll(selectedOnlyTimes);

			// 4. 추가 대상: 선택된 시간 - 기존
			List<String> timesToInsert = new ArrayList<>(selectedOnlyTimes);
			timesToInsert.removeAll(existingTimes);

			// 5. 삭제 실행
			if (!timesToDelete.isEmpty()) {
				counselorMapper.deleteAvailableTimesByTimes(counselorId, selectedDate, timesToDelete);
			}

			// 6. insert 실행
			for (String time : timesToInsert) {
				String full = selectedDate + " " + time;
				LocalDateTime start = LocalDateTime.parse(full, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
				counselorMapper.insertCounselAvailable(counselorId, start);
			}

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// ✅ 상담 예약 상세 조회
	public CounselorReservation getReservationDetail(int reservationNo) {
		return counselorMapper.findReservationByNo(reservationNo);
	}

	// ✅ GPT 프롬프트 생성
	public String analyzeClient(CounselorReservation res) {
		String feedback = res.getFeedback();
		if (feedback == null || feedback.isBlank()) {
			feedback = "설문 피드백 데이터가 없습니다.";
		}

		return String.format("아래는 내담자의 최근 심리설문 피드백 내용을 바탕으로 상담을 어떻게 진행하면 좋을지 내담자의 심리상태와 상담 방향을 10줄 요약해 주세요.\n\n%s",
				feedback);
	}

	// ✅ 예약 목록 조회
	public List<CounselorReservation> getReservationsByCounselor(String counselorId) {
		return counselorMapper.findReservationsByCounselor(counselorId);
	}

	// ✅ 페이징 + 정렬 포함 예약 목록 조회
	public List<CounselorReservation> getPagedReservations(String counselorId, int page, int size, String sortColumn,
			String sortOrder) {
		int offset = (page - 1) * size;
		Map<String, Object> param = new HashMap<>();
		param.put("counselorId", counselorId);
		param.put("offset", offset);
		param.put("size", size);
		param.put("sortColumn", sortColumn != null ? sortColumn : "a.start");
		param.put("sortOrder", sortOrder != null ? sortOrder : "DESC");

		return counselorMapper.findReservationsByCounselorWithPaging(param);
	}

	// ✅ 전체 예약 건수
	public int getTotalReservations(String counselorId) {
		return counselorMapper.countReservationsByCounselor(counselorId);
	}

	// ✅ 예약 취소 (다건)
	public boolean cancelReservationsByCounselor(List<Integer> reservationNos) {
		boolean allCanceled = true;

		for (int reservationNo : reservationNos) {
			if (!cancelReservationByCounselor(reservationNo)) {
				allCanceled = false;
			}
		}
		return allCanceled;
	}

	// ✅ 예약 취소 (단건)
	public boolean cancelReservationByCounselor(int reservationNo) {
		return counselorMapper.cancelReservationByCounselor(reservationNo) > 0;
	}

	// ✅ 페이징용 Map 처리 오버로딩
	public List<CounselorReservation> getPagedReservations(Map<String, String> param) {
		String counselorId = param.get("counselorId");
		int page = Integer.parseInt(param.getOrDefault("page", "1"));
		int size = Integer.parseInt(param.getOrDefault("size", "10"));
		String sortColumn = param.getOrDefault("sortColumn", "start");
		String sortOrder = param.getOrDefault("sortOrder", "DESC");

		return getPagedReservations(counselorId, page, size, sortColumn, sortOrder);
	}

	// ✅ 페이징 단순 호출
	public List<CounselorReservation> getReservationsByCounselorWithPaging(String counselorId, int page, int size) {
		int offset = (page - 1) * size;
		Map<String, Object> param = new HashMap<>();
		param.put("counselorId", counselorId);
		param.put("offset", offset);
		param.put("size", size);
		param.put("sortColumn", "a.start");
		param.put("sortOrder", "DESC");

		return counselorMapper.findReservationsByCounselorWithPaging(param);
	}

	// ✅ 예약 가능 시간 삭제 (ID로)
	public void deleteAvailableTimesByIds(List<Integer> counselNos) {
		if (!counselNos.isEmpty()) {
			counselorMapper.deleteAvailableTimesByIds(counselNos);
		}
	}

	public List<CounselAvailable> getAvailableTimes(String counselorId) {
		return counselorMapper.findAvailableTimesByCounselorIdAll(counselorId);
	}

	// 예약된 시간 캘린더에 표시

	public List<String> getReservedTimesByDate(String date, String counselorId) {
		return counselorMapper.findReservedTimesByDate(date, counselorId);
	}

}