package com.example.demo.model;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class AdminReservation {
    private int reservationNo;
    private String counselorName;
    private String clientName;
    private String clientId;
    private String phone;
    private Timestamp start;
    private String state;
}
