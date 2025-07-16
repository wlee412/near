package com.example.demo.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CounselReservation {
    private int reservationNo;
    private int counselNo;
    private String clientId;
    private String state;
    private Timestamp regDate;

}

