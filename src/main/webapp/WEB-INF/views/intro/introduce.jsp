<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>n:ear 소개</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/introduce.css">
</head>
<body>

	<div class="wrapper">
		<%@ include file="../includes/header.jsp"%>

		<!-- 비동기 탭 메뉴 -->
		<div class="tab-nav">
			<button class="tab-button active" data-tab="tab-about">사이트
				소개</button>
			<button class="tab-button" data-tab="tab-counselors">상담사 프로필</button>
			<button class="tab-button" data-tab="tab-guide">이용안내</button>
		</div>

		<!-- 탭 콘텐츠 영역 -->
		<div class="tab-content-wrapper">
			<!-- 탭 1: 사이트 소개 -->
			<div class="tab-content active" id="tab-about">
				<h2>n:ear 소개</h2>
				<p>
					n:ear는 ' near ' + ' ear ' 의 합성어로<br> “가까이 귀 기울이는 누군가”를 의미하는<br>
					<span style="color: #5a909b; font-weight: bold;">실시간 화상상담
						플랫폼</span>입니다.<br> <br> 혼자 감당하기 어려운 감정이 밀려올 때, <br> 누구에게도
					말하지 못한 고민이 마음을 짓누를 때, <br> n:ear는 당신의 이야기에 조용히 귀를 기울입니다. <br>
				</p>
				<br>
			</div>

			<!-- 탭 2: 상담사 프로필 -->
			<div class="tab-content" id="tab-counselors">
				<h2>상담사 프로필</h2>
				<div class="counselor-list vertical-profile">
					<div class="counselor-card-horizontal">
						<img src="/images/counselor01.webp" alt="상담사1">
						<div class="counselor-info">
							<h3>이병철 상담사</h3>
							<p class="subtitle">정신건강 임상심리사 / 1급 상담심리사</p>
							<p>
								<strong>[학력]</strong><br>서울마음대학교 심리학과 석사
							</p>
							<p>
								<strong>[약력]</strong><br>마음숲심리상담센터 수석상담사(前)<br>위로정신건강센터
								전임상담사(現)
							</p>
							<p>
								<strong>[전문분야]</strong><br>우울, 불안, 대인관계, 자존감 회복
							</p>
						</div>
					</div>

					<div class="counselor-card-horizontal">
						<img src="/images/counselor02.webp" alt="상담사2">
						<div class="counselor-info">
							<h3>신하령 상담사</h3>
							<p class="subtitle">청소년 상담사 / 놀이치료사</p>
							 <p><strong>[학력]</strong><br>한마음교육대학교 아동상담 전공</p>
   							 <p><strong>[약력]</strong><br>온기심리센터 놀이치료실장(前)<br>햇살상담소 청소년상담팀(現)</p>
  						     <p><strong>[전문분야]</strong><br>아동 정서조절, 청소년 진로, 부모코칭</p>
						</div>
					</div>

					<div class="counselor-card-horizontal">
						<img src="/images/counselor03.webp" alt="상담사3">
						<div class="counselor-info">
							<h3>전미희 상담사</h3>
							<p class="subtitle">부부상담 / 중독상담 전문가</p>
							<p><strong>[학력]</strong><br>휴먼상담대학원 부부·중독상담 전공</p>
   							<p><strong>[약력]</strong><br>다온가족치료센터 센터장(前)<br>온누리심리상담센터 부부상담팀장(現)</p>
    						<p><strong>[전문분야]</strong><br>부부갈등, 정체성, 게임·알코올 중독</p>
						</div>
					</div>
				</div>
			</div>


			<!-- 탭 3: 이용안내 -->
			<div class="tab-content" id="tab-guide">
				<h2>이용안내</h2>
				<div class="step-cards">
					<div class="step-card">
						<h4>Step 1.</h4>
						<p>상담예약 선택</p>
					</div>
					<div class="step-card">
						<h4>Step 2.</h4>
						<p>상담사 선택</p>
					</div>
					<div class="step-card">
						<h4>Step 3.</h4>
						<p>시간 선택하기</p>
					</div>
					<div class="step-card">
						<h4>Step 4.</h4>
						<p>화상상담시작</p>
					</div>
				</div>
			</div>
		</div>

		<!-- 비동기 탭 전환 스크립트 -->
		<script>
document.addEventListener("DOMContentLoaded", () => {
  const tabButtons = document.querySelectorAll(".tab-button");
  const tabContents = document.querySelectorAll(".tab-content");

  tabButtons.forEach(button => {
    button.addEventListener("click", () => {
      tabButtons.forEach(btn => btn.classList.remove("active"));
      button.classList.add("active");

      const target = button.dataset.tab;
      tabContents.forEach(content => {
        content.classList.remove("active");
        if (content.id === target) content.classList.add("active");
      });
    });
  });
});
</script>

	</div>
	<%@ include file="../includes/footer.jsp"%>

</body>
</html>