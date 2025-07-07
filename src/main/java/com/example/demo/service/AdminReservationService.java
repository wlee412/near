package com.example.demo.service;

import com.example.demo.mapper.AdminReservationMapper;
import com.example.demo.model.AdminReservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminReservationService {

    private final AdminReservationMapper reservationMapper;

    public List<AdminReservation> getReservations(int page, int pageSize) {
        int startRow = (page - 1) * pageSize;

        Map<String, Object> params = new HashMap<>();
        params.put("startRow", startRow);
        params.put("pageSize", pageSize);

        return reservationMapper.getReservationsWithPaging(params);
    }

    public int getTotalCount() {
        return reservationMapper.getTotalReservationCount();
    }

    public boolean cancelReservation(int reservationNo) {
        return reservationMapper.cancelReservation(reservationNo) > 0;
    }
}
