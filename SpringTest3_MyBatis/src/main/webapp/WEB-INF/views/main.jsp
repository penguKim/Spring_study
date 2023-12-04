<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div align="center">
		<h1>메인페이지</h1>
		<hr>
		<h3><a href="registStudent">학생정보등록</a></h3>
		<h3><a href="studentList">학생목록조회</a></h3>
		<hr>
		<form action="studentInfo" method="get">
			<input type="text" name="idx" placeholder="검색할 번호">
			<input type="submit" value="학생정보조회">
		</form>
		<hr>
		<form action="removeStudent" method="post">
			<input type="text" name="name" placeholder="검색할 이름"><br>
			<input type="text" name="email" placeholder="검색할 이메일"><br>
			<input type="submit" value="학생정보삭제">
		</form>
	</div>
</body>
</html>