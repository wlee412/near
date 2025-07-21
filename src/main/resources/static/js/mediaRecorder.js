let mediaRecorder;
let recordedChunks = [];
let mixedStream;
const $recStart = $("#rec-start");
const $recStop = $("#rec-stop");

$(function() {
	$recStart.show();
	$recStop.hide();

	$recStart.click(function() {
		if (remotestream != null)
			startRecording();
		else
			alert("녹화 대상이 없습니다.");
	});
	$recStop.click(function() {
		stopRecording();
	});
});

function startRecording() {
	const stream = createMixedStream(remotestream, mystream);
	recordedChunks = [];

	mediaRecorder = new MediaRecorder(stream, {
		mimeType: "video/webm;codecs=vp9",
		videoBitsPerSecond: 600_000, // 600kbps
		audioBitsPerSecond: 64_000   // 64kbps
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
	sendSysMessage("녹화가 시작되었습니다.");
	$recStart.hide();
	$recStop.show();
}

function stopRecording() {
	if (mediaRecorder && mediaRecorder.state !== "inactive") {
		mediaRecorder.stop();
		sendSysMessage("녹화가 종료되었습니다.");
		$recStart.show();
		$recStop.hide();
	}
}
window.stopRec = stopRecording;


function uploadToServer(blob) {
	const formData = new FormData();
	formData.append("file", blob, `recording_${Date.now()}.webm`);
	formData.append("roomId", roomId);
	fetch("/vid/rec", {
		method: "POST",
		body: formData
	})
		.then(res => res.text())
		.catch(err => console.error("업로드 실패:", err));
}

function createMixedStream(remoteStream, mystream) {
	mixedStream = new MediaStream();

	// 1. remote video + audio track 추가
	remoteStream.getTracks().forEach(track => mixedStream.addTrack(track));

	// 2. local audio track 추가
	mystream.getAudioTracks().forEach(track => mixedStream.addTrack(track));

	return mixedStream;
}

function sendSysMessage(payload) {
	const msg = { type: "sys", roomId, sender: "시스템", payload };
	stompClient.send(`/app/textmsg/${roomId}`, {}, JSON.stringify(msg));
}
