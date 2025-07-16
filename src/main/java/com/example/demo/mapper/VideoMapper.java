package com.example.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.RoomRecording;
import com.example.demo.model.VideoInfo;

@Mapper
public interface VideoMapper {

	public void uploadRec(RoomRecording rec);

	public List<VideoInfo> videoList(VideoInfo vid);
	
	public VideoInfo getVideoInfo(String filename);

	public VideoInfo getVideoName(String filename);

}
