package com.example.demo.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AdminMember {
	    private String clientId;
	    private String name;
	    private String phone;
	    private String emailId;         
	    private String emailDomain; 
	    private String addrBase;	// 주소
	    private int state;
	    private Timestamp regDate;
	    
	    // 페이징 및 검색용
	    private int startRow;
	    private int endRow;
	    private String type;   // name, email, state
	    private String keyword;
	    
}
