package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.model.Counselor;
import com.example.demo.model.RoomRecording;
import com.example.demo.model.VideoInfo;
import com.example.demo.service.VideoService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/vid")
public class VideoController {
	private final String savePath = "/var/secure-videos/";

	@Autowired
	private VideoService videoService;

	// 영상 녹화
	@PostMapping("/rec")
	public ResponseEntity<String> uploadRecording(@RequestParam("file") MultipartFile file,
			@ModelAttribute RoomRecording rec) {
		System.out.println("영상 저장 중...");
		String filename = UUID.randomUUID() + ".webm";
		Path path = Paths.get(savePath + filename);

		try {
			Files.copy(file.getInputStream(), path);
			rec.setName(filename);
			videoService.uploadRec(rec);
			System.out.println("영상 저장 완료!");
			return ResponseEntity.ok("저장 완료: " + filename);
		} catch (IOException ioe) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류");
		}
	}

	// 다운로드 페이지
	@GetMapping("")
	public ModelAndView videoList(@ModelAttribute VideoInfo vid, HttpSession session, Model model) {
		
		if (session.getAttribute("loginCounselor") == null) {
			return new ModelAndView("redirect:/");
	    }
		
		String counselorId = ((Counselor) session.getAttribute("loginCounselor")).getCounselorId();
		vid.setCounselorId(counselorId);
		List<VideoInfo> recordings = videoService.videoList(vid);
		model.addAttribute("recordings", recordings);
		return new ModelAndView("room/recordings");
	}

	// 영상 다운로드
	@GetMapping("/rec/{filename}")
	public ResponseEntity<Resource> download(@PathVariable("filename") String filename, HttpSession session) {
		Counselor counselor = (Counselor)session.getAttribute("loginCounselor");
		VideoInfo info = videoService.getVideoInfo(filename);
		if (counselor == null || !counselor.getCounselorId().equals(info.getCounselorId()) ) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
	    }
		
		Path path = Paths.get(savePath, filename);
		Resource resource;
		try {
			resource = new UrlResource(path.toUri());
			if (!resource.exists()) {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.internalServerError().build();
		}
		String displayName = videoService.getVideoName(filename);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + displayName + "\"").body(resource);
	}

}
