package com.example.demo.model;

import java.time.LocalDateTime;

import org.apache.ibatis.type.Alias;

import lombok.Data;

@Data
@Alias("room")
public class Room {
    private int roomId;                // PK
    private int janusNum;              // Janus room 번호
    private String roomToken;          // UUID - NOT NULL (입장 토큰)
    private int reservationNo;     		// 예약번호
    private String counselorId;        // 상담사 ID
    private String clientId;           // 내담자 ID
    private LocalDateTime start;       // 시작시간
    private LocalDateTime end;         // 종료시간
    private String state;              // 상태(예약/진행중/완료 등)
}