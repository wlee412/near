<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>감정 타이핑 내리기 게임</title>
  <style>
body {
  background: #f0f8ff;
  margin: 0;
  padding: 0;
  font-family: 'Noto Sans KR', sans-serif;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  height: 100%;
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

.button-group {
  display: flex;
  gap: 10px;
  justify-content: center;
  margin-bottom: 10px;
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
  margin: 8px 0;
  font-size: 18px;
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

.game-box {
  width: 100%;
  max-width: 400px;
  height: 280px;
  border: 4px solid #007bff;
  border-radius: 12px;
  background: white;
  position: relative;
  overflow: hidden;
  margin: 10px auto;
  box-shadow: 0 0 12px rgba(0, 0, 0, 0.1);
}

.word {
  position: absolute;
  font-size: 16px;
  font-weight: bold;
  color: #ff5555;
}

#typing-input {
  margin-top: 10px;
  padding: 10px;
  width: 90%;
  max-width: 400px;
  font-size: 16px;
}
  </style>
</head>
<body>
  <div class="game-container">
    <h2>⌨️ 감정 타자 게임 ⚡</h2>

    <div class="button-group">
      <button id="start-btn">게임 시작</button>
      <button id="restart-btn" style="display: none;">다시하기</button>
    </div>

    <div id="score-board"> 점수: 0</div>
    <div class="timer-bar-container">
      <div class="timer-bar-fill" id="timer-bar"></div>
    </div>

    <div class="game-box" id="game-box"></div>
    <input type="text" id="typing-input" placeholder="감정 단어를 입력하세요" disabled>
  </div>

  <script>
    const words = [
      "행복", "기쁨", "감사", "사랑", "희망", "자신감", "설렘", "평온", "편안함",
      "슬픔", "분노", "우울", "불안", "스트레스", "걱정", "외로움", "짜증", "무기력", "불만"
    ];

    let score = 0;
    let timeLeft = 30;
    let fallSpeed = 1.5;
    let wordInterval;
    let gameInterval;
    let wordId = 0;

    const gameBox = document.getElementById("game-box");
    const input = document.getElementById("typing-input");
    const scoreBoard = document.getElementById("score-board");
    const timerBar = document.getElementById("timer-bar");
    const startBtn = document.getElementById("start-btn");
    const restartBtn = document.getElementById("restart-btn");

    function createWord() {
      const word = document.createElement("div");
      const id = "word-" + wordId++;
      const text = words[Math.floor(Math.random() * words.length)];
      word.className = "word";
      word.id = id;
      word.textContent = text;
      word.style.left = Math.random() * 90 + "%";
      word.style.top = "0px";
      gameBox.appendChild(word);

      function fall() {
        if (!document.getElementById(id)) return;
        let top = parseFloat(word.style.top);
        top += fallSpeed;
        word.style.top = top + "px";
        if (top < 500) {
          requestAnimationFrame(fall);
        } else {
          word.remove();
        }
      }
      fall();
    }

    input.addEventListener("keydown", function (e) {
      if (e.key === "Enter") {
        const typed = input.value.trim();
        const allWords = document.querySelectorAll(".word");
        for (let w of allWords) {
          if (w.textContent === typed) {
            score++;
            scoreBoard.textContent = "점수: " + score;
            w.remove();
            break;
          }
        }
        input.value = "";
      }
    });

    function updateTimer() {
      timeLeft--;
      const percentage = (timeLeft / 30) * 100;
      timerBar.style.width = percentage + "%";
      if (timeLeft === 20) fallSpeed = 2.5;
      if (timeLeft === 10) fallSpeed = 3.5;
      if (timeLeft <= 0) {
        clearInterval(wordInterval);
        clearInterval(gameInterval);
        input.disabled = true;
        timerBar.style.width = "0%";
        restartBtn.style.display = "inline-block";
      }
    }

    function startGame() {
      score = 0;
      timeLeft = 30;
      fallSpeed = 1.5;
      wordId = 0;
      gameBox.innerHTML = "";
      input.value = "";
      input.disabled = false;
      input.focus();
      scoreBoard.textContent = "점수: 0";
      timerBar.style.width = "100%";
      restartBtn.style.display = "none";

      wordInterval = setInterval(createWord, 1000);
      gameInterval = setInterval(updateTimer, 1000);
    }

    startBtn.addEventListener("click", startGame);
    restartBtn.addEventListener("click", startGame);
  </script>
</body>
</html>
