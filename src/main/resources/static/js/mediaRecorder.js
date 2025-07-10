let mediaRecorder;
let recordedChunks = [];
const $recStart = $("#rec-start");
const $recStop = $("#rec-stop");

$(function() {
	$recStart.show();
	$recStop.hide();

	$recStart.click(function() {
		if (remotestream != null)
			startRecording(remotestream);
		else
			alert("녹화 대상이 없습니다.");
	});
	$recStop.click(function() {
		stopRecording();
	});
});

// Janus에서 받은 remote stream 사용
function startRecording(stream) {
	recordedChunks = [];

	mediaRecorder = new MediaRecorder(stream, {
		mimeType: "video/webm;codecs=vp8" // 대부분의 브라우저에서 지원
	});

	mediaRecorder.ondataavailable = event => {
		if (event.data.size > 0) {
			recordedChunks.push(event.data);
		}
	};

	mediaRecorder.onstop = () => {
		const blob = new Blob(recordedChunks, { type: "video/webm" });
		uploadToServer(blob);
	};

	mediaRecorder.start();
	$recStart.hide();
	$recStop.show();
}

function stopRecording() {
	if (mediaRecorder && mediaRecorder.state !== "inactive") {
		mediaRecorder.stop();
		$recStart.show();
		$recStop.hide();
	}
}
window.stopRec = startRecording;


function uploadToServer(blob) {
	const formData = new FormData();
	formData.append("file", blob, `recording_${Date.now()}.webm`);
	formData.append("roomId", roomId);
	fetch("/chat/rec", {
		method: "POST",
		body: formData
	})
		.then(res => res.text())
//		.then(msg => console.log("업로드 완료:", msg))
		.catch(err => console.error("업로드 실패:", err));
}
