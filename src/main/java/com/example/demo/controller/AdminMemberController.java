package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.model.AdminMember;
import com.example.demo.page.AdminPagenation;
import com.example.demo.service.AdminMemberService;

@Controller
@RequestMapping("/admin")
public class AdminMemberController {

    @Autowired
    private AdminMemberService adminMemberService;

    @GetMapping("/adminMember")
    public String showMemberList(@RequestParam(name = "pageNum", defaultValue = "1") int pageNum,
                                 @RequestParam(name = "type", required = false) String type,
                                 @RequestParam(name = "keyword", required = false) String keyword,
                                 Model model) {

        final int pageSize = 10;   // 페이지당 게시물 수
        final int blockSize = 10;  // 페이징 블럭 크기

        int total = adminMemberService.getTotal(type, keyword);

        // 페이징 계산 클래스 사용
        AdminPagenation pagenation = new AdminPagenation(total, pageNum, pageSize, blockSize);

        List<AdminMember> memberList = adminMemberService.pagingSearch(
            type, keyword, pagenation.getStartRow(), pagenation.getEndRow()
        );

        int no = total - pagenation.getStartRow() + 1;

        model.addAttribute("memberList", memberList);
        model.addAttribute("no", no);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("total", total);
        model.addAttribute("type", type);
        model.addAttribute("keyword", keyword);

        // 페이징 정보 전달
        model.addAttribute("pageCount", pagenation.getPageCount());
        model.addAttribute("startPage", pagenation.getStartPage());
        model.addAttribute("endPage", pagenation.getEndPage());
        model.addAttribute("currentPage", pagenation.getCurrentPage());

        return "admin/adminMember";
    }

    
    // 회원 상태 업데이트
    @PostMapping("/updateState")
    @ResponseBody
    public ResponseEntity<String> updateMemberState(@RequestParam("client_id") String client_id,
            @RequestParam("state") int state) {
        int result = adminMemberService.updateState(client_id, state);
        return result > 0 ? ResponseEntity.ok("success") : ResponseEntity.status(500).body("fail");
    }
}