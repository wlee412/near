package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface YoutubeMapper {

    List<String> selectVideosByMood(@Param("mood") String mood);

    void insertYoutubeVideo(@Param("mood") String mood,
                            @Param("keyword") String keyword,
                            @Param("videoId") String videoId);
}
