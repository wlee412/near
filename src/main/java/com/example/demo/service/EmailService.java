
package com.example.demo.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendHtmlMessage(String to, String htmlContent) {
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
			helper.setTo(to);
			helper.setSubject("오늘의 게임 할인 소식🎮");
			helper.setText(htmlContent, true);
			helper.setFrom("2j1william@gmail.com");
			helper.addInline("logoImage", new File("src/main/resources/static/images/icons/logo.png"));
			helper.addInline("emailIcon", new File("src/main/resources/static/images/icons/email.png"));
			helper.addInline("kakaoIcon", new File("src/main/resources/static/images/icons/kakao.png"));

			mailSender.send(message);
			System.out.println("이메일 전송 완료: " + to);
		} catch (Exception e) {
			System.out.println("이메일 전송 실패: " + to);
			e.printStackTrace();
		}
	}

	public void sendFindIdEmail(String to, String code) {
		String subject = "[Gamesamo]아이디 찾기 인증코드";
		String content = "" + "<html>"
				+ "<body style='font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 30px;'>"
				+ "<table style='margin: 0 auto; text-align: center'>" + "<tr>" + "<td style='text-align: center;'>"
				+ "<img src='https://i.imgur.com/VKxL314.png' style='width: 300px; height: 80px;'>" + "</td>" + "</tr>"
				+ "</table>" + "<div style='height: 30px;'></div>"
				+ "<div style='max-width: 500px; margin: auto; background-color: #fff; border-radius: 12px; padding: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1);'>"
				+ "<div style='text-align: center; margin-bottom:10px;'>아이디 찾기 인증코드:</div>"
				+ "<h2 style='text-align: center; color: #333; background-color: #F7F7F7; width: 180px; margin: 0 auto;\r\n"
				+ "    border-radius: 10px;\r\n'>" + code + "</h2>"
				+ "<p style='text-align: center;'>10분 이내로 6자리 코드를 입력해 주세요.</p>" + "</div>" + "</body>" + "</html>";

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(new InternetAddress("2j1william@gmail.com", "Gamesemo"));
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendFindPwEmail(String to, String code) {
		String subject = "[Gamesamo]비밀번호 찾기 인증 코드";
		String content = "" + "<html>"
				+ "<body style='font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 30px;'>"
				+ "<table style='margin: 0 auto; text-align: center'>" + "<tr>" + "<td style='text-align: center;'>"
				+ "<img src='https://i.imgur.com/VKxL314.png' style='width: 300px; height: 80px;'>" + "</td>" + "</tr>"
				+ "</table>" + "<div style='height: 30px;'></div>"
				+ "<div style='max-width: 500px; margin: auto; background-color: #fff; border-radius: 12px; padding: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1);'>"
				+ "<div style='text-align: center; margin-bottom:10px;'>비밀번호 찾기 인증코드:</div>"
				+ "<h2 style='text-align: center; color: #333; background-color: #F7F7F7; width: 180px; margin: 0 auto;\r\n"
				+ "    border-radius: 10px;\r\n'>" + code + "</h2>"
				+ "<p style='text-align: center;'>10분 이내로 6자리 코드를 입력해 주세요.</p>" + "</div>" + "</body>" + "</html>";

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(new InternetAddress("2j1william@gmail.com", "Gamesemo"));
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendVerificationEmail(String to, String code) {
		String subject = "[Gamesamo]이메일 회원인증 요청";
		String url = "http://3.34.122.138/verify/verifyCode?code=" + code + "&type=MEMBER_JOIN"; 
		String content = "" + "<html>"
				+ "<body style='font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 30px;'>"
				+ "<table style='margin: 0 auto; text-align: center'>" + "<tr>" + "<td style='text-align: center;'>"
				+ "<img src='https://i.imgur.com/VKxL314.png' style='width: 300px; height: 80px;'>" + "</td>" + "</tr>"
				+ "</table>" + "<div style='height: 30px;'></div>"
				+ "<div style='max-width: 500px; margin: auto; background-color: #fff; border-radius: 12px; padding: 30px; box-shadow: 0 0 10px rgba(0,0,0,0.1);'>"
				+ "<h2 style='text-align: center; color: #333;'>이메일 인증</h2>"
				+ "<p style='text-align: center;'>아래 버튼을 클릭하면 인증이 완료됩니다.</p>"
				+ "<div style='text-align: center; margin-top: 30px;'>" + "<a href='" + url
				+ "' style='background-color: #000000; color: white; padding: 12px 24px; border-radius: 6px; text-decoration: none;'>이메일 인증</a>"
				+ "</div>" + "</div>" + "</body>" + "</html>";

		MimeMessage message = mailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(new InternetAddress("2j1william@gmail.com", "Gamesemo"));
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, true);
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}