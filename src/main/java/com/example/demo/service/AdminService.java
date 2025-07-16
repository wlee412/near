package com.example.demo.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.AdminMapper;
import com.example.demo.model.Admin;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public boolean login(String id, String password) {
        Admin admin = adminMapper.findAdminById(id);
        
        System.out.println("💡 전달된 raw adminId bytes: " + Arrays.toString(id.getBytes()));
        System.out.println("💡 DB에 있는 adminId와 비교 결과: " + "admin".equals(id));
        System.out.println("👉 [디버그] 조회된 admin: " + admin);
        System.out.println("💡 전달된 id: [" + id + "]");
        System.out.println("💡 전달된 password: [" + password + "]");

        if (admin == null) {
            System.out.println("❌ 사용자 없음");
            return false;
        }

        if (!admin.getPassword().equals(password)) {
            System.out.println("❌ 비밀번호 불일치: 입력=" + password + ", DB=" + admin.getPassword());
            return false;
        }

        System.out.println("✅ 로그인 성공");
        return true;
    }
}