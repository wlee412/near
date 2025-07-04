<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<script type="text/javascript" src="/js/janus.js"></script>
<script type="text/javascript" src="/js/videoroom.js"></script>
<script>
	$(function() {
		$(".navbar-static-top").load(
				"navbar.html",
				function() {
					$(".navbar-static-top li.dropdown").addClass("active");
					$(".navbar-static-top a[href='videoroomtest.html']")
							.parent().addClass("active");
				});
		$(".footer").load("footer.html");
	});
</script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/bootswatch/3.4.0/cerulean/bootstrap.min.css"
	type="text/css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/2.1.4/toastr.min.css" />
<style>
table {
	width: 100%;
	border-top: 1px solid #bdbcbc;
	border-collapse: collapse;
}

th, td {
	border-bottom: 1px solid #bdbcbc;
	padding: 10px;
}
</style>
<link rel="stylesheet" href="/css/room.css" type="text/css" />
</head>

<body>
	<div class="container">
		<div class="row">
			<div class="col-md-12">
				<div class="page-header">
					<h1>
						상담
						<button class="btn btn-default" autocomplete="off" id="start">Start</button>
					</h1>
				</div>
				<div class="container hide" id="videojoin">
					<div class="row">
						<div class="col-md-12" id="controls">
							<div id="registernow">
								<span class="label label-info" id="room"></span>
								<div class="input-group margin-bottom-md "
									style="width: 100% !important;">
									<input autocomplete="off" class="form-control" type="text"
										id="roomname" value="${janusNum}" readonly />
								</div>
								<span class="label label-info" id="you"></span>
								<div class="input-group margin-bottom-md ">
									<span class="input-group-addon">대화명</span> <input
										autocomplete="off" class="form-control" type="text"
										placeholder="My Name" id="username"
										onkeypress="return checkEnter(this, event);"
										value="${username}" /> <span class="input-group-btn">
										<button class="btn btn-success" autocomplete="off"
											id="register">대화방 참여</button>
									</span>
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="video-container hide" id="videos">
					<!-- 상대 -->
					<div class="video-main">
						<div class="panel">
							<div class="panel-heading">
								<h3 class="panel-title">상대</h3>
							</div>
							<div class="panel-body" id="videoremote1"></div>
						</div>
					</div>

					<aside class="video-sidebar">
						<!-- 제어 -->
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
							<button id="btn-mic">마이크</button>
							<button id="btn-cam">카메라</button>
							<button id="btn-end">종료</button>
						</div>

						<!-- 나 -->
						<div class="panel">
							<div class="panel-heading">
								<h3 class="panel-title">나</h3>
							</div>
							<div class="panel-body" id="videolocal"></div>
						</div>
					</aside>

				</div>
			</div>
		</div>
		<hr>
		<div class="footer"></div>
	</div>
</body>
</html>