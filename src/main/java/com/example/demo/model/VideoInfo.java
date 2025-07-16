package com.example.demo.model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("vid")
public class VideoInfo {
	private String clientName;
	private LocalDateTime start;
	private String recName;

	private String startFilter;
	private String counselorId;
	
	// Date형 - 포맷팅 가능
	public Date getStartDate() {
		return Date.from(start.atZone(ZoneId.systemDefault()).toInstant());
	}
}
