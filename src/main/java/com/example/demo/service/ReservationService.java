package com.example.demo.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.ReservationMapper;
import com.example.demo.model.CounselReservationDTO;

@Service
public class ReservationService {

    @Autowired
    private ReservationMapper reservationMapper;

    // ✅ 1. 예약 불가 시간 조회 (이미 예약됨)
    public List<String> getUnavailableTimes(String counselorId, String date) {
        return reservationMapper.selectUnavailableTimes(counselorId, date);
    }

    // ✅ 2. 예약 가능한 시간 조회 (counsel_available 기준)
    public List<String> getAvailableTimes(String counselorId, String date) {
        return reservationMapper.getAvailableTimes(counselorId, date);
    }

    // ✅ 3. 선택된 시간에 해당하는 counsel_no 조회
    public Integer findCounselNo(String counselorId, LocalDateTime start) {
        return reservationMapper.findCounselNoByTime(counselorId, start);
    }

    // ✅ 4. 예약 DTO 생성
    public CounselReservationDTO createReservationDTO(
        String clientId, String counselorId, String date, String time, List<String> sympCode
    ) {
        LocalDateTime start = LocalDateTime.parse(date + "T" + time);
        Integer counselNo = reservationMapper.findCounselNoByTime(counselorId, start);
        if (counselNo == null) throw new RuntimeException("예약 가능한 시간이 아닙니다.");

        CounselReservationDTO dto = new CounselReservationDTO();
        dto.setClientId(clientId);
        dto.setCounselNo(counselNo);
        dto.setStart(start);
        dto.setSympCode(sympCode);
        dto.setState("예약");
        dto.setRegDate(Timestamp.valueOf(LocalDateTime.now()));
        return dto;
    }

    // ✅ 5. 예약 저장
    public boolean saveReservation(CounselReservationDTO dto) {
        boolean exists = reservationMapper.checkDuplicateReservation(dto);
        if (exists) throw new RuntimeException("이미 예약된 시간입니다.");

        reservationMapper.insertReservation(dto);

        if (dto.getSympCode() != null) {
            for (String code : dto.getSympCode()) {
                reservationMapper.insertSymptom(dto.getReservationNo(), code);
            }
        }
        return true;
    }
}