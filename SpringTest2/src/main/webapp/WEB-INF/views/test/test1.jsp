<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>test/test1.jsp</h1>
	<hr>
	<form action="test2" method="get">
		<input type="submit" value="test2 서블릿 요청(GET)">
	</form>
	<form action="test2" method="post">
		<input type="submit" value="test2 서블릿 요청(POST)">
	</form>
</body>
</html>