<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<header>
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<hr>
	<article>
		<h1>Model&View</h1>
		<%-- 전달받은 Map 객체(속성명 : "map") 내의 PersonVO 객체(키 : "person") 접근하여 데이터 출력 --%>
		<h3>이름 : ${map.person.name }</h3>
		<h3>나이 : ${map.person.age }</h3>
		<%-- 전달받은 Map 객체(속성명 : "map") 내의 TestVO 객체(키 : "test") 접근하여 데이터 출력 --%>
		<h3>제목 : ${map.test.subject }</h3>
		<h3>내용 : ${map.test.content }</h3>
	</article>
</body>
</html>