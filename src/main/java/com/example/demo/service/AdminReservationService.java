package com.example.demo.service;

import com.example.demo.mapper.AdminReservationMapper;
import com.example.demo.model.AdminReservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReservationService {

    private final AdminReservationMapper reservationMapper;

    public List<AdminReservation> getReservations(int page, int pageSize, String type, String keyword) {
        int startRow = (page - 1) * pageSize;
        return reservationMapper.getReservations(startRow, pageSize, type, keyword);
    }

    public int getTotalCount(String type, String keyword) {
        return reservationMapper.getTotalCount(type, keyword);
    }

    public boolean cancelReservation(int reservationNo) {
        return reservationMapper.cancelReservation(reservationNo) > 0;
    }
}