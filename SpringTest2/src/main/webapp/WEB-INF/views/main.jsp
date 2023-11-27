<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<%-- 현재 문서에 포함시킬 JSP 파일도 WEB-INF 디렉토리 내의 views 디렉토리 내에 위치해야함 --%>
	<%-- 상대 경로로 접근 --%>
	<jsp:include page="inc/top.jsp"></jsp:include>
	<h1>main.jsp</h1>
	<hr>
	<form action="test2" method="get">
		<input type="submit" value="test2 서블릿 요청(GET)">
	</form>
</body>
</html>