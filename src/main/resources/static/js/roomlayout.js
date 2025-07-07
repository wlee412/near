document.getElementById("whiteboardBtn").addEventListener("click", function() {
	document.getElementById("videos").classList.add("hide");
	document.getElementById("whiteboard-mode").classList.remove("hidden");

	// DOM 이동 (비디오 스트림 유지)
	document.querySelector(".bottom-section #videolocal").appendChild(document.getElementById("myvideo"));
	document.querySelector(".right-top #videoremote1").appendChild(document.getElementById("remotevideo1"));
});

document.getElementById("backToVideo").addEventListener("click", function() {
	document.getElementById("whiteboard-mode").classList.add("hidden");
	document.getElementById("videos").classList.remove("hide");

	// 다시 원래 위치로 이동
	document.querySelector("#videolocal").appendChild(document.getElementById("myvideo"));
	document.querySelector("#videoremote1").appendChild(document.getElementById("remotevideo1"));
});
