package com.example.demo.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CounselorReservation {
	private Timestamp start;
	private String counselorId;
	private String clientId;
	private String name;
	private String birth;
	private String gender;
	private String phone;
	private String address;
	
	private String state; //예약 상태
	private int reservationNo;
	private int counselNo;
	private int reason; 
	
	private String feedback;
	private String interest;
	private String gptSummary;

}
