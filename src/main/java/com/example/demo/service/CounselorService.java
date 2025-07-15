package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.CounselorMapper;
import com.example.demo.model.Counselor;

@Service
public class CounselorService {
	
	  @Autowired
	    private CounselorMapper counselorMapper;


		public Counselor loginCounselor(Counselor loginCounselor) {
	        return counselorMapper.loginCounselor(loginCounselor);
		}
}
