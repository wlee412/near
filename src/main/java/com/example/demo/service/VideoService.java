package com.example.demo.service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.mapper.VideoMapper;
import com.example.demo.model.RoomRecording;
import com.example.demo.model.VideoInfo;

@Service
public class VideoService {
	@Autowired
	private VideoMapper videoMapper;

	public void uploadRec(RoomRecording rec) {
		videoMapper.uploadRec(rec);
	}

	public List<VideoInfo> videoList(VideoInfo vid) {
		return videoMapper.videoList(vid);
	}
	
	public VideoInfo getVideoInfo(String filename) {
		return videoMapper.getVideoInfo(filename);
	}

	public String getVideoName(String filename) {
		VideoInfo vid = videoMapper.getVideoName(filename);
		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd_HH시");
		String displayName = vid.getClientName() + "_" + sd.format(vid.getStartDate()) + ".webm";
		return displayName;
	}
}
