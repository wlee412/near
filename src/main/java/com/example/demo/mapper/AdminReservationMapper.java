package com.example.demo.mapper;

import java.util.List;
import java.util.Map;
import com.example.demo.model.AdminReservation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminReservationMapper {
    List<AdminReservation> getReservationsWithPaging(Map<String, Object> params);
    int getTotalReservationCount();
    int cancelReservation(int reservationNo);
}
