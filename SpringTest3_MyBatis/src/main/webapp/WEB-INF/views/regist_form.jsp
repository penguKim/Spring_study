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
		<h1>학생정보등록</h1>
		<form action="registStudentPro" method="post">
			<table border="1">
				<tr>
					<th>이름</th>
					<td><input type="text" name="name"></td>
				</tr>
				<tr>
					<th>E-Mail</th>
					<td><input type="text" name="email"></td>
				</tr>
				<tr>
					<th>학년</th>
					<td><input type="text" name="grade"></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="등록"></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>












