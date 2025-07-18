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

    // 병원 목록 조회 (검색용)
    @GetMapping("/list")
    public ResponseEntity<?> getHospitals(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "area", required = false) String area,
            @RequestParam(name = "dept", required = false) String dept,
            @RequestParam(name = "type", required = false) String type){

        try {
            return ResponseEntity.ok(service.getHospitals(name, area, dept, type));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("오류 발생: " + e.getMessage());
        }
    }
    
    
    
//    @GetMapping("/hospital/map")
//    public String showHospitalMap() {
//        return "map/hospitalMap"; // => /WEB-INF/views/hospitalMap.jsp 로 forward 됨
//    }
}



