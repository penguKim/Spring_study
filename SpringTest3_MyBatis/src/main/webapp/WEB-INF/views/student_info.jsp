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
		<h1>학생 상세정보</h1>
		<table border="1">
			<tr>
				<th>번호</th>
				<td>${student.idx }</td>
			</tr>
			<tr>
				<th>이름</th>
				<td>${student.name }</td>
			</tr>
			<tr>
				<th>E-Mail</th>
				<td>${student.email }</td>
			</tr>
			<tr>
				<th>학년</th>
				<td>${student.grade }</td>
			</tr>
		</table>
		<hr>
		<form action="editStudent" method="post">
			<input type="hidden" name="idx" value="${student.idx}">
			<input type="text" name="name" placeholder="수정할 이름"><br>
			<input type="text" name="email" placeholder="수정할 이메일"><br>
			<input type="text" name="grade" placeholder="수정할 학년"><br>
			<input type="submit" value="학생정보수정">
		</form>
	</div>
</body>
</html>












