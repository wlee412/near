package com.example.demo.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.PharmacyInfo;

@Mapper
public interface PharmacyMapper {
    PharmacyInfo findById(String pharmId);
    
    void insert(PharmacyInfo pharmacy);
    
 // 약국 검색
    List<Map<String, Object>> searchPharmacies(@Param("name") String name, @Param("area") String area);
}

