package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.HospFavorite;
import com.example.demo.model.PharmFavorite;

@Mapper
public interface FavoriteMapper {


	int deletePharmFav(@Param("clientId")String clientId,@Param("pharmId")String pharmId);

	int deleteHospFav(@Param("clientId")String clientId,@Param("hospId")String hospId);
	
	List<PharmFavorite> getPharmFavoriteList(String clientId);
	
	List<HospFavorite> getHospFavoriteList(String clientId);

}
