package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.ClientReservationMapper;
import com.example.demo.model.ClientReservation;

@Service
public class ClientReservationService {
	
	@Autowired
	ClientReservationMapper clientReservationMapper;
	
	public ClientReservation getReservationList() {
		return  clientReservationMapper.getReservationList();
	}

}
