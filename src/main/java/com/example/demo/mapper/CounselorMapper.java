package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.Counselor;

@Mapper
public interface CounselorMapper {

	Counselor loginCounselor(Counselor loginCounselor);

}
