package com.example.demo.mapper;

import java.time.LocalDateTime;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.CounselReservationDTO;

@Mapper
public interface ReservationMapper {

    // 1. 예약 불가 시간 조회 (문자열로 시간 목록)
    List<String> selectUnavailableTimes(
        @Param("counselorId") String counselorId,
        @Param("date") String date
    );

    // 2. 예약 가능한 시간대의 PK (counsel_no) 조회
    Integer findCounselNoByTime(
        @Param("counselorId") String counselorId,
        @Param("start") LocalDateTime start
    );

    // 3. 중복 예약 여부 확인 (상담사/사용자 기준)
    boolean checkDuplicateReservation(CounselReservationDTO dto);

    // 4. 예약 저장
    void insertReservation(CounselReservationDTO dto);

    // 5. 증상 저장 (중간 테이블 insert)
    void insertSymptom(@Param("reservationNo") int reservationNo, @Param("sympCode") String sympCode);
    
 // 예약 가능한 시간 조회 (counsel_available 기준)
    List<String> getAvailableTimes(
        @Param("counselorId") String counselorId,
        @Param("date") String date
    );
}