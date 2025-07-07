document.addEventListener('DOMContentLoaded', function() {
	const canvas = new fabric.Canvas('whiteboard-canvas', {
		isDrawingMode: true  // 기본적으로 그리기 모드 활성화
	});

	// 예시: 펜 두께 및 색상 설정
	canvas.freeDrawingBrush.color = "#000000"; // 검정색
	canvas.freeDrawingBrush.width = 3;

	// 필요 시 외부에서 canvas에 접근 가능하도록 window에 저장
	window.fabricCanvas = canvas;
});