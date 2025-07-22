package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.mapper.HospitalMapper;
import com.example.demo.service.HospitalService;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    @Autowired
    private HospitalService service;

    @Autowired
    private HospitalMapper mapper;

    // 병원 데이터 저장 (전체 저장용)
    @GetMapping("/load")
    public ResponseEntity<String> loadAllHospitals() {
        try {
            service.fetchAndSaveAllHospitals();
            return ResponseEntity.ok("병원 정보 전체 저장 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("오류 발생: " + e.getMessage());
        }
    }

    // 병원 목록 조회 (검색용)  ★ 변경: 파라미터 정리 & "전체 병원 종류" 처리
    @GetMapping("/list")
    public ResponseEntity<?> getHospitals(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "area", required = false) String area,
            @RequestParam(name = "dept", required = false) String dept,
            @RequestParam(name = "type", required = false) String type) {

        try {
            // ★ 변경: trim & 빈문자 null 처리
            name = normalize(name);
            area = normalize(area);
            dept = normalize(dept);
            type = normalize(type);

            // ★ 변경: 필터 꺼짐 처리
            if ("전체 병원 종류".equals(type)) type = null;

            List<Map<String, Object>> data = service.getHospitals(name, area, dept, type);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("오류 발생: " + e.getMessage());
        }
    }

    // 반경 검색 (필요 시 남겨둠)  ★ 변경: km → int 미터값 들어오면 /1000 변환
    @GetMapping("/nearby")
    public ResponseEntity<?> getNearbyHospitals(
            @RequestParam("lat") double lat,
            @RequestParam("lng") double lng,
            @RequestParam(value = "radius", defaultValue = "3000") int radiusMeters, // JS는 3000m
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "area", required = false) String area,
            @RequestParam(name = "type", required = false) String type) {

        try {
            // ★ 변경: normalize
            name = normalize(name);
            area = normalize(area);
            type = normalize(type);
            if ("전체 병원 종류".equals(type)) type = null;

            // ★ 변경: 기존 SQL이 km 단위이므로 m→km 환산
            int radiusKm = (int) Math.ceil(radiusMeters / 1000.0);

            List<Map<String, Object>> result = service.findHospitalsByDistance(lat, lng, radiusKm, name, area, type);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("오류 발생: " + e.getMessage());
        }
    }

    // --- 유틸 ---
    // ★ 변경: 공백 문자열 -> null
    private String normalize(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }
}
    
    
    
//    @GetMapping("/hospital/map")
//    public String showHospitalMap() {
//        return "map/hospitalMap"; // => /WEB-INF/views/hospitalMap.jsp 로 forward 됨
//    }





