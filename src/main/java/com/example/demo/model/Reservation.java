package com.example.demo.model;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("reservation")
public class Reservation {
	private int reservationNo;
	private int counselNo;
	private String clientId;
	private String clientName;
	private String clientPhone;
	private String counselorId;
	private String counselorName;
	private String counselorPhone;
	private String sympCsv;
	private LocalDateTime start;
	private String state;
	private Timestamp reg_date;
	
	public Date getStartDate() {
		return Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
	}

	public String[] getSympArray() {
		return sympCsv != null ? sympCsv.split(",") : new String[0];
	}
}