package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.HospFavoriteMapper;

@Service
public class HospFavoriteService {

    @Autowired
    private HospFavoriteMapper mapper;

    public boolean addFavorite(String clientId, String hospId) {
        return mapper.insertFavorite(clientId, hospId) > 0;
    }

    public boolean removeFavorite(String clientId, String hospId) {
        return mapper.deleteFavorite(clientId, hospId) > 0;
    }

    public boolean isFavorited(String clientId, String hospId) {
        return mapper.isFavorited(clientId, hospId);
    }
}