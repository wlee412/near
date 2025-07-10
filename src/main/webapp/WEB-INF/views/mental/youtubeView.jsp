<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 유튜브 영상 모달 -->
<div id="youtubeModal" class="modal" style="display: none;">
    <div class="modal-content">
        <span class="close" onclick="closeYoutubeModal()">&times;</span>

        <!-- 기분 선택 버튼 그룹 -->
        <div id="mood-buttons" style="text-align: center; margin-bottom: 20px;">
            <p><strong>오늘 기분을 선택해보세요</strong></p>
            <button onclick="selectMood('depressed')">😔 우울함</button>
            <button onclick="selectMood('tired')">😵 피곤함</button>
            <button onclick="selectMood('exhausted')">😩 지침</button>
            <button onclick="selectMood('happy')">😊 행복함</button>
            <button onclick="selectMood('excited')">😆 신남</button>
        </div>

        <!-- 추천 키워드 영역 -->
        <div id="keyword-box" style="text-align: center; margin-bottom: 10px;"></div>

        <!-- YouTube 영상 삽입 영역 -->
        <div id="youtubeContainer" style="display: flex; flex-wrap: wrap; gap: 20px; justify-content: center;">
            <!-- YouTube iframe이 여기에 삽입됨 -->
        </div>
    </div>
</div>
