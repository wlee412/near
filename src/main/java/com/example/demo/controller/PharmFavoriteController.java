package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.PharmFavoriteService;

@RestController
@RequestMapping("/api/favorite/pharm")
public class PharmFavoriteController {

    @Autowired
    private PharmFavoriteService service;

    // 즐겨찾기 등록
    @PostMapping("/add")
    public ResponseEntity<String> addFavorite(@RequestParam String clientId, @RequestParam String pharmId) {
        if (service.addFavorite(clientId, pharmId)) {
            return ResponseEntity.ok("즐겨찾기 등록 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록 실패");
        }
    }

    // 즐겨찾기 해제
    @PostMapping("/remove")
    public ResponseEntity<String> removeFavorite(@RequestParam String clientId, @RequestParam String pharmId) {
        if (service.removeFavorite(clientId, pharmId)) {
            return ResponseEntity.ok("즐겨찾기 삭제 완료");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }

    // 즐겨찾기 여부 확인
    @GetMapping("/check")
    public ResponseEntity<Boolean> isFavorited(@RequestParam String clientId, @RequestParam String pharmId) {
        return ResponseEntity.ok(service.isFavorited(clientId, pharmId));
    }
}