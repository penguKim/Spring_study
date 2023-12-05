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
		<h1>test2/push.jsp</h1>
		<%-- 디스패치 방식 포워딩 시 함께 전달된 request 객체의 msg 속성값 가져와서 출력하기 --%>
		<%-- Model 객체를 통해 데이터 저장시에도 사용 시점에서는 request 객체 그대로 사용 --%>
		<h3>msg 속성값 : <%=request.getAttribute("msg") %></h3>
		<h3>msg 속성값 : ${msg}</h3>
		<hr>
		<%-- 디스패치 방식 포워딩 시 함께 전달된 request 객체의 test 속성값 가져와서 멤버변수값 출력하기 --%>
		<%-- Model 객체를 통해 데이터 저장시에도 사용 시점에서는 request 객체 그대로 사용 --%>
		<h3>test 속성값(객체)의 제목 : ${test.subject}</h3>
		<h3>test 속성값(객체)의 내용 : ${test.content}</h3>
	</article>
</body>
</html>