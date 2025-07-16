<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>화상채팅</title>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/webrtc-adapter/6.4.0/adapter.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery.blockUI/2.70/jquery.blockUI.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/bootbox.js/5.4.0/bootbox.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/spin.js/2.3.2/spin.min.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.4/toastr.min.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootswatch/3.4.0/cerulean/bootstrap.min.css"
	type="text/css" />

<script type="text/javascript" src="/js/janus.js"></script>
<script type="text/javascript" src="/js/videoroom.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/lz-string@1.4.4/libs/lz-string.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/fabric.js/5.3.0/fabric.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client@1.6.1/dist/sockjs.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/lodash@4.17.21/lodash.min.js"></script>
<link rel="stylesheet" href="/css/common.css" type="text/css" />
<link rel="stylesheet" href="/css/videoroom.css" type="text/css" />
<link rel="stylesheet" href="/css/roomlayout.css" type="text/css" />
<link rel="stylesheet" href="/css/textroom.css" type="text/css" />
<link rel="stylesheet" href="/css/toolbar.css" type="text/css" />
<script>
	var roomId = ${room.roomId};
	var myroom = ${room.janusNum};
	var iAm = "${username}";
</script>
</head>

<body>
	<div class="container wrapper">
		<div id="videojoin">
		<div class="row" id="camera-and-desc">
				<div class="col-md-6" style="height: 100%;">
					<div id="camera-test-container">
						<div class="panel" style="height: 100%">
							<div class="panel-heading">
								<h3 class="panel-title">카메라 테스트</h3>
							</div>
							<div class="panel-body" id="cam-test">
								<video id="camera-test-video" autoplay playsinline muted></video>
							</div>
						</div>
						<div id="camtest-btn" align="right">
							<button id="camera-test-start" class="svg-btn" onclick="startCameraTest()">
							<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-video-off-icon lucide-video-off"><path d="M10.66 6H14a2 2 0 0 1 2 2v2.5l5.248-3.062A.5.5 0 0 1 22 7.87v8.196"/><path d="M16 16a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h2"/><path d="m2 2 20 20"/></svg>
							카메라 켜기
							</button>
							<button id="camera-test-stop" class="svg-btn" onclick="stopCameraTest()">
							<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-video-icon lucide-video"><path d="m16 13 5.223 3.482a.5.5 0 0 0 .777-.416V7.87a.5.5 0 0 0-.752-.432L16 10.5"/><rect x="2" y="6" width="14" height="12" rx="2"/></svg>
							카메라 끄기
							</button>
						</div>
					</div>
				</div>
				<div class="col-md-6" id="description">
					<div  id="desc-content">
						<h2>심리 상담</h2>
						<p>상담 일시: <fmt:formatDate value="${rsv.startDate }" pattern="yyyy년 M월 d일 E요일 HH:mm"/></p>
						<c:if test="${who eq 'client' }">
							<p>상담사: ${rsv.counselorName }</p>
							<p>상담사 연락처: ${rsv.counselorPhone }</p>
						</c:if>
						<c:if test="${who eq 'counselor' }">
							<p>내담자: ${rsv.clientName }</p>
							<p>내담자 연락처: ${rsv.clientPhone }</p>
						</c:if>
						<p>상담 사유: ${rsv.sympCsv }</p>
					</div>
					<div id="controls">
						<div id="registernow">
							<span class="label label-info" id="room"></span>
							<div class="input-group margin-bottom-md "
								style="width: 100% !important;">
								<span class="input-group-addon group-label">상담실 번호</span>
								<input autocomplete="off" class="form-control" type="text"
									id="roomname" value="${room.janusNum}" readonly />
							</div>
							<span class="label label-info" id="you"></span>
							<div class="input-group margin-bottom-md ">
								<span class="input-group-addon group-label">이름</span> <input
									autocomplete="off" class="form-control" type="text"
									placeholder="My Name" id="username"
									onkeypress="return checkEnter(this, event);" value="${username}" 
									readonly/>
								<span class="input-group-btn">
									<button class="btn svg-btn" autocomplete="off" id="register">
									상담실 입장
									<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-door-open-icon lucide-door-open"><path d="M11 20H2"/><path d="M11 4.562v16.157a1 1 0 0 0 1.242.97L19 20V5.562a2 2 0 0 0-1.515-1.94l-4-1A2 2 0 0 0 11 4.561z"/><path d="M11 4H8a2 2 0 0 0-2 2v14"/><path d="M14 12h.01"/><path d="M22 20h-3"/></svg>
									</button>
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row" align="center" id="exit">
				<button onclick="location.href='/'" class="svg-btn">
				상담실 퇴장
				<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-log-out-icon lucide-log-out"><path d="m16 17 5-5-5-5"/><path d="M21 12H9"/><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/></svg>
				</button>
			</div>
		</div>
<%-- ------------------------------video join end----------------------------------- --%>

		<div class="video-container hide layout" id="videos">
			<%-- 상대 --%>
			<div class="video-main">
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">${opponent }</h3>
					</div>
					<div class="panel-body" id="videoremote1"></div>
				</div>
			</div>

			<aside class="video-sidebar">
				<%-- 제어 --%>
				<div class="video-controls">
					<div class="btn-group btn-group-xs pull-right hide">
						<div class="btn-group btn-group-xs">
							<button id="bitrateset" autocomplete="off"
								class="btn btn-primary dropdown-toggle" data-toggle="dropdown">
								Bandwidth<span class="caret"></span>
							</button>
							<ul id="bitrate" class="dropdown-menu" role="menu">
								<li><a href="#" id="0">No limit</a></li>
								<li><a href="#" id="128">Cap to 128kbit</a></li>
								<li><a href="#" id="256">Cap to 256kbit</a></li>
								<li><a href="#" id="512">Cap to 512kbit</a></li>
								<li><a href="#" id="1024">Cap to 1mbit</a></li>
								<li><a href="#" id="1500">Cap to 1.5mbit</a></li>
								<li><a href="#" id="2000">Cap to 2mbit</a></li>
							</ul>
						</div>
					</div>
					<div class="controls">
						<c:if test="${who eq 'counselor' }">
							<button id="rec-start" class="svg-btn">
							<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-circle-play-icon lucide-circle-play"><circle cx="12" cy="12" r="10"/><polygon points="10 8 16 12 10 16 10 8"/></svg>
							녹화 시작
							</button>
							<button id="rec-stop" class="svg-btn">
							<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-circle-stop-icon lucide-circle-stop"><circle cx="12" cy="12" r="10"/><rect x="9" y="9" width="6" height="6" rx="1"/></svg>
							녹화 종료
							</button>
						</c:if>
						<button id="whiteboardBtn" class="svg-btn" onclick="showWhiteboardMode()">
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-palette-icon lucide-palette"><path d="M12 22a1 1 0 0 1 0-20 10 9 0 0 1 10 9 5 5 0 0 1-5 5h-2.25a1.75 1.75 0 0 0-1.4 2.8l.3.4a1.75 1.75 0 0 1-1.4 2.8z"/><circle cx="13.5" cy="6.5" r=".5" fill="currentColor"/><circle cx="17.5" cy="10.5" r=".5" fill="currentColor"/><circle cx="6.5" cy="12.5" r=".5" fill="currentColor"/><circle cx="8.5" cy="7.5" r=".5" fill="currentColor"/></svg>
						화이트보드
						</button>
						<button id="mute" class="svg-btn">
							<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-mic-icon lucide-mic"><path d="M12 19v3"/><path d="M19 10v2a7 7 0 0 1-14 0v-2"/><rect x="9" y="2" width="6" height="13" rx="3"/></svg>
							마이크 끄기
						</button>
						<button id="cam-off" class="svg-btn">
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-video-icon lucide-video"><path d="m16 13 5.223 3.482a.5.5 0 0 0 .777-.416V7.87a.5.5 0 0 0-.752-.432L16 10.5"/><rect x="2" y="6" width="14" height="12" rx="2"/></svg>
						카메라 끄기
						</button>
						<button id="cam-on" class="svg-btn">
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-video-off-icon lucide-video-off"><path d="M10.66 6H14a2 2 0 0 1 2 2v2.5l5.248-3.062A.5.5 0 0 1 22 7.87v8.196"/><path d="M16 16a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h2"/><path d="m2 2 20 20"/></svg>
						카메라 켜기
						</button>
						<label for="volumeSlider">Volume:</label> <input type="range"
							id="volumeSlider" min="0" max="1.2" step="0.01" value="0.8" />
						<button autocomplete="off" id="start" class="svg-btn">시작</button>
					</div>
				</div>

				<div id="chat-ui">
					<div id="chat-window"></div>
					<div id="chat-input-area">
						<textarea id="chat-input" rows="1" placeholder="메시지 입력"
							style="resize: none; overflow-y: auto;"></textarea>
						<button id="chat-send" onclick="sendChatMessage()">전송</button>
					</div>
				</div>


				<%-- 나 --%>
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">${username }</h3>
					</div>
					<div class="panel-body" id="videolocal"></div>
				</div>
			</aside>
		</div>

		<div id="whiteboard-mode" class="layout hidden">
			<div class="left-section">
				<div class="whiteboard">
					<canvas id="whiteboard-canvas"></canvas>
				</div>
				<div class="toolbar">
				<div class="tool-group">
					<button onclick="usePen()" class="svg-btn">
					<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-brush-icon lucide-brush"><path d="m11 10 3 3"/><path d="M6.5 21A3.5 3.5 0 1 0 3 17.5a2.62 2.62 0 0 1-.708 1.792A1 1 0 0 0 3 21z"/><path d="M9.969 17.031 21.378 5.624a1 1 0 0 0-3.002-3.002L6.967 14.031"/></svg>
					그리기
					</button>
					<button onclick="useEraser()" class="svg-btn">
					<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-eraser-icon lucide-eraser"><path d="M21 21H8a2 2 0 0 1-1.42-.587l-3.994-3.999a2 2 0 0 1 0-2.828l10-10a2 2 0 0 1 2.829 0l5.999 6a2 2 0 0 1 0 2.828L12.834 21"/><path d="m5.082 11.09 8.828 8.828"/></svg>
					지우개
					</button>
				</div>
				<div class="tool-group">
					<button onclick="useSelector()" class="svg-btn">
					<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-square-dashed-mouse-pointer-icon lucide-square-dashed-mouse-pointer"><path d="M12.034 12.681a.498.498 0 0 1 .647-.647l9 3.5a.5.5 0 0 1-.033.943l-3.444 1.068a1 1 0 0 0-.66.66l-1.067 3.443a.5.5 0 0 1-.943.033z"/><path d="M5 3a2 2 0 0 0-2 2"/><path d="M19 3a2 2 0 0 1 2 2"/><path d="M5 21a2 2 0 0 1-2-2"/><path d="M9 3h1"/><path d="M9 21h2"/><path d="M14 3h1"/><path d="M3 9v1"/><path d="M21 9v2"/><path d="M3 14v1"/></svg>
					선택
					</button>
					<button onclick="deleteSelected()" class="svg-btn">
					<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-square-x-icon lucide-square-x"><rect width="18" height="18" x="3" y="3" rx="2" ry="2"/><path d="m15 9-6 6"/><path d="m9 9 6 6"/></svg>
					선택 삭제
					</button>
					<button onclick="clearCanvas()" id="clearButton" class="svg-btn">
					<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-trash2-icon lucide-trash-2"><path d="M10 11v6"/><path d="M14 11v6"/><path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6"/><path d="M3 6h18"/><path d="M8 6V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"/></svg>
					전체 지우기
					</button>
				</div>
				<div class="tool-group">
					<button onclick="saveCheckpoint()" class="svg-btn">
					<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-save-icon lucide-save"><path d="M15.2 3a2 2 0 0 1 1.4.6l3.8 3.8a2 2 0 0 1 .6 1.4V19a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2z"/><path d="M17 21v-7a1 1 0 0 0-1-1H8a1 1 0 0 0-1 1v7"/><path d="M7 3v4a1 1 0 0 0 1 1h7"/></svg>
					중간 저장
					</button>
					<button onclick="rollback()" class="svg-btn">
					<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-history-icon lucide-history"><path d="M3 12a9 9 0 1 0 9-9 9.75 9.75 0 0 0-6.74 2.74L3 8"/><path d="M3 3v5h5"/><path d="M12 7v5l4 2"/></svg>
					되돌리기
					</button>
					<button onclick="saveCanvasAsImage()" class="svg-btn">
					<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-download-icon lucide-download"><path d="M12 15V3"/><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><path d="m7 10 5 5 5-5"/></svg>
					다운로드
					</button>
				</div>
				<div class="tool-group">
					<label> 색상: <input type="color" id="colorPicker"
						value="#000000">
					</label>
					<label> 배경색: <input type="color" id="bgColorPicker"
						value="#ffffff" onchange="changeBackgroundColor(this.value)">
					</label>
					<label> 굵기: <input type="range" id="brushWidth" min="1"
						max="30" value="3">
					</label>
				</div>
				</div>

			</div>
			<div class="right-section">
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">${opponet }</h3>
					</div>
					<div class="panel-body-sm" id="videoremote1"></div>
				</div>
				<div class="controls">
					<button id="backToVideo" class="svg-btn">
					<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-tv-icon lucide-tv"><path d="m17 2-5 5-5-5"/><rect width="20" height="15" x="2" y="7" rx="2"/></svg>
					영상 모드로
					</button>
				</div>
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">${username }</h3>
					</div>
					<div class="panel-body" id="videolocal"></div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="/js/VolumeControl.js"></script>
	<script type="text/javascript" src="/js/roomlayout.js"></script>
	<script type="text/javascript" src="/js/whiteboard.js"></script>
	<script type="text/javascript" src="/js/textmsg.js"></script>
	<script type="text/javascript" src="/js/cameratest.js"></script>
	<script type="text/javascript" src="/js/mediaRecorder.js"></script>
</body>
</html>