package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public Counselor login(Counselor input) {
        Counselor found = counselorMapper.findByCounselorId(input.getCounselorId());

        if (found != null && found.getPassword().equals(input.getPassword())) {
            System.out.println("✅ 로그인 성공: " + found.getName());
            return found;
        } else {
            System.out.println("❌ 로그인 실패 - 아이디 또는 비밀번호 불일치");
            return null;
        }
    }

    // 상담사 ID로 예약 가능 시간 조회
    public List<CounselAvailable> getAvailableTimes(String counselorId) {
        return counselorMapper.findAvailableTimesByCounselorId(counselorId);
    }

    // 상담사 예약 가능 시간 저장
    public boolean saveAvailableTimes(String counselorId, String selectedDate, List<String> selectedTimes) {
        try {
            for (String time : selectedTimes) {
                String dateTime = selectedDate + " " + time;  // 예: "2025-07-08 09:00"
                counselorMapper.insertAvailableTime(counselorId, dateTime);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveAvailableTimes(String counselorId, List<String> selectedTimes) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            for (String timeStr : selectedTimes) {
                LocalDateTime start = LocalDateTime.parse(timeStr, formatter);
                counselorMapper.insertAvailableTimeWithDateTime(counselorId, start);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
