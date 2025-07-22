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

    // 일반 검색용
    List<Map<String, Object>> searchHospitals(
        @Param("name") String name,
        @Param("area") String area,
        @Param("dept") String dept,
        @Param("type") String type
    );

    // ✅ 내 위치 기반 반경 검색용
    List<Map<String, Object>> findNearbyHospitals(
        @Param("lat") double lat,
        @Param("lng") double lng,
        @Param("radius") int radius,
        @Param("name") String name,
        @Param("area") String area,
        @Param("type") String type
    );
}