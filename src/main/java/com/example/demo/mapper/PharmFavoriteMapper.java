package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PharmFavoriteMapper {
	
    int insertFavorite(@Param("clientId") String clientId, @Param("pharmId") String pharmId);
    
    int deleteFavorite(@Param("clientId") String clientId, @Param("pharmId") String pharmId);
    
    boolean isFavorited(@Param("clientId") String clientId, @Param("pharmId") String pharmId);
}
