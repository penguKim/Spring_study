<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<div align="center">
		<h1>학생목록조회</h1>
		<table border="1">
			<tr>
				<th>번호</th>
				<th>이름</th>
				<th>E-Mail</th>
				<th>학년</th>
			</tr>
			<c:choose>
				<c:when test="${empty studentList}">
					<tr>
						<td colspan="4">학생 정보가 존재하지 않습니다.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<%-- 학생 목록 정보가 저장된 List<StudentVO> 객체(studentList) 활용하여 목록 출력 --%>
					<c:forEach var="student" items="${studentList}">
						<tr>
							<td>${student.idx}</td>
							<td>${student.name}</td>
							<td>${student.email}</td>
							<td>${student.grade}</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</table>
	</div>
</body>
</html>