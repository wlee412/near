package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.AdminMemberMapper;
import com.example.demo.model.AdminMember;

@Service

public class AdminMemberService {

    @Autowired
    private AdminMemberMapper adminMemberMapper;

    // 회원 목록 출력
    public List<AdminMember> getBasicMemberList() {
    	return adminMemberMapper.selectBasicMembers();
    }

    // 회원 상태 관리
    public int updateState(String client_id, int state) {
        return adminMemberMapper.updateState(client_id, state);
    }
    
    
 // 검색 + 페이징 리스트
    public List<AdminMember> pagingSearch(String type, String keyword, int startRow, int endRow) {
        return adminMemberMapper.pagingSearch(type, keyword, startRow, endRow);
    }

    // 검색 + 페이징 총 개수
    public int getTotal(String type, String keyword) {
        return adminMemberMapper.getTotalCount(type, keyword);
    }
}
