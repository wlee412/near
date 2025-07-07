let savedState = null;
window.fabricCanvas = null;
let currentBgColor = '#ffffff';

document.addEventListener('DOMContentLoaded', function() {
	// 캔버스 초기화
	const canvas = new fabric.Canvas('whiteboard-canvas', { isDrawingMode: true });
	window.fabricCanvas = canvas;

	// 캔버스 크기 조절 함수
	const resizeCanvas = () => {
		const left = document.querySelector('.left-section');
		canvas.setDimensions({
			width: left.clientWidth,
			height: left.clientHeight
		});
	};
	// 초기 사이즈 및 리사이즈 이벤트
	resizeCanvas();
	window.addEventListener('resize', resizeCanvas);
	const target = document.querySelector('#whiteboard-mode');
	new MutationObserver(() => {
		if (!target.classList.contains('hidden')) requestAnimationFrame(resizeCanvas);
	}).observe(target, { attributes: true, attributeFilter: ['class'] });

	// 초기 브러시 설정
	canvas.freeDrawingBrush.color = '#000000';
	canvas.freeDrawingBrush.width = 3;

	// 모드 전환 및 커서 업데이트
	window.setMode = function(mode) {
		const pencilCursor = "url('/cursor/cursor_pencil.png') 0 0, auto";
		const eraserCursor = "url('/cursor/cursor_eraser.png') 5 5, auto";

		switch (mode) {
			case 'draw':
				canvas.isDrawingMode = true;
				canvas.freeDrawingCursor = pencilCursor;
				canvas.defaultCursor = pencilCursor;
				canvas.hoverCursor = pencilCursor;
				canvas.moveCursor = pencilCursor;
				canvas.upperCanvasEl.style.cursor = pencilCursor;
				break;
			case 'erase':
				canvas.isDrawingMode = true;
				canvas.freeDrawingCursor = eraserCursor;
				canvas.defaultCursor = eraserCursor;
				canvas.hoverCursor = eraserCursor;
				canvas.moveCursor = eraserCursor;
				canvas.upperCanvasEl.style.cursor = eraserCursor;
				break;
			case 'select':
				canvas.isDrawingMode = false;
				canvas.freeDrawingCursor = 'default';
				canvas.defaultCursor = 'default';
				canvas.hoverCursor = 'move';
				canvas.moveCursor = 'move';
				canvas.upperCanvasEl.style.cursor = 'default';
			default:
		}

	};

	// UI 컨트롤 바인딩
	document.getElementById('penButton').addEventListener('click', usePen);
	document.getElementById('eraserButton').addEventListener('click', useEraser);
	document.getElementById('clearButton').addEventListener('click', clearCanvas);
	document.getElementById('colorPicker').addEventListener('change', e => {
		canvas.freeDrawingBrush.color = e.target.value;
	});
	document.getElementById('brushWidth').addEventListener('input', e => {
		canvas.freeDrawingBrush.width = parseInt(e.target.value, 10);
	});
	document.addEventListener('keydown', function(e) {
		if (e.key === 'Delete') {
			deleteSelected();
		}
	});

	// 웹소켓용 이벤트 핸들러
	canvas.on('path:created', function(e) {
		sendUpdate("draw", e.path.toObject());
	});
	canvas.on('object:modified', function(e) {
		sendUpdate("modify", e.target.toObject());
	});
	canvas.on('object:added', function(e) {
		sendUpdate("add", e.target.toObject());
	});
	canvas.on('object:removed', function(e) {
		sendUpdate("remove", e.target.toObject());
	});
	function sendUpdate(type, payload) {
		socket.send(JSON.stringify({
			type: type,
			payload: payload
		}));
	}
});

// 펜 모드 활성화 함수
function usePen() {
	setMode('draw');
	fabricCanvas.freeDrawingBrush = new fabric.PencilBrush(fabricCanvas);
	fabricCanvas.freeDrawingBrush.color = document.getElementById('colorPicker').value;
	fabricCanvas.freeDrawingBrush.width = parseInt(document.getElementById('brushWidth').value, 10);
}

// 지우개 모드 활성화 함수
function useEraser() {
	setMode('erase');
	fabricCanvas.freeDrawingBrush = new fabric.PencilBrush(fabricCanvas);
	fabricCanvas.freeDrawingBrush.color = currentBgColor;
	fabricCanvas.freeDrawingBrush.width = 30;
}

// 전체 지우기 함수
function clearCanvas() {
	fabricCanvas.clear();
	fabricCanvas.setBackgroundColor(currentBgColor, fabricCanvas.renderAll.bind(fabricCanvas));
}

// 캔버스 저장 및 복원
function saveCheckpoint() {
	savedState = fabricCanvas.toJSON();
	alert('캔버스 상태가 저장되었습니다!');
}

function rollback() {
	if (savedState) {
		fabricCanvas.loadFromJSON(savedState, () => fabricCanvas.renderAll());
	}
}

// 선택된 객체 삭제 함수
function deleteSelected() {
	const obj = fabricCanvas.getActiveObject();
	if (obj) {
		if (obj.type === 'activeSelection') obj.forEachObject(o => fabricCanvas.remove(o));
		else fabricCanvas.remove(obj);
		fabricCanvas.discardActiveObject();
		fabricCanvas.requestRenderAll();
	}
}

// 배경색
function changeBackgroundColor(color) {
	currentBgColor = color;
	fabricCanvas.setBackgroundColor(color, fabricCanvas.renderAll.bind(fabricCanvas));
}

// 그림 다운로드
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


const socket = new SockJS("/ws");
const stompClient = Stomp.over(socket);

stompClient.connect({}, function() {
	stompClient.subscribe("/topic/whiteboard", function(message) {
		const msg = JSON.parse(message.body);
		if (msg.type === "draw") {
			fabric.util.enlivenObjects([msg.payload], function(objects) {
				objects.forEach(obj => canvas.add(obj));
				canvas.renderAll();
			});
		}
	});
});