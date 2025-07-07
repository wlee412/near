package com.example.demo.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.CounselAvailable;
import com.example.demo.model.Counselor;

@Mapper
public interface CounselorMapper {

    // 상담사 로그인
    Counselor findByCounselorId(@Param("counselorId") String counselorId);

    // 예약 가능 시간 등록
    void insertAvailableTime(@Param("counselorId") String counselorId,
                             @Param("dateTime") String dateTime);

    // 예약 가능 시간 삭제 (번호로 삭제)
    void deleteAvailableTimeById(int counselNo);

    // 상담사 예약 가능 시간 목록 조회
    List<CounselAvailable> findAvailableTimesByCounselorId(@Param("counselorId") String counselorId);

	void insertAvailableTimeWithDateTime(@Param("counselorId") String counselorId, @Param("start") LocalDateTime start);
}
