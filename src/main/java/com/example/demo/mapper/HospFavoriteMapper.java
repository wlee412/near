package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface HospFavoriteMapper {
    int insertFavorite(@Param("clientId") String clientId, @Param("hospId") String hospId);
    
    int deleteFavorite(@Param("clientId") String clientId, @Param("hospId") String hospId);
    
    boolean isFavorited(@Param("clientId") String clientId, @Param("hospId") String hospId);
}
