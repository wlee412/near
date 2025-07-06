package com.example.demo.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class Client{
    private String clientId;        
    private String password;        
    private String name;            
    private String phone;           
    private String emailId;         
    private String emailDomain;     

    private String zipcode;         
    private String addrBase;        
    private String addrDetail;      

    private Date birth;        
    private String gender;          

    private boolean alarm;          
    private boolean personalInfo;   

    private String socialPlatform;  
    private String socialId;        
    private String interest;       

    private String verified;   
    private int state;              
    private Timestamp regDate;      
}
