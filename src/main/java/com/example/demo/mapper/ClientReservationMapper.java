package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.ClientReservation;

@Mapper
public interface ClientReservationMapper {
	public List<ClientReservation> getReservationList(String clientId);

	public int cancelReservation(String reservationNo);

}
