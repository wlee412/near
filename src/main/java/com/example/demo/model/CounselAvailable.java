package com.example.demo.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CounselAvailable {
    private int counselNo;   //counsel_no
    private String counselorId;   // counselor_id
    private LocalDateTime start;

}