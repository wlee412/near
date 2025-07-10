let previewStream;
const $camStart = $('#camera-test-start');
const $camStop = $('#camera-test-stop');

$(function() {
	$camStart.show();
	$camStop.hide();

	$('#register').click(stopCameraTest);

});

function startCameraTest() {
	navigator.mediaDevices.getUserMedia({ video: true, audio: true })
		.then(stream => {
			previewStream = stream;
			const video = document.getElementById('camera-test-video');
			video.srcObject = stream;
			$camStart.hide();
			$camStop.show();
		})
		.catch(err => {
			alert("카메라 또는 마이크를 사용할 수 없습니다: " + err.message);
			$camStart.show();
			$camStop.hide();
		});
}

function stopCameraTest() {
	if (previewStream) {
		previewStream.getTracks().forEach(track => track.stop());
	}
	$camStart.show();
	$camStop.hide();
}