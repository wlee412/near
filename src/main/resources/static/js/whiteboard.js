let savedState = null;
window.canvas = null;
let currentMode = null;
let currentBgColor = '#ffffff';
let currentBrushColor = '#000000';
const container = document.querySelector('.left-section');
var socket = new SockJS('/ws');
var stompClient = Stomp.over(socket);
let reconnectAttempts = 0;
const MAX_RECONNECTS = 5;
stompClient.debug = () => { };

document.addEventListener('DOMContentLoaded', function() {
	connectStomp();
	// 캔버스 초기화
	const canvas = new fabric.Canvas('whiteboard-canvas', {
		isDrawingMode: true,
		enableRetinaScaling: true
	});
	window.canvas = canvas;

	// 캔버스 리사이즈 (백스토어 포함)
	const resizeCanvas = () => {
		canvas.setDimensions({
			width: container.clientWidth,
			height: container.clientHeight
		});
	};

	const wbmode = document.querySelector('#whiteboard-mode');
	const mo = new MutationObserver(muts => {
		muts.forEach(m => {
			if (m.attributeName === 'class'
				&& !wbmode.classList.contains('hidden')) {
				// 보이게 될 때 한 번만 사이즈 재설정
				resizeCanvas();
				mo.disconnect();
			}
		});
	});
	mo.observe(wbmode, { attributes: true, attributeFilter: ['class'] });

	// 기본 브러시 설정
	canvas.freeDrawingBrush = new fabric.PencilBrush(canvas);
	canvas.freeDrawingBrush.color = currentBrushColor;
	canvas.freeDrawingBrush.width = 3;
	usePen();

	// 배경 초기화
	canvas.setBackgroundColor(currentBgColor, canvas.renderAll.bind(canvas));

	// 1) 자유 그리기 이벤트
	canvas.on('path:created', e => {
		const path = e.path;
		path.id = path.id || crypto.randomUUID();
		const minimalPayload = {
			id: path.id,
			type: path.type,
			path: path.path,
			stroke: path.stroke,
			strokeWidth: path.strokeWidth,
			fill: null
		};
		sendUpdate("draw", minimalPayload);
	});

	// 2) 객체 추가
	//	canvas.on('object:added', e => {
	//		if (e.target._sync) return;
	//		sendUpdate('add', e.target.toObject(['id']));
	//	});

	// 3) 객체 수정
	canvas.on('object:modified', e => {
		sendUpdate('modify', e.target.toObject(['id']));
	});

	// 4) 객체 삭제
	//	canvas.on('object:removed', e => {
	//		console.log("remove fired", {
	//			_sync: e.target._sync,
	//			isClearing
	//		});
	//
	//		if (e.target._sync || isClearing) return;
	//		sendUpdate('remove', { id: e.target.id });
	//	});


	// 5) 캔버스 초기화 버튼
	document.getElementById('clearButton').addEventListener('click', () => {
		canvas.clear();
		sendUpdate('clear', null);
		canvas.setBackgroundColor(currentBgColor, canvas.renderAll.bind(canvas));
	});


	window.addEventListener('resize', resizeCanvas);
	const target = document.querySelector('#whiteboard-mode');
	new MutationObserver(() => {
		if (!target.classList.contains('hidden'))
			requestAnimationFrame(resizeCanvas);
	}).observe(target, { attributes: true, attributeFilter: ['class'] });

	// UI 컨트롤 바인딩
	document.getElementById('penButton').addEventListener('click', usePen);
	document.getElementById('eraserButton').addEventListener('click', useEraser);
	document.getElementById('selectorButton').addEventListener('click', useSelector);

});

// 공통 sendUpdate 함수
function sendUpdate(type, payload = {}) {
	const compressedPayload = LZString.compressToBase64(JSON.stringify(payload));
	const msg = { type, roomId, sender: iAm, payload: compressedPayload };
	stompClient.send(`/app/whiteboard/${roomId}`, {}, JSON.stringify(msg));
}


// 모드 전환 함수
function setMode(mode) {
	currentMode = mode;
	const pencilCursor = "url('/cursor/cursor_pencil.png') 0 0, auto";
	const eraserCursor = "url('/cursor/cursor_eraser.png') 5 5, auto";
	let cursor;
	switch (mode) {
		case 'draw':
			canvas.isDrawingMode = true;
			cursor = pencilCursor;
			break;
		case 'erase':
			canvas.isDrawingMode = true;
			cursor = eraserCursor;
			break;
		case 'select':
			canvas.isDrawingMode = false;
			cursor = 'default';
			break;
	}
	canvas.freeDrawingCursor = cursor;
	canvas.upperCanvasEl.style.cursor = cursor;
	canvas.defaultCursor = cursor;
	canvas.hoverCursor = cursor;
	canvas.moveCursor = cursor;
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
	if (!obj) return;

	if (obj.type === 'activeSelection') {
		obj.forEachObject(o => {
			canvas.remove(o);
			sendUpdate('remove', { id: o.id });
		});
	} else {
		canvas.remove(obj);
		sendUpdate('remove', { id: obj.id });
	}

	canvas.discardActiveObject();
	canvas.requestRenderAll();
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

function connectStomp() {
	// STOMP 연결 및 메시지 구독
	stompClient.connect({}, function(frame) {
		console.log('STOMP 연결 성공:', frame);
		stompClient.subscribe(`/topic/whiteboard/${roomId}`, frame => {
			try {
				const msg = JSON.parse(frame.body);
				if (msg.sender === iAm) return;
				const json = LZString.decompressFromBase64(msg.payload);
				const payload = JSON.parse(json);
				
				switch (msg.type.trim()) {
					case 'draw':
					//case 'add':
					case 'modify':
						fabric.util.enlivenObjects([payload], objs => {
							const obj = objs[0];
							obj._sync = true;
							const existing = canvas.getObjects().find(o => o.id === obj.id);
							if (existing) existing.setOptions(payload);
							else canvas.add(obj);
							canvas.renderAll();
						});
						break;
					case 'remove':
						const toRemove = canvas.getObjects().find(o => o.id === payload.id);
						if (toRemove) canvas.remove(toRemove);
						break;
					case 'clear':
						canvas.getObjects().forEach(obj => obj._sync = true);
						canvas.clear();
						canvas.setBackgroundColor(currentBgColor, canvas.renderAll.bind(canvas));
						break;
					case 'bg':
						currentBgColor = payload.color;
						canvas.setBackgroundColor(currentBgColor, canvas.renderAll.bind(canvas));
						break;
				}
			} catch (e) {
				console.error("압축 해제 실패:", e);
			}

		});
		window.textmsgSubscribe();
	}, err => {
		console.error('STOMP Error', err);
		retryReconnect();
	});
}

function retryReconnect() {
	if (reconnectAttempts >= MAX_RECONNECTS) {
		alert("서버와의 연결이 지속적으로 실패했습니다.");
		return;
	}
	reconnectAttempts++;

	setTimeout(() => {
		console.log(`STOMP 재연결 시도 (${reconnectAttempts}/${MAX_RECONNECTS})`);
		connectStomp();
	}, 2000); // 2초 후 재시도
}

// 굵기
document.getElementById('brushWidth').addEventListener('input', e => {
	const w = parseInt(e.target.value, 10);
	canvas.freeDrawingBrush.width = w;
});

// 브러시 색상 변경
document.getElementById('colorPicker').addEventListener('change', e => {
	currentBrushColor = e.target.value;
	canvas.freeDrawingBrush.color = currentBrushColor;
});

// Delete 키로 선택 객체 삭제
document.addEventListener('keydown', function(e) {
	if (e.key === 'Delete') deleteSelected();
});

// 배경색 변경 및 동기화
document.getElementById('bgColorPicker').addEventListener('change', e => {
	currentBgColor = e.target.value;
	canvas.setBackgroundColor(currentBgColor, canvas.renderAll.bind(canvas));
	sendUpdate('bg', { color: currentBgColor });
});


