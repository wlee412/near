package com.example.demo.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ClientReservation {
	private int reservationNo;
	private String counselorName;
	private String clientId;
	private Timestamp startTime;
	private String state;
	private Timestamp regDate;
}
