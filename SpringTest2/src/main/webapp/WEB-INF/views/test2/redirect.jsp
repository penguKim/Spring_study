<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
	article {
		text-align: center;
	}
</style>
</head>
<body>
	<header>
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<hr>
	<article>
		<h1>test2/redirect.jsp</h1>
		<h3> 이름 : ${param.name}</h3>
		<h3> 나이 : ${param.age}</h3>
	</article>
</body>
</html>