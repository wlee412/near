package com.example.demo.model;

import lombok.Data;

@Data
public class HospitalInfo {
    private String hospId;
    private String hospName;
    private String hospAddress;
    private String hospTel;
    private String hospType;
    private String hospDepartment;
    private Double hospLat;
    private Double hospLng;
}