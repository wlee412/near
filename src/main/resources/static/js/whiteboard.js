let savedState = null;

document.addEventListener('DOMContentLoaded', function() {
	const canvas = new fabric.Canvas('whiteboard-canvas', {
		isDrawingMode: true
	});

	// Canvas 크기 조절
	const left = document.querySelector('.left-section');
	const width = left.clientWidth;
	const height = left.clientHeight;

	const canvasEl = document.getElementById('whiteboard-canvas');
	canvasEl.style.width = width + 'px';
	canvasEl.style.height = height + 'px';

	canvas.setDimensions({ width, height });

	const target = document.querySelector('#whiteboard-mode');
	const observer = new MutationObserver(() => {
		if (!target.classList.contains('hidden')) {
			requestAnimationFrame(() => {
				const left = document.querySelector('.left-section');
				canvas.setDimensions({
					width: left.clientWidth,
					height: left.clientHeight
				});
			});
		}
	});
	observer.observe(target, { attributes: true, attributeFilter: ['class'] });

	// 초기 1회 설정
	requestAnimationFrame(() => {
		const left = document.querySelector('.left-section');
		canvas.setDimensions({
			width: left.clientWidth,
			height: left.clientHeight
		});
	});

	// 창 크기 변경 시
	window.addEventListener('resize', () => {
		const left = document.querySelector('.left-section');
		canvas.setDimensions({
			width: left.clientWidth,
			height: left.clientHeight
		});
	});


	// 초기 펜 설정
	canvas.freeDrawingBrush.color = "#000000";
	canvas.freeDrawingBrush.width = 3;

	// 외부 접근용
	window.fabricCanvas = canvas;

	// 모드 전환
	window.setMode = function(mode) {
		canvas.isDrawingMode = (mode === 'draw');
	};

	// 색상 변경
	document.getElementById('colorPicker').addEventListener('change', function(e) {
		canvas.freeDrawingBrush.color = e.target.value;
	});

	// 굵기 조절
	document.getElementById('brushWidth').addEventListener('input', function(e) {
		canvas.freeDrawingBrush.width = parseInt(e.target.value, 10);
	});

	// 전체 지우기
	window.clearCanvas = function() {
		canvas.clear();
	};

});

// 사진으로 다운로드
function saveCanvasAsImage() {
	const dataURL = fabricCanvas.toDataURL({
		format: 'png',
		multiplier: 2
	});

	const link = document.createElement('a');
	link.href = dataURL;
	link.download = 'whiteboard.png';
	link.click();
}

// 배경색
let currentBgColor = '#ffffff';
function changeBackgroundColor(color) {
	currentBgColor = color;
	fabricCanvas.setBackgroundColor(color, fabricCanvas.renderAll.bind(fabricCanvas));
}

// 그리기
function usePen() {
	fabricCanvas.isDrawingMode = true;
	fabricCanvas.freeDrawingBrush = new fabric.PencilBrush(fabricCanvas);
	fabricCanvas.freeDrawingBrush.color = document.getElementById('colorPicker').value;
	fabricCanvas.freeDrawingBrush.width = parseInt(document.getElementById('brushWidth').value, 10);
}

// 지우개
function useEraser() {
	fabricCanvas.isDrawingMode = true;
	fabricCanvas.freeDrawingBrush = new fabric.PencilBrush(fabricCanvas);
	fabricCanvas.freeDrawingBrush.color = currentBgColor;
	fabricCanvas.freeDrawingBrush.width = 30;
}

// 중간 저장
function saveCheckpoint() {
	savedState = fabricCanvas.toJSON();
	alert('캔버스 상태가 저장되었습니다!');
}

// 롤백
function rollback() {
	if (savedState) {
		fabricCanvas.loadFromJSON(savedState, () => {
			fabricCanvas.renderAll();
		});
	} else {
		console.log('저장된 캔버스 없음');
	}
}

// 객체 삭제
function deleteSelected() {
	const obj = fabricCanvas.getActiveObject();

	if (obj) {
		if (obj.type === 'activeSelection') {
			// 다중 선택일 경우
			obj.forEachObject(o => fabricCanvas.remove(o));
		} else {
			// 단일 선택일 경우
			fabricCanvas.remove(obj);
		}
		fabricCanvas.discardActiveObject();
		fabricCanvas.requestRenderAll();
	}
}

function setMode(mode) {
	if (mode === 'draw') {
		canvas.isDrawingMode = true;
		canvas.defaultCursor = 'crosshair';
	} else if (mode === 'select') {
		canvas.isDrawingMode = false;
		canvas.defaultCursor = 'default';
	}
}



