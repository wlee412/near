package com.example.demo.model;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

// 예약과 내담자, 상담사 모든 정보
// v_reservation_summary 뷰에 대한 DTO

@Getter
@Setter
@Alias("reservation")
public class Reservation {
	private int reservationNo;
	private int counselNo;
	private String clientId;
	private String clientName;
	private String clientPhone;
	private String clientEmail;
	private String counselorId;
	private String counselorName;
	private String counselorPhone;
	private String counselorEmail;
	private String sympCsv;		// , 구분으로 증상 열거
	private LocalDateTime start;
	private String state;
	private Timestamp reg_date;
	private String roomToken;
	
	// Date형 - 포맷팅 가능
	public Date getStartDate() {
		return Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
	}

	// 배열형 - 증상 체크박스
	public String[] getSympArray() {
		return sympCsv != null ? sympCsv.split(",") : new String[0];
	}
}