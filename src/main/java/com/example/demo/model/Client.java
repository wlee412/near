package com.example.demo.model;

import java.sql.Date;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class Client{
    private String clientId;
    private String password;
    private String name;
    private String phone;
    private String emailId;
    private String emailDomain;
    private String address;
    private Date birth;
    private char gender;
    private boolean alarm;
    private boolean personalInfo;
    private String socialPlatform;
    private String socialId;
    private String interest;
    private String emailVerifed;
    private Timestamp regDate;
}
