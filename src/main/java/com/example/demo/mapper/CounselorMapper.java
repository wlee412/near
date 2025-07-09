package com.example.demo.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.CounselAvailable;
import com.example.demo.model.Counselor;

@Mapper
public interface CounselorMapper {

    // 상담사 로그인
    Counselor loginCounselor(Counselor loginCounselor);

    // 예약 가능 시간 저장 (하나씩 insert)
    void insertAvailableTime(@Param("counselorId") String counselorId,
                             @Param("start") LocalDateTime start);

    // 상담사 ID로 예약 가능 시간 목록 조회
    List<CounselAvailable> findAvailableTimesByCounselorId(@Param("counselorId") String counselorId);

    // 오늘 예약 수 조회
    int countTodayReservations(@Param("counselorId") String counselorId);
    
    void deleteAvailableTimesByTimes(@Param("counselorId") String counselorId,
            @Param("times") List<String> times);


	List<Map<String, Object>> countReservationsByDate(String counselorId);


}
