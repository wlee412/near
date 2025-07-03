package com.example.demo.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.AccountVerification;
import com.example.demo.model.Client;

@Mapper
public interface VerifyMapper {


	public Client getTestClient();

	AccountVerification findByCode(@Param("code") String code, @Param("type") String type);

	public int updateEmailVerified(String token);

	public void insertTestClient(Client Client);

	public void insertVerification(AccountVerification verification);

	public int updateVerificationTable(@Param("code") String code, @Param("type") String type);

	public Client findIdClient(@Param("email")String email);

	public boolean isEmailExist(@Param("email") String email);

	AccountVerification findByEmailAndType(@Param("email") String email, @Param("type") String type);

	public AccountVerification findByIdAndType(Map<String, Object> map);

	public Client findIdClient(String email, String domain);

	public void updateClientTable(String id);

}