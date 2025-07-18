package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.PharmacyService;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacyController {

    @Autowired
    private PharmacyService service;

    @GetMapping("/load")
    public ResponseEntity<String> loadAllPharmacies() {
        try {
            service.fetchAndSaveAllPharmacies();
            return ResponseEntity.ok("약국 정보 전체 저장 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("오류 발생: " + e.getMessage());
        }
    }
    
    @GetMapping("/list")
    public ResponseEntity<?> getPharmacies(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "area", required = false) String area) {

        try {
            return ResponseEntity.ok(service.getPharmacies(name, area));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("오류 발생: " + e.getMessage());
        }
    }
    
}