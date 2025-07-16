package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.model.Client;
import com.example.demo.model.CounselReservationDTO;
import com.example.demo.service.ReservationService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // ✅ 1. 예약 불가 시간 조회 (이미 예약된 시간)
    @GetMapping("/unavailable")
    public List<String> getUnavailableTimes(
    	    @RequestParam("counselorId") String counselorId,
    	    @RequestParam("date") String date
    	) {
        return reservationService.getUnavailableTimes(counselorId, date);
    }

    // ✅ 2. 예약 가능한 시간 조회 (예약 가능한 시간대만)
    @GetMapping("/available-times")
    public List<String> getAvailableTimes(
        @RequestParam("counselorId") String counselorId,
        @RequestParam("date") String date
    ) {
        return reservationService.getAvailableTimes(counselorId, date);
    }

    // ✅ 3. 특정 시간의 counsel_no 조회 (예약 저장용)
    @GetMapping("/find-counselNo")
    public ResponseEntity<?> getCounselNo(@RequestParam String counselorId,
                                          @RequestParam String start) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(start);  // "yyyy-MM-ddTHH:mm"
            Integer counselNo = reservationService.findCounselNo(counselorId, dateTime);
            return ResponseEntity.ok(counselNo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("counsel_no 조회 실패: " + e.getMessage());
        }
    }

    // ✅ 4. 예약 저장
    @PostMapping("/save")
    public ResponseEntity<?> saveReservation(HttpSession session,@RequestBody Map<String, Object> payload) {
    	 Client client = (Client) session.getAttribute("loginClient");
    	 
         if (client == null) {
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
         }
    	
    	try {
            String clientId = (String) payload.get("clientId");
            String counselorId = (String) payload.get("counselorId");
            String date = (String) payload.get("date");         // yyyy-MM-dd
            String time = (String) payload.get("time");         // HH:mm
            List<String> sympCode = (List<String>) payload.get("symptoms");

            CounselReservationDTO dto = reservationService.createReservationDTO(
                clientId, counselorId, date, time, sympCode
            );

            boolean result = reservationService.saveReservation(dto);
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("예약 처리 실패: " + e.getMessage());
        }
    }
}