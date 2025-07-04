package com.example.demo.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.example.demo.model.Client;

@Mapper
public interface ClientMapper {

	public int countById(@Param("clientId") String clientId);

//	public int countByNickname(@Param("nickname") String nickname);

	public int insert(Client client);

	public Client findById(String id);

	public int updateClient(Client client);

	public Client selectClientById(String id);

	public int updatePassword(Client client); // 일반 비번 변경

	public int deleteClient(String id);

	public Client selectByEmailForRegister(String email);

	public Client selectByEmailForFind(String email);
	
	public Client selectByIdAndEmail(@Param("id") String id, @Param("email") String email);

	public int updatePasswordForFind(Map<String, Object> paramMap); // 비번찾기 후 비번변경

	// 이메일인증 : 재원님꺼 추가함

	public Client findByEmail(@Param("email") String email);

	public int checkIdExists(String id);

	public Client findByEmailForRegister(String email);

	public boolean registerClient(Client client);
	
	
	// 로그인 : 영교님꺼 추가	
	
	void socialInsert(Client client);

	public Client login(String clientId);
	
	Client findBySocialIdAndPlatform(@Param("socialId") String socialId, @Param ("platform") String platform);

	


}