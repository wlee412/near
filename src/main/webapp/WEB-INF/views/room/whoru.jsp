<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<form method="post" action="/room/video" id="autoForm">
	<input type="hidden" name="roomId" value="${roomId}" />
	<input type="hidden" name="who" value="${who}" />
</form>
<script>
  window.onload = () => document.getElementById("autoForm").submit();
</script>