package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.ClientMapper;
import com.example.demo.mapper.VerifyMapper;
import com.example.demo.model.AccountVerification;
import com.example.demo.model.Client;

@Service
public class VerifyService{
	
	@Autowired
	VerifyMapper verifyMapper;
	
	@Autowired
	ClientMapper ClientMapper;
	
	public AccountVerification findByCode(String code, String type) {
		return verifyMapper.findByCode(code, type);
	}

	public int updateEmailVerified(String code) {
		return verifyMapper.updateEmailVerified(code);
	}

	public Client getTestClient() {
		return verifyMapper.getTestClient();
	}

	public void insertTestClient(Client client) {
		verifyMapper.insertTestClient(client);
	}

	public void insertVerification(AccountVerification verification) {
		verifyMapper.insertVerification(verification);
	}

	public int updateVerificationTable(String code, String type) {
		return verifyMapper.updateVerificationTable(code, type);
	}


	public Client findIdClient(String email) {
		
		return verifyMapper.findIdClient(email);
	}

	public boolean isEmailExist(String email) {
		return verifyMapper.isEmailExist(email);
	}

	public AccountVerification findByEmailAndType(String email, String type) {
		return verifyMapper.findByEmailAndType(email, type);
	}

	public Client findIdClient(String email, String domain) {
		return verifyMapper.findIdClient(email, domain);
	}

	public void updateClientTable(String clientId) {
		verifyMapper.updateClientTable(clientId);
	}

	public AccountVerification findByPhoneAndType(String phone, String type) {
		return verifyMapper.findByPhoneAndType(phone, type);
	}

	public void updateVerificationForJoin(AccountVerification existing) {
		verifyMapper.updateVerificationForJoin(existing);
	}

	public void insertClientIdByPhone(String phone, String clientId) {
	    Map<String, Object> map = new HashMap<>();
	    map.put("phone", phone);
	    map.put("clientId", clientId);
	    verifyMapper.insertClientIdByPhone(map);  // ✅ Mapper 호출
	}

	public void updateVerifiedById(String clientId) {
		verifyMapper.updateVerifiedById(clientId);
	}


	
	
}