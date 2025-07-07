<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<title>회원정보 수정</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/client.css">
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

</head>
<body>
<!-- 헤더 영역 -->
<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true"/>
	<!-- ✅ 마이페이지 wrapper 안에 타이틀 포함 -->
	<div class="mypage-wrapper">

		<!-- ✅사이드바 -->
		<%
		String uri = request.getRequestURI();
		%>
		<div class="mypage-sidebar">
			<h1 class="mypage-title">
				<a href="${pageContext.request.contextPath}/client/mypage"
					style="text-decoration: none; color: inherit;">마이페이지</a>
			</h1>

			<div class="mypage-divider"></div>

			<div
				class="menu-item <%= uri.contains("mypageProfile") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/client/mypageProfile'">프로필</div>

			<div
				class="menu-item <%= uri.contains("mypageUpdate") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/client/mypageUpdate'">정보수정</div>

			<div
				class="menu-item <%= uri.contains("mypagePassword") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/client/mypagePassword'">비밀번호
				변경</div>

			<div
				class="menu-item <%= uri.contains("mypageDelete") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/client/mypageDelete'">회원탈퇴</div>
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

			<form id="updateForm" action="/client/update" method="post"
				onsubmit="return validateUpdateForm();">

				<!-- 아이디 -->
				<div class="form-group">
					<label>아이디</label> <input type="text" name="clientId"
						value="${client.clientId}" readonly>
				</div>

				<!-- 이름 -->
				<div class="form-group">
					<label>이름</label> <input type="text" id="name" name="name"
						value="${client.name}">
					<div id="nameCheckResult" class="result-text"></div>
				</div>

				<!-- 휴대폰 -->
				<div class="form-group">
					<label>휴대폰 번호</label> <input type="tel" id="phone" name="phone"
						maxlength="11" value="${client.phone}">
					<div id="phoneCheckResult" class="result-text"></div>
				</div>

				<!-- 이메일 -->
				<div class="form-group">
					<label>이메일</label>
					<div class="input-row">
						<input type="text" id="emailId" name="emailId" placeholder="이메일 아이디" readonly value="${client.emailId}" required>
						<span>@</span>

						<!-- 직접입력 input -->
						<input type="text" id="customEmailDomain" name="emailDomain" placeholder="직접입력" readonly
							style="${client.emailDomain != 'gmail.com' && client.emailDomain != 'naver.com' && client.emailDomain != 'daum.net' ? 'display:inline-block;' : 'display:none;'}"
							value="${client.emailDomain}"
							${client.emailDomain != 'gmail.com' && client.emailDomain != 'naver.com' && client.emailDomain != 'daum.net' ? 'name="emailDomain"' : 'disabled'}>

						<!-- select 도메인 -->
						<select id="emailDomainSelect" 
							${client.emailDomain == 'gmail.com' || client.emailDomain == 'naver.com' || client.emailDomain == 'daum.net' ? 'name="emailDomain"' : ''}
							name="emailDomain" onchange="handleDomainChange()" required>
							<option value="">도메인 선택</option>
							<option value="gmail.com"
								${client.emailDomain == 'gmail.com' ? 'selected' : ''}>gmail.com</option>
							<option value="naver.com"
								${client.emailDomain == 'naver.com' ? 'selected' : ''}>naver.com</option>
							<option value="daum.net"
								${client.emailDomain == 'daum.net' ? 'selected' : ''}>daum.net</option>
							<option value="custom"
								${client.emailDomain != 'gmail.com' && client.emailDomain != 'naver.com' && client.emailDomain != 'daum.net' ? 'selected' : ''}>직접입력</option>
						</select>
					</div>
					<div id="emailCheckResult" class="result-text"></div>
				</div>
				
				<!-- 주소 -->
				<div class="form-group">
				    <label>주소</label>
				    <div class="input-row">
				        <input type="text" name="zipcode" id="postcode" placeholder="우편번호" readonly style="width: 150px;" value="${client.zipcode}" /> <button type="button" onclick="execDaumPostcode()" class="btn">주소 찾기</button>
				    </div>
				    <input type="text" id="roadAddress" placeholder="도로명 주소" name="addrBase" readonly required style="margin-top: 8px;" value="${client.addrBase}" />
				    <input type="text" id="detailAddress" placeholder="상세 주소 입력" name="addrDetail" required style="margin-top: 8px;" value="${client.addrDetail}" />
				</div>


				
				<!-- 생년월일 -->
				<div class="form-group">
					<label>생년월일</label> <input type="date" name="birth" id="birth"
						value="${client.birth}">
				</div>

			    <!-- 성별 -->
				<c:choose>
				  <c:when test="${client != null and client.gender eq 'N'}">
				    <div class="form-group">
				      <label>성별</label>
				      <div class="radio-group">
				        <label>
				          <input type="radio" name="gender" value="M"
				            <c:if test="${client.gender eq 'M'}">checked</c:if>> 남자
				        </label>
				        <label>
				          <input type="radio" name="gender" value="F"
				            <c:if test="${client.gender eq 'F'}">checked</c:if>> 여자
				        </label>
				      </div>
				    </div>
				  </c:when>
				  <c:otherwise>
				    <div class="form-group">
				      <label>성별</label>
				      <div class="radio-group">
				        <label>
				          <input type="radio" name="gender" value="M"
				            <c:if test="${client.gender eq 'M'}">checked</c:if> disabled> 남자
				        </label>
				        <label>
				          <input type="radio" name="gender" value="F"
				            <c:if test="${client.gender eq 'F'}">checked</c:if> disabled> 여자
				        </label>
				      </div>
				    </div>
				  </c:otherwise>
			    </c:choose>

				<!-- 관심사 -->
				<div class="form-group">
				    <label>관심사 (복수 선택 가능)</label>
				    <div class="checkbox-group">
				        <label><input type="checkbox" name="interestList" value="우울감" <c:if test="${interestList.contains('우울감')}">checked</c:if>/>우울감</label> 
				        <label><input type="checkbox" name="interestList" value="불안" <c:if test="${interestList.contains('불안')}">checked</c:if>/>불안</label>
				        <label><input type="checkbox" name="interestList" value="자존감" <c:if test="${interestList.contains('자존감')}">checked</c:if>/>자존감</label> 
				        <label><input type="checkbox" name="interestList" value="수면 문제" <c:if test="${interestList.contains('수면 문제')}">checked</c:if>/>수면 문제</label>
				        <label><input type="checkbox" name="interestList" value="스트레스" <c:if test="${interestList.contains('스트레스')}">checked</c:if>/>스트레스</label>
				        <label><input type="checkbox" name="interestList" value="공황/불안발작" <c:if test="${interestList.contains('공황/불안발작')}">checked</c:if>/>공황/불안발작</label>
				        <label><input type="checkbox" name="interestList" value="대인관계" <c:if test="${interestList.contains('대인관계')}">checked</c:if>/>대인관계</label> 
				        <label><input type="checkbox" name="interestList" value="섭식 문제" <c:if test="${interestList.contains('섭식 문제')}">checked</c:if>/>섭식 문제</label>
				        <label><input type="checkbox" name="interestList" value="ADHD" <c:if test="${interestList.contains('ADHD')}">checked</c:if>/>ADHD</label> 
				        <label><input type="checkbox" name="interestList" value="자기통제" <c:if test="${interestList.contains('자기통제')}">checked</c:if>/>자기통제</label>
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
						onclick="location.href='/client/mypageProfile'">취소</button>
				</div>
			</form>
		</div>
	</div>

	<script src="${pageContext.request.contextPath}/js/mypageUpdate.js" defer></script>
	
	<!-- 푸터 영역 -->

</body>
<%-- 	<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true"/> --%>

</html>
