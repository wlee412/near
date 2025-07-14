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
							<button id="camera-test-start" onclick="startCameraTest()">카메라 켜기</button>
							<button id="camera-test-stop" onclick="stopCameraTest()">카메라 끄기</button>
						</div>
					</div>
				</div>
				<div class="col-md-6" id="description">
					<div  id="desc-content">
						<h2>심리 상담</h2>
						<p>상담 일시: <fmt:formatDate value="${rsv.startDate }" pattern="yy-M-d E요일 HH:mm"/></p>
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
									onkeypress="return checkEnter(this, event);" value="${username}" />
								<span class="input-group-btn">
									<button class="btn" autocomplete="off" id="register">상담실 입장</button>
								</span>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row" align="center" id="exit">
				<button onclick="location.href='/'">상담실 퇴장</button>
			</div>
		</div>
<%-- ------------------------------video join end----------------------------------- --%>

		<div class="video-container hide layout" id="videos">
			<%-- 상대 --%>
			<div class="video-main">
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">상대</h3>
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
						<button id="rec-start">녹화 시작</button>
						<button id="rec-stop">녹화 종료</button>
						<button id="whiteboardBtn" onclick="showWhiteboardMode()">화이트보드</button>
						<button id="mute">마이크 끄기</button>
						<button id="unpublish">카메라 끄기</button>
						<label for="volumeSlider">Volume:</label> <input type="range"
							id="volumeSlider" min="0" max="1.2" step="0.01" value="0.8" />
						<button autocomplete="off" id="start">시작</button>
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
						<h3 class="panel-title">나</h3>
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
					<button onclick="usePen()">그리기</button>
					<button onclick="useEraser()">지우개</button>
				</div>
				<div class="tool-group">
					<button onclick="useSelector()">선택</button>
					<button onclick="deleteSelected()">선택 삭제</button>
					<button onclick="clearCanvas()" id="clearButton">전체 지우기</button>
				</div>
				<div class="tool-group">
					<button onclick="saveCanvasAsImage()">다운로드</button>
					<button onclick="saveCheckpoint()">중간 저장</button>
					<button onclick="rollback()">롤백</button>
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
						<h3 class="panel-title">상대</h3>
					</div>
					<div class="panel-body-sm" id="videoremote1"></div>
				</div>
				<div class="controls">
					<button id="mute">마이크 끄기</button>
					<button id="unpublish">카메라 끄기</button>
					<button id="backToVideo">영상 모드로</button>
				</div>
				<div class="panel">
					<div class="panel-heading">
						<h3 class="panel-title">나</h3>
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