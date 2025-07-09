package com.example.demo.mapper;

import com.example.demo.model.AdminReservation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminReservationMapper {

    List<AdminReservation> getReservations(
        @Param("startRow") int startRow,
        @Param("pageSize") int pageSize,
        @Param("type") String type,
        @Param("keyword") String keyword
    );

    int getTotalCount(
        @Param("type") String type,
        @Param("keyword") String keyword
    );

    int cancelReservation(@Param("reservationNo") int reservationNo);
}
