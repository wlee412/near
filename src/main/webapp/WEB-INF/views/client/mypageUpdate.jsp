<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<title>회원정보 수정</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/member.css">
<link rel="stylesheet" href="<c:url value='/css/header.css' />" />
<link rel="stylesheet" href="<c:url value='/css/footer.css' />" />
</head>
<body>
<!-- 헤더 영역 -->
<jsp:include page="/WEB-INF/views/common/header.jsp" flush="true"/>
	<!-- ✅ 마이페이지 wrapper 안에 타이틀 포함 -->
	<div class="mypage-wrapper">

		<!-- ✅사이드바 -->
		<%
		String uri = request.getRequestURI();
		%>
		<div class="mypage-sidebar">
			<h1 class="mypage-title">
				<a href="${pageContext.request.contextPath}/member/mypage"
					style="text-decoration: none; color: inherit;">마이페이지</a>
			</h1>

			<div class="mypage-divider"></div>

			<div
				class="menu-item <%= uri.contains("mypageProfile") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/member/mypageProfile'">프로필</div>

			<div
				class="menu-item <%= uri.contains("mypageUpdate") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/member/mypageUpdate'">정보수정</div>

			<div
				class="menu-item <%= uri.contains("mypagePassword") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/member/mypagePassword'">비밀번호
				변경</div>

			<div
				class="menu-item <%= uri.contains("mypageDelete") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/member/mypageDelete'">회원탈퇴</div>
		</div>


		<!-- ✅ 콘텐츠 영역 -->
		<div class="mypage-content">
			<h2 class="title">회원정보 수정</h2>
			<div class="divider"></div>

			<c:if test="${not empty message}">
				<script>
					alert("${message}");
				</script>
			</c:if>

			<form id="updateForm" action="/member/update" method="post"
				onsubmit="return validateUpdateForm();">

				<!-- 아이디 -->
				<div class="form-group">
					<label>아이디</label> <input type="text" name="id"
						value="${member.id}" readonly>
				</div>

				<!-- 이름 -->
				<div class="form-group">
					<label>이름</label> <input type="text" id="name" name="name"
						value="${member.name}">
					<div id="nameCheckResult" class="result-text"></div>
				</div>

				<!-- 닉네임 -->
				<div class="form-group">
					<label>닉네임</label> <input type="text" id="nickname" name="nickname"
						value="${member.nickname}">
					<div id="nicknameCheckResult" class="result-text"></div>
				</div>

				<!-- 휴대폰 -->
				<div class="form-group">
					<label>휴대폰 번호</label> <input type="tel" id="phone" name="phone"
						maxlength="11" value="${member.phone}">
					<div id="phoneCheckResult" class="result-text"></div>
				</div>

				<!-- 이메일 -->
				<div class="form-group">
					<label>이메일</label>
					<div class="input-row">
						<input type="text" id="emailId" name="emailId"
							placeholder="이메일 아이디" value="${member.emailId}" required>
						<span>@</span>

						<!-- 직접입력 input -->
						<input type="text" id="customEmailDomain" placeholder="직접입력"
							style="${member.emailDomain != 'gmail.com' && member.emailDomain != 'naver.com' && member.emailDomain != 'daum.net' ? 'display:inline-block;' : 'display:none;'}"
							value="${member.emailDomain}"
							${member.emailDomain != 'gmail.com' && member.emailDomain != 'naver.com' && member.emailDomain != 'daum.net' ? 'name="emailDomain"' : 'disabled'}>

						<!-- select 도메인 -->
						<select id="emailDomainSelect"
							${member.emailDomain == 'gmail.com' || member.emailDomain == 'naver.com' || member.emailDomain == 'daum.net' ? 'name="emailDomain"' : ''}
							onchange="handleDomainChange()" required>
							<option value="">도메인 선택</option>
							<option value="gmail.com"
								${member.emailDomain == 'gmail.com' ? 'selected' : ''}>gmail.com</option>
							<option value="naver.com"
								${member.emailDomain == 'naver.com' ? 'selected' : ''}>naver.com</option>
							<option value="daum.net"
								${member.emailDomain == 'daum.net' ? 'selected' : ''}>daum.net</option>
							<option value="custom"
								${member.emailDomain != 'gmail.com' && member.emailDomain != 'naver.com' && member.emailDomain != 'daum.net' ? 'selected' : ''}>직접입력</option>
						</select>
					</div>
					<div id="emailCheckResult" class="result-text"></div>
				</div>

				<!-- 생년월일 -->
				<div class="form-group">
					<label>생년월일</label> <input type="date" name="birthdate" id="birth"
						value="${member.birthDate}">
				</div>

				<div class="form-group">
					<label>성별</label>
					<div class="radio-group">
						<label><input type="radio" name="gender" value="M"
							${member.gender == 'M' ? 'checked' : ''}> 남자</label> <label><input
							type="radio" name="gender" value="F"
							${member.gender == 'F' ? 'checked' : ''}> 여자</label>
					</div>
				</div>


				<div class="form-group checkbox-group">
					<label><input type="checkbox" name="agreeUpdate" required>
						정보 수정 동의 (필수)</label>
				</div>


				<!-- 버튼 -->
				<div class="form-group button-row">
					<button type="submit" class="btn btn-half">수정하기</button>
					<button type="button" class="btn btn-half"
						onclick="location.href='/member/mypageProfile'">취소</button>
				</div>
			</form>
		</div>
	</div>

	<script src="${pageContext.request.contextPath}/js/member.js" defer></script>
	<script src="<c:url value='/js/proFile.js'/>"></script>
	
	<!-- 푸터 영역 -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" flush="true"/>

</body>
</html>
