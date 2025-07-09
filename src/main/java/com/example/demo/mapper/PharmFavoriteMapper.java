package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.model.PharmFavorite;

@Mapper
public interface PharmFavoriteMapper {

	List<PharmFavorite> getPharmFavoriteList(String clientId);

	int deletePharmFav(@Param("clientId")String clientId,@Param("pharmId")String pharmId);
}
