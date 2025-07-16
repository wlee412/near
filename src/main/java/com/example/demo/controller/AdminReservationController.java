package com.example.demo.controller;

import com.example.demo.model.AdminReservation;
import com.example.demo.service.AdminReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminReservationController {

    private final AdminReservationService reservationService;

    @GetMapping("/adminReservation")
    public String reservationPage(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "type", required = false) String type,
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {

        int pageSize = 10;
        int total = reservationService.getTotalCount(type, keyword);
        int totalPages = (int) Math.ceil((double) total / pageSize);

        List<AdminReservation> list = reservationService.getReservations(page, pageSize, type, keyword);

        model.addAttribute("reservations", list);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        return "admin/adminReservation";
    }

    //  취소 기능 추가
    @PostMapping("/cancelReservation")
    @ResponseBody
    public String cancelReservation(@RequestParam("reservationNo") int reservationNo) {
        boolean success = reservationService.cancelReservation(reservationNo);
        return success ? "success" : "fail";
    }
}
