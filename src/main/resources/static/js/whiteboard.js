let savedState = null;
window.canvas = null;
let currentMode = null;
let currentBgColor = '#ffffff';
let currentBrushColor = '#000';
const socket = new SockJS("/ws");
const stompClient = Stomp.over(socket);
stompClient.debug = () => { };

document.addEventListener('DOMContentLoaded', function() {
	// 캔버스 초기화
	const canvas = new fabric.Canvas('whiteboard-canvas', {
		isDrawingMode: true,
		enableRetinaScaling: false
	});
	window.canvas = canvas;
	canvas.freeDrawingBrush = new fabric.PencilBrush(canvas);
	canvas.freeDrawingBrush.color = currentBrushColor;
	canvas.freeDrawingBrush.width = 3;
	usePen();

	console.log('isDrawingMode:', canvas.isDrawingMode);  // true 여야 함

	// 1) 자유 그리기
	canvas.on('path:created', e => {
		const path = e.path;
		path.id = crypto.randomUUID();
		const data = path.toObject(['id']);
		sendUpdate('draw', data);
	});

	// 2) 객체 추가 (canvas.add 호출 시)
	canvas.on('object:added', e => {
		if (e.target._sync) return;
		sendUpdate('add', e.target.toObject(['id']));
	});

	// 3) 객체 수정 (이동, 크기, 회전 등)
	canvas.on('object:modified', e => {
		sendUpdate('modify', e.target.toObject(['id']));
	});

	// 4) 객체 삭제
	canvas.on('object:removed', e => {
		if (e.target._sync || isClearing) return;
		sendUpdate('remove', { id: e.target.id });
	});

	let isClearing = false;
	// 5) 캔버스 초기화
	clearButton.addEventListener('click', () => {
		isClearing = true;
		canvas.clear();
		sendUpdate('clear', null);
		isClearing = false;
	});

	// 캔버스 크기 조절 함수
	const resizeCanvas = () => {
		const left = document.querySelector('.left-section');
		canvas.setDimensions({
			width: left.clientWidth,
			height: left.clientHeight
		}, { cssOnly: true });
	};
	// 초기 사이즈 및 리사이즈 이벤트
	resizeCanvas();
	window.addEventListener('resize', resizeCanvas);
	const target = document.querySelector('#whiteboard-mode');
	new MutationObserver(() => {
		if (!target.classList.contains('hidden')) requestAnimationFrame(resizeCanvas);
	}).observe(target, { attributes: true, attributeFilter: ['class'] });

	// UI 컨트롤 바인딩
	document.getElementById('penButton').addEventListener('click', usePen);
	document.getElementById('eraserButton').addEventListener('click', useEraser);
	document.getElementById('selectorButton').addEventListener('click', useSelector);
	document.getElementById('clearButton').addEventListener('click', clearCanvas);
	document.getElementById('colorPicker').addEventListener('change', e => {
		currentBrushColor = e.target.value;
		usePen();
	});
	document.getElementById('brushWidth').addEventListener('input', e => {
		canvas.freeDrawingBrush.width = parseInt(e.target.value, 10);
		switch (currentMode) {
			case 'draw':
				usePen();
				break;
			case 'erase':
				useEraser();
				break;
		}
	});
	document.addEventListener('keydown', function(e) {
		if (e.key === 'Delete') {
			deleteSelected();
		}
	});

});

// 공통 sendUpdate 함수  
function sendUpdate(type, payload) {
	const msg = {
		type,                 // 'draw' | 'add' | 'modify' | 'remove' | 'clear'
		roomId: roomId,
		sender: iAm,
		payload
	};
	stompClient.send('/app/whiteboard', {}, JSON.stringify(msg));
}

// 모드 전환 및 커서 업데이트
window.setMode = function(mode) {
	currentMode = mode;
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

// 펜 모드 활성화 함수
function usePen() {
	setMode('draw');
	canvas.freeDrawingBrush.color = currentBrushColor;
}

// 지우개 모드 활성화 함수
function useEraser() {
	setMode('erase');
	canvas.freeDrawingBrush.color = currentBgColor;
}

function useSelector() {
	setMode('select');
}

// 전체 지우기 함수
function clearCanvas() {
	canvas.clear();
	canvas.setBackgroundColor(currentBgColor, canvas.renderAll.bind(canvas));
}

// 캔버스 저장 및 복원
function saveCheckpoint() {
	savedState = canvas.toJSON();
	alert('캔버스 상태가 저장되었습니다!');
}

function rollback() {
	if (savedState) {
		canvas.loadFromJSON(savedState, () => canvas.renderAll());
	}
}

// 선택된 객체 삭제 함수
function deleteSelected() {
	const obj = canvas.getActiveObject();
	if (obj) {
		if (obj.type === 'activeSelection') obj.forEachObject(o => canvas.remove(o));
		else canvas.remove(obj);
		canvas.discardActiveObject();
		canvas.requestRenderAll();
	}
}

// 배경색
function changeBackgroundColor(color) {
	currentBgColor = color;
	canvas.setBackgroundColor(color, canvas.renderAll.bind(canvas));
}

// 그림 다운로드
function saveCanvasAsImage() {
	const dataURL = canvas.toDataURL({
		format: 'png',
		multiplier: 2
	});
	const link = document.createElement('a');
	link.href = dataURL;
	link.download = 'whiteboard.png';
	link.click();
}

stompClient.connect({}, function(frame) {
	console.log('STOMP 연결 성공:', frame);
	stompClient.subscribe(`/topic/whiteboard/${roomId}`, frame => {
		const msg = JSON.parse(frame.body);
		if (msg.sender === iAm) return;

		switch (msg.type) {
			case 'draw':
			case 'add':
			case 'modify':
				fabric.util.enlivenObjects([msg.payload], objs => {
					const obj = objs[0];
					obj._sync = true;         // 무한 루프 방지용 플래그
					const existing = canvas.getObjects().find(o => o.id === obj.id);
					if (existing) {
						// 수정
						existing.setOptions(obj);
					} else {
						// 새로 추가
						canvas.add(obj);
					}
					canvas.renderAll();
				});
				break;
			case 'remove':
				const toRemove = canvas.getObjects().find(o => o.id === msg.payload.id);
				if (toRemove) canvas.remove(toRemove);
				break;
			case 'clear':
				// 기존 객체 모두 확실히 제거
				canvas.getObjects().slice().forEach(o => canvas.remove(o));
				// 선택된 객체도 초기화
				canvas.discardActiveObject();
				// 배경색 복원
				canvas.setBackgroundColor(currentBgColor, () => {
					canvas.renderAll();
				});
				break;
		}
	});
}, err => console.error('STOMP Error', err));