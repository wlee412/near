package com.example.demo.model;

import lombok.Data;

@Data
public class AdminMember {
	    private String client_id;
	    private String name;
	    private String phone;
	    private String emailId;         
	    private String emailDomain; 
	    private String addr_base;	// 주소
	    private int state;
	    
	    // 페이징 및 검색용
	    private int startRow;
	    private int endRow;
	    private String type;   // name, email, state
	    private String keyword;
	    
}
