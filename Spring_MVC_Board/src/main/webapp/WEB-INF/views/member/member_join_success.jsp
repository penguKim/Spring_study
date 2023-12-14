<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<!-- 외부 CSS 파일(css/default.css) 연결하기 -->
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
</head>
<body>
	<header>
		<!-- inc/top.jsp 페이지 삽입 -->
		<!-- JSP 파일 삽입 대상은 현재 파일을 기준으로 상대주소 지정 -->
		<!-- webapp 디렉토리를 가리키려면 최상위(루트) 경로 활용 -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<article>
		<h1>회원 가입 성공</h1>
		<h3>회원 가입을 축하드립니다!
			3000 포인트가 적립되었습니다.</h3>
	</article>
	<footer>
	
	</footer>
</body>
</html>