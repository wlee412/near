let previewStream;

onload = function startCameraTest() {
	navigator.mediaDevices.getUserMedia({ video: true, audio: true })
		.then(stream => {
			previewStream = stream;
			const video = document.getElementById('camera-test-video');
			video.srcObject = stream;
			document.getElementById('camera-test-container').classList.remove('hidden');
		})
		.catch(err => {
			alert("카메라 또는 마이크를 사용할 수 없습니다: " + err.message);
		});
}

function stopCameraTest() {
	if (previewStream) {
		previewStream.getTracks().forEach(track => track.stop());
	}
	document.getElementById('camera-test-container').classList.add('hidden');
}

$(function() {
	$('#register').click(stopCameraTest);
});