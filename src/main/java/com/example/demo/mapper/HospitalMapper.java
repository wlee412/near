package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.HospitalInfo;

@Mapper
public interface HospitalMapper {
	// 병원 ID로 중복 검사
    HospitalInfo findById(String hospId);
    
    // 병원 데이터 삽입
    void insert(HospitalInfo hospital);
    
    List<Map<String, Object>> searchHospitals(@Param("name") String name,
            @Param("area") String area,
            @Param("dept") String dept, @Param("type") String type);
}
