package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.PharmFavoriteMapper;


@Service
public class PharmFavoriteService {

    @Autowired
    private PharmFavoriteMapper mapper;

    public boolean addFavorite(String clientId, String pharmId) {
        return mapper.insertFavorite(clientId, pharmId) > 0;
    }

    public boolean removeFavorite(String clientId, String pharmId) {
        return mapper.deleteFavorite(clientId, pharmId) > 0;
    }

    public boolean isFavorited(String clientId, String pharmId) {
        return mapper.isFavorited(clientId, pharmId);
    }
}