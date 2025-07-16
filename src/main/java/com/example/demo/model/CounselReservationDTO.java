package com.example.demo.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CounselReservationDTO {
	  private int reservationNo;
	    private String clientId;
	    private int counselNo;
	    private String state;
	    private Timestamp regDate;
	    
	    // 👇 join된 start (예약시간)
	    private LocalDateTime start;

	    // 🧠 예약 사유와 증상 코드도 추가 가능
	    private List<String> sympCode;      // 증상 코드 (symptom 테이블)
	
}
