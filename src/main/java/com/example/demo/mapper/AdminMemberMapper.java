package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.AdminMember;
@Mapper
public interface AdminMemberMapper {
	List<AdminMember> selectBasicMembers(); // 상태 관리용 정보 조회
	
	// 상태 변경
	int updateState(@Param("client_id") String client_id, @Param("state") int state);

	// 검색 + 페이징된 회원 목록
	List<AdminMember> pagingSearch(@Param("type") String type, @Param("keyword") String keyword,
			@Param("startRow") int startRow, @Param("endRow") int endRow);

	// 검색 + 페이징 총 개수
	int getTotalCount(@Param("type") String type, @Param("keyword") String keyword);
}
