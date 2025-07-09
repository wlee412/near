package com.example.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CounselorMapper;
import com.example.demo.model.CounselAvailable;
import com.example.demo.model.Counselor;

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

    //오늘 예약건수 
	public int getTodayReservationCount(String counselorId) {
		  return counselorMapper.countTodayReservations(counselorId);
	}

	// 예약 가능 시간 저장 (LocalDateTime 문자열 직접 파싱)
	public boolean saveAvailableTimes(String counselorId, String selectedDate, List<String> selectedTimes) {
	     try {
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	            for (String timeStr : selectedTimes) {
	                LocalDateTime start = LocalDateTime.parse(timeStr, formatter);
	                counselorMapper.insertAvailableTime(counselorId, start);
	            }
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return false;
	        }
	    }

	public void deleteAvailableTimesByTimes(String counselorId, List<String> timesToDelete) {
	    counselorMapper.deleteAvailableTimesByTimes(counselorId, timesToDelete);  
	}

	//예약현황
	public List<Map<String, Object>> getReservationCountByDate(String counselorId) {
		return counselorMapper.countReservationsByDate(counselorId);

	}
}
