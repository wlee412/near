package com.example.demo.model;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AccountVerification {
	
	private int num;
	private String id;
	private String code;
	private String type;
	private Timestamp expiresAt;
	private char verified;
	private Timestamp createdAt;
	private Timestamp usedAt;
	
}