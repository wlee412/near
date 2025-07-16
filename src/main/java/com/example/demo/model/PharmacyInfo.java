package com.example.demo.model;

import lombok.Data;

@Data
public class PharmacyInfo {
    private String pharmId;
    private String pharmName;
    private String pharmAddress;
    private String pharmTel;
    private Double pharmLat;
    private Double pharmLng;
    private String openHour;
}
