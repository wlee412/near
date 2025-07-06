package com.example.demo.service;

import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.mapper.ClientMapper;
import com.example.demo.model.Client;

@Service
@Transactional
public class ClientService {

	private final PasswordEncoder passwordEncoder;
	private final ClientMapper clientMapper;
	private final EmailService emailService;

	@Autowired
	public ClientService(PasswordEncoder passwordEncoder, ClientMapper clientMapper, EmailService emailService) {
		this.passwordEncoder = passwordEncoder;
		this.clientMapper = clientMapper;
		this.emailService = emailService;
	}
	
	public boolean updateClient(Client member) {
		return clientMapper.updateClient(member) > 0;
	}

	public boolean updatePassword(Client member) {
		return clientMapper.updatePassword(member) > 0;
	}

	public boolean deleteClient(String clientId) {
		return clientMapper.deleteClient(clientId) > 0;
	}

	public Client getClientById(String clientId) {
		return clientMapper.getClientById(clientId);
	}

	public void sendAuthCode(String email, String code) {
		emailService.sendVerificationEmail(email, code);
	}

	public void sendIdAuthCode(String email, String code) {
		emailService.sendFindIdEmail(email, code);
	}

	public Client findByEmailForFind(String email) {
		return clientMapper.selectByEmailForFind(email);
	}

	public Client findByIdAndEmail(String clientId, String email) {
		return clientMapper.selectByIdAndEmail(clientId, email);
	}

	public boolean updatePasswordForFind(Map<String, Object> paramMap) {
		return clientMapper.updatePasswordForFind(paramMap) > 0;
	}

	// 지선, 재원 중복 맞춤
	public Client findByEmail(String email) {
		return clientMapper.findByEmail(email);
	}
	
	public boolean checkIdExists(String clientId) {
		return clientMapper.countById(clientId) > 0;
	}
	
//	public boolean checkNicknameExists(String nickname) {
//		return clientMapper.countByNickname(nickname) > 0;
//	}
	
	public Client findByEmailForRegister(String email) {
		return clientMapper.selectByEmailForRegister(email);
	}
	
	public boolean registerClient(Client client) {
		return clientMapper.insert(client) > 0;
	}
	
	
	//영교님 꺼 
	public Client login(Client login) {
		Client dbClient = clientMapper.login(login.getClientId());
		if(dbClient != null && passwordEncoder.matches(login.getPassword(), dbClient.getPassword())) {
			return dbClient;
		}
		return null;
	}

	public Client findById(@Param("clientId") String clientId) {
		return clientMapper.findById(clientId);
	}
	


	
}


