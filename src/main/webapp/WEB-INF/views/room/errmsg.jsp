<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>n:ear</title>
</head>
<body>
	<script>
		const path = "${goto}";
		alert("${msg}");
		
		if(path == "")
			location.href="/";
		else
			location.href=path;
	</script>
</body>
</html>