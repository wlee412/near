package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.mapper.CounselorMapper;
import com.example.demo.model.CounselAvailable;
import com.example.demo.model.Counselor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CounselorService {

    private final CounselorMapper counselorMapper;

    // 상담사 로그인
    public Counselor login(Counselor loginCounselor) {
        Counselor found = counselorMapper.findByCounselorId(loginCounselor.getCounselorId());
        if (found != null && found.getPassword().equals(loginCounselor.getPassword())) {
            return found;
        }
        return null;
    }

    // 상담 가능 시간 등록
    public void insertAvailableTime(CounselAvailable available) {
        counselorMapper.insertAvailableTime(available);
    }

    // 상담사 ID로 가능 시간 목록 조회
    public List<CounselAvailable> getAvailableTimesByCounselor(String counselorId) {
        return counselorMapper.selectAvailableTimesByCounselor(counselorId);
    }
}
