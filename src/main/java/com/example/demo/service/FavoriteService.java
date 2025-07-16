package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.FavoriteMapper;
import com.example.demo.model.HospFavorite;
import com.example.demo.model.PharmFavorite;

@Service
public class FavoriteService {
	
	@Autowired
	FavoriteMapper FavoriteMapper;
	
	public List<PharmFavorite> getPharmFavoriteList(String clientId) {
		return FavoriteMapper.getPharmFavoriteList(clientId);
	}

	public List<HospFavorite> getHospFavoriteList(String clientId) {
		return FavoriteMapper.getHospFavoriteList(clientId);
	}

	public int deletePharmFav(String clientId, String pharmId) {
		return FavoriteMapper.deletePharmFav(clientId,pharmId);
	}
	
	public int deleteHospFav(String clientId, String hospId) {
		return FavoriteMapper.deleteHospFav(clientId,hospId);
	}

	
}
