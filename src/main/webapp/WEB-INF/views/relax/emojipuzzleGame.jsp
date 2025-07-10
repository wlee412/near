<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>감정 짝 맞추기 퍼즐</title>
  <style>
    body {
  background: #f0f8ff;
  margin: 0;
  padding: 0;
  font-family: 'Noto Sans KR', sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.game-container {
  width: 100%;
  max-width: 700px; /* 모달 내에서 적절히 보이도록 제한 */
  margin: 0 auto;
  text-align: center;
  padding: 20px 0;
}

h2 {
  margin-top: 10px;
  margin-bottom: 10px;
  font-size: 22px;
}

.game-board {
  display: grid;
  grid-template-columns: repeat(6, 50px);
  gap: 10px;
  justify-content: center;
  margin: 20px auto;
}

.card {
  width: 50px;
  height: 50px;
  font-size: 24px;
  line-height: 50px;
  background-color: #007bff;
  color: transparent;
  text-align: center;
  overflow: hidden;
  border-radius: 6px;
  display: flex;
  justify-content: center;
  align-items: center;
  user-select: none;
  outline: none;
  transition: background-color 0.3s, color 0.3s;
}

.card.flipped,
.card.matched {
  background-color: white;
  color: black;
  border: 2px solid #007bff;
}

.info {
  margin-top: 10px;
  font-size: 16px;
}

.button-group {
  margin-top: 15px;
  display: flex;
  justify-content: center;
  gap: 10px;
}

#start-btn, #restart-btn {
  padding: 8px 20px;
  font-size: 16px;
  cursor: pointer;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 6px;
}

#start-btn:hover, #restart-btn:hover {
  background-color: #0056b3;
}

#timer {
  visibility: visible !important;
  color: black !important;
}

#result-time {
  margin-top: 10px;
  font-weight: bold;
}
  </style>
</head>
<body>
  <div class="game-container">
    <h2>💞 감정 짝 맞추기 퍼즐 🧩</h2>

    <div class="button-group">
      <button id="start-btn">게임 시작</button>
      <button id="restart-btn" style="display: none;">다시하기</button>
    </div>

    <div class="info">
      <div id="timer">경과 시간: <span id="timer-value">0</span>초</div>
      <div id="matched-count">맞춘 짝: 0 / 15</div>
       <div id="result-time" style="margin-top: 10px; font-weight: bold;"></div>
    </div>

    <div class="game-board" id="game-board"></div>
  </div>

  <script>
  
  document.addEventListener("DOMContentLoaded", function () {
    const emojis = ["😊", "😢", "😠", "😍", "😎", "😱", "😴", "😅", "🤔", "🤯", "🤗", "🥶", "🥳", "🫠", "😇"];
    let cards = [];
    let firstCard = null;
    let secondCard = null;
    let lockBoard = false;
    let matchedCount = 0;
    let timerInterval;
    let elapsedSeconds = 0;
    let finalTime = 0;

    const board = document.getElementById("game-board");
    const timerValue = document.getElementById("timer-value");
    console.log("✅ 타이머 요소 확인:", document.getElementById("timer"));
    const matchedDisplay = document.getElementById("matched-count");
    const startBtn = document.getElementById("start-btn");
    const restartBtn = document.getElementById("restart-btn");

    function shuffle(array) {
      for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
      }
      return array;
    }

    function createBoard() {
      const emojiPairs = shuffle([...emojis, ...emojis]);
      board.innerHTML = "";
      cards = [];
      emojiPairs.forEach((emoji, idx) => {
        const card = document.createElement("div");
        card.className = "card";
        card.dataset.emoji = emoji;
        card.dataset.index = idx;
        card.addEventListener("click", () => handleCardClick(card));
        board.appendChild(card);
        cards.push(card);
      });
    }

    function handleCardClick(card) {
      if (lockBoard || card.classList.contains("flipped") || card.classList.contains("matched")) return;

      card.classList.add("flipped");
      card.textContent = card.dataset.emoji;

      if (!firstCard) {
        firstCard = card;
      } else {
        secondCard = card;
        lockBoard = true;

        if (firstCard.dataset.emoji === secondCard.dataset.emoji) {
          firstCard.classList.add("matched");
          secondCard.classList.add("matched");
          matchedCount++;
          matchedDisplay.textContent = `맞춘 짝: ${matchedCount} / 15`;
          resetTurn();
          if (matchedCount === 15) {
        	  console.log("✅ matchedCount 15 달성!");
        	  clearInterval(timerInterval);
        	  finalTime = elapsedSeconds;
        	  console.log("✔️ 완료 시점 초:", finalTime);
        	  alert("🎉 축하합니다! 완료 시간: " + elapsedSeconds + "초");
        	  restartBtn.style.display = "inline-block";
        	}
        } else {
          setTimeout(() => {
            firstCard.classList.remove("flipped");
            secondCard.classList.remove("flipped");
            firstCard.textContent = "";
            secondCard.textContent = "";
            resetTurn();
          }, 1000);
        }
      }
    }

    function resetTurn() {
      [firstCard, secondCard] = [null, null];
      lockBoard = false;
    }

    function startTimer() {
    	  clearInterval(timerInterval);
    	  elapsedSeconds = 0;
    	  timerValue.textContent = "0"; // 숫자만

    	  console.log("⏱️ 타이머 시작됨");

    	  timerInterval = setInterval(() => {
    	    elapsedSeconds++;
    	    console.log(`⏳ 경과 시간: ${elapsedSeconds}`);
    	    timerValue.textContent = elapsedSeconds; // 숫자만 대입
    	  }, 1000);
    	}

    function startGame() {
    	 console.log("🎮 게임 시작됨"); 
      clearInterval(timerInterval);
      elapsedSeconds = 0;
      timerValue.textContent = "0";
      matchedCount = 0;
      matchedDisplay.textContent = `맞춘 짝: 0 / 15`;
      startBtn.disabled = true;
      restartBtn.style.display = "none";
      createBoard();
      startTimer();
    }

    startBtn.addEventListener("click", startGame);
    restartBtn.addEventListener("click", startGame);
    
  });
  </script>
</body>
</html>