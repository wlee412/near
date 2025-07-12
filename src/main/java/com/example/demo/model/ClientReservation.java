package com.example.demo.model;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class ClientReservation {
	  private int       reservationNo;
	  private String    state;
	  private Timestamp startTime;
	  private String    name;
	  private String    counselorPhone;
	  private String    symptom;
}
