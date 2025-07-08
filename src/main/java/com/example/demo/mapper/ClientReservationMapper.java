package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.ClientReservation;

@Mapper
public interface ClientReservationMapper {

	ClientReservation getReservationList();

}
