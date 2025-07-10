<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>🎈 풍선 스트레스 해소 게임</title>
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
  text-align: center;
  padding: 20px;
  width: 100%;
  max-width: 700px;
  box-sizing: border-box;
}

h2 {
  margin-top: 10px;
  margin-bottom: 10px;
  font-size: 22px;
}

    .game-box {
      width: 400px;
      height: 280px;
      border: 4px solid #007bff;
      border-radius: 12px;
      background: white;
      position: relative;
      overflow: hidden;
      margin: 20px auto;
      box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
    }

    .balloon {
      position: absolute;
      font-size: 40px;
      animation: floatUp 3s linear forwards;
      cursor: pointer;
      transition: opacity 0.3s;
    }

    @keyframes floatUp {
      0%   { bottom: -60px; opacity: 1; }
      100% { bottom: 100%; opacity: 0; }
    }

    .pop-effect {
      position: absolute;
      font-size: 24px;
      color: red;
      font-weight: bold;
      animation: popFade 0.6s ease-out;
      pointer-events: none;
      z-index: 9999;
    }

    @keyframes popFade {
      0% { transform: scale(1); opacity: 1; }
      100% { transform: scale(2); opacity: 0; }
    }

    .timer-bar-container {
  width: 100%;
  max-width: 400px;
  height: 16px;
  background-color: #ddd;
  border-radius: 10px;
  overflow: hidden;
  margin: 10px auto;
}

    .timer-bar-fill {
      height: 100%;
      width: 100%;
      background-color: #007bff;
      transition: width 1s linear;
    }

    .button-group {
      display: flex;
      gap: 10px;
      justify-content: center;
      margin-top: 10px;
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

    #score-board {
      margin-top: 15px;
      font-size: 20px;
      font-weight: bold;
    }

    #result-message {
      margin-top: 10px;
      font-size: 18px;
      color: #555;
    }
  </style>
</head>
<body>

  <div class="game-container">
    <h2>🎈 스트레스 해소 풍선 게임 🎯</h2>

    <div class="button-group">
      <button id="start-btn">게임 시작</button>
      <button id="restart-btn" style="display: none;">다시하기</button>
    </div>

    <div id="score-board">점수: 0</div>

    <div class="timer-bar-container">
      <div class="timer-bar-fill" id="timer-bar"></div>
    </div>

    <div id="result-message"></div>
    <div class="game-box" id="game-area"></div>
  </div>

  <script>
    document.addEventListener("DOMContentLoaded", function () {
      let score = 0;
      let gameInterval;
      let gameTimeout;
      let timerInterval;
      let gameRunning = false;
      let activeBalloons = [];

      const gameArea = document.getElementById("game-area");
      const scoreBoard = document.getElementById("score-board");
      const resultMessage = document.getElementById("result-message");
      const startBtn = document.getElementById("start-btn");
      const restartBtn = document.getElementById("restart-btn");
      const timerBar = document.getElementById("timer-bar");

      function createBalloon() {
        if (!gameRunning) return;

        const balloon = document.createElement("div");
        balloon.className = "balloon";
        balloon.textContent = "🎈";
        balloon.style.left = Math.random() * 90 + "%";
        balloon.style.bottom = "-60px";

        balloon.onclick = (e) => {
          score++;
          scoreBoard.textContent = "점수: " + score;
          showPopEffect(e.clientX, e.clientY);
          balloon.remove();
          activeBalloons = activeBalloons.filter(b => b !== balloon);
        };

        gameArea.appendChild(balloon);
        activeBalloons.push(balloon);

        setTimeout(() => {
          balloon.remove();
          activeBalloons = activeBalloons.filter(b => b !== balloon);
        }, 3000);
      }

      function showPopEffect(x, y) {
    	  const pop = document.createElement("div");
    	  pop.className = "pop-effect";
    	  pop.textContent = "팡!";

    	  // 🎯 gameArea 기준 좌표 계산
    	  const rect = gameArea.getBoundingClientRect();
    	  const offsetX = x - rect.left;
    	  const offsetY = y - rect.top;

    	  pop.style.left = offsetX - 20 + "px";
    	  pop.style.top = offsetY - 20 + "px";

    	  // 📌 gameArea에 append 하도록 변경
    	  gameArea.appendChild(pop);

    	  setTimeout(() => pop.remove(), 600);
    	}

      function startGame() {
        gameRunning = true;
        score = 0;
        scoreBoard.textContent = "점수: 0";
        resultMessage.textContent = "";
        gameArea.innerHTML = "";
        activeBalloons = [];
        startBtn.disabled = true;
        restartBtn.style.display = "none";
        timerBar.style.width = "100%";

        gameInterval = setInterval(createBalloon, 300);
        let timeLeft = 30;

        timerInterval = setInterval(() => {
          timeLeft--;
          timerBar.style.width = (timeLeft / 30 * 100) + "%";
          if (timeLeft <= 0) clearInterval(timerInterval);
        }, 1000);

        gameTimeout = setTimeout(() => endGame(), 30000);
      }

      function endGame() {
        clearInterval(gameInterval);
        clearInterval(timerInterval);
        gameRunning = false;

        activeBalloons.forEach(b => b.remove());
        activeBalloons = [];

        resultMessage.textContent = "⏰ 게임 종료! 최종 점수: " + score + "점 🎉";
        startBtn.disabled = false;
        restartBtn.style.display = "inline-block";
        timerBar.style.width = "0%";
      }

      startBtn.addEventListener("click", startGame);
      restartBtn.addEventListener("click", () => {
        restartBtn.style.display = "none";
        startGame();
      });
    });
  </script>

</body>
</html>
