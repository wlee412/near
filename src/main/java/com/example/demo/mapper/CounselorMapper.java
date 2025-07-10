package com.example.demo.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.CounselAvailable;
import com.example.demo.model.Counselor;
import com.example.demo.model.CounselorReservation;

@Mapper
public interface CounselorMapper {

    // 상담사 로그인
    Counselor loginCounselor(Counselor loginCounselor);


    // 상담사 ID로 예약 가능 시간 목록 조회
    List<CounselAvailable> findAvailableTimesByCounselorId(@Param("counselorId") String counselorId);

    // 오늘 예약 수 조회
    int countTodayReservations(@Param("counselorId") String counselorId);
    
    // 예약 가능 시간 저장 (하나씩 insert)
    void insertAvailableTime(@Param("counselorId") String counselorId,
    		@Param("start") LocalDateTime start);

//	List<Map<String, Object>> countReservationsByDate(@Param("counselorId") String counselorId);


//	void deleteAvailableTimesByDate(@Param("counselorId") String counselorId, @Param("selectedDate") String selectedDate);


	// 날짜별 예약 가능 시간 조회
	List<CounselAvailable> findAvailableTimesByDate(@Param("counselorId") String counselorId,
	                                                 @Param("selectedDate") String selectedDate);
	// 시간 삭제 (시간만 받음)
	void deleteAvailableTimesByIds(@Param("counselNos") List<Integer> counselNos);

	void deleteAvailableTimesByTimes(
			@Param("counselorId") String counselorId,
		    @Param("selectedDate") String selectedDate,
		    @Param("times") List<String> times
		);

//	int getReservationCount(String counselorId);
	
	  // 예약 번호로 예약 상세 조회
    CounselorReservation findReservationByNo(@Param("reservationNo") int reservationNo);

    // 특정 날짜의 상담사 예약 목록 조회
    List<CounselorReservation> findReservationsByDate(
        @Param("date") String date,
        @Param("counselorId") String counselorId);

	List<CounselorReservation> findReservationsByCounselor(String counselorId);

}
