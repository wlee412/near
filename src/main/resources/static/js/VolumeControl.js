// 볼륨 조절 슬라이더
const slider = document.getElementById("volumeSlider");
if (slider) {
	slider.addEventListener("input", function(e) {
		const vol = parseFloat(e.target.value);
		console.log("볼륨: " + vol);
	});

function setupAudioVolumeControl(stream) {
	const audioContext = new (window.AudioContext || window.webkitAudioContext)();

	const source = audioContext.createMediaStreamSource(stream);
	const gainNode = audioContext.createGain();
	gainNode.gain.value = 0.8;

	source.connect(gainNode).connect(audioContext.destination);
}