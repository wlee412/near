package com.example.demo.mapper;

import java.sql.Timestamp;
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

    // 특정 날짜의 예약 가능 시간 조회
    List<CounselAvailable> findAvailableTimesByCounselorId(
        @Param("counselorId") String counselorId,
        @Param("selectedDate") String selectedDate);

    // 전체 예약 가능 시간 조회
    List<CounselAvailable> findAvailableTimesByCounselorIdAll(
        @Param("counselorId") String counselorId);

    // 날짜별 예약 가능 시간 조회
    List<CounselAvailable> findAvailableTimesByDate(
        @Param("counselorId") String counselorId,
        @Param("selectedDate") String selectedDate);

    // 선택 해제된 시간 삭제
    void deleteAvailableTimesByTimes(
        @Param("counselorId") String counselorId,
        @Param("selectedDate") String selectedDate,
        @Param("times") List<String> times);

    // 개별 시간 삭제 (by PK)
    void deleteAvailableTimesByIds(@Param("counselNos") List<Integer> counselNos);

    // 예약 가능 시간 저장
    void insertCounselAvailable(
        @Param("counselorId") String counselorId,
        @Param("start") LocalDateTime start);

    // 오늘 상담 예약 수
    int countTodayReservations(@Param("counselorId") String counselorId);

    // 예약 상세 조회
    CounselorReservation findReservationByNo(@Param("reservationNo") int reservationNo);

    // 예약 조회
    List<CounselorReservation> findReservationsByDate(
        @Param("date") String date,
        @Param("counselorId") String counselorId);

    List<CounselorReservation> findReservationsByCounselor(String counselorId);

    int countReservationsByCounselor(String counselorId);

    int cancelReservationByCounselor(int reservationNo);

    List<CounselorReservation> findReservationsByCounselorWithPaging(Map<String, Object> param);
}