package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.ClientReservationMapper;
import com.example.demo.model.ClientReservation;

@Service
public class ClientReservationService {
	
	@Autowired
	ClientReservationMapper clientReservationMapper;
	
	public List<ClientReservation> getReservationList(String clientId) {
		return  clientReservationMapper.getReservationList(clientId);
	}

	public boolean cancelReservation(String reservationNo) {
		return clientReservationMapper.cancelReservation(reservationNo) > 0;
	}

}
