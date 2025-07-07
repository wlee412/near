package com.example.demo.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CounselAvailable {
    private int counselNo;   
    private String counselorId;  
    private LocalDateTime start;
		


}