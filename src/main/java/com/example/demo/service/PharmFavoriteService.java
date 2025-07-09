package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.PharmFavoriteMapper;
import com.example.demo.model.PharmFavorite;

@Service
public class PharmFavoriteService {
	
	@Autowired
	PharmFavoriteMapper pharmFavoriteMapper;
	
	public List<PharmFavorite> getPharmFavoriteList(String clientId) {
		return pharmFavoriteMapper.getPharmFavoriteList(clientId);
	}

	public int deletePharmFav(String clientId, String pharmId) {
		return pharmFavoriteMapper.deletePharmFav(clientId,pharmId);
	}
	
}
