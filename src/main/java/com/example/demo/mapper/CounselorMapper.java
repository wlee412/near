package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.CounselAvailable;
import com.example.demo.model.Counselor;

@Mapper
public interface CounselorMapper {

    // 상담사 로그인
    Counselor findByCounselorId(@Param("counselorId") String counselorId);

    // 상담 가능 시간 등록
    void insertAvailableTime(CounselAvailable available);

    // 상담사별 등록된 가능 시간 조회
    List<CounselAvailable> selectAvailableTimesByCounselor(@Param("counselorId") String counselorId);
}
