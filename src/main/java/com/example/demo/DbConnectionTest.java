package com.example.demo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class DbConnectionTest implements CommandLineRunner {

    private final DataSource dataSource;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = dataSource.getConnection()) {
            System.out.println("✅ DB 연결 성공!");
            System.out.println("URL: " + conn.getMetaData().getURL());
            System.out.println("사용자명: " + conn.getMetaData().getUserName());
        } catch (Exception e) {
            System.out.println("❌ DB 연결 실패!");
            e.printStackTrace();
        }
    }
}
