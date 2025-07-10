package com.example.demo.model;

import java.security.Timestamp;
import java.time.LocalDateTime;

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

	public String[] getSymp() {
		return sympCsv != null ? sympCsv.split(",") : new String[0];
	}
}