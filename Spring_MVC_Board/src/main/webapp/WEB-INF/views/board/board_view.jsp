<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MVC 게시판</title>
<script type="text/javascript">
	function confirmDelete() {
		// 삭제 버튼 클릭 시 확인창을 통해 "삭제하시겠습니까?" 출력 후
		// 확인 버튼 클릭 시 "BoardDelete" 서블릿 요청(파라미터 : 글번호, 페이지번호)
		if(confirm('삭제하시겠습니까?')) {
			// EL은 자바스크립트와 별개로 동작하기에  EL문법 사용 가능
			location.href="BoardDelete?board_num=${board.board_num}&pageNum=${param.pageNum }";
		}
	}
</script>
<!-- 외부 CSS 파일(css/default.css) 연결하기 -->
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
<style type="text/css">
	#articleForm {
		width: 500px;
		height: 550px;
		border: 1px solid red;
		margin: auto;
	}
	
	h2 {
		text-align: center;
	}
	
	table {
		border: 1px solid black;
		border-collapse: collapse; 
	 	width: 500px;
	}
	
	th {
		text-align: center;
	}
	
	td {
		width: 150px;
		text-align: center;
	}
	
	#basicInfoArea {
		min-height: 130px;
		text-align: center;
	}
	
	#articleContentArea {
		background: orange;
		margin-top: 20px;
		height: 350px;
		text-align: center;
		overflow: auto;
		white-space: pre-line;
	}
	
	#commandList {
		margin: auto;
		width: 500px;
		text-align: center;
	}
</style>
</head>
<body>
	<header>
		<!-- Login, Join 링크 표시 영역 -->
		<jsp:include page="../inc/top.jsp"></jsp:include>
	</header>
	<!-- 게시판 상세내용 보기 -->
	<article id="articleForm">
		<h2>글 상세내용 보기</h2>
		<section id="basicInfoArea">
			<table border="1">
			<tr><th width="70">제 목</th><td colspan="3" >${board.board_subject }</td></tr>
			<tr>
				<th width="70">작성자</th><td>${board.board_name }</td>
				
				<th width="70">작성일</th>
				<td>
					<fmt:formatDate value="${board.board_date }" 
						pattern="yyyy-MM-dd HH:mm"/>
				</td>
			</tr>
			<tr>
				<th width="70">조회수</th><td>${board.board_readcount }</td>
				<th width="70">작성자IP</th><td>${board.writer_ip }</td>
			</tr>
			<tr>
				<th width="70">파일</th>
				<td colspan="3">
					<%-- 파일명에서 업로드 한 원본 파일명만 추출하기 --%>
					<%-- 1) split() 함수 활용하여 "_" 구분자로 분리하여 1번 인덱스 배열 사용 --%>
<%-- 						${fn:split(board.board_file1, "_")[1]} --%>
<!-- 						<br> -->
					<%-- 2) substringAfter() 함수 활용하여 기준 문자열의 다음 모든 문자열 추출 --%>
<%-- 						${fn:substringAfter(board.board_file2, "_")}<br> --%>
					<%-- 3) substring() 함수 활용하여 시작 인덱스부터 지정한 인덱스까지 문자열 추출 --%>
					<%-- 단, 전체 파일명의 길이를 지정한 인덱스로 활용하기 위해 length() 함수 추가 사용 => 변수에 저장 필요 --%>
<%-- 						<c:set var="file3_length" value="${fn:length(board.board_file3)}" /> --%>
<%-- 						${fn:substring(board.board_file3, 20, file3_length)}<br> --%>
					<%-- =========================================================== --%>
					
					<c:if test="${not empty board.board_file1}">
						<div class="file">
							<c:set var="original_file_name1" value="${fn:substringAfter(board.board_file1, '_')}"/>
							${original_file_name1}
							<%-- 다운로드 버튼을 활용하여 해당 파일 다운로드 --%>
							<%-- 버튼에 하이퍼링크 설정하여 download 속성 설정 시 다운로드 가능 --%>
							<%-- 이 때, download 속성값 지정 시 다운로드 되는 파일명 변경 가능 --%>
							<a href="${pageContext.request.contextPath }/resources/upload/${board.board_file1}" download="${original_file_name1}"><input type="button" value="다운로드"></a><br>
						</div>
					</c:if>
					<c:if test="${not empty board.board_file2}">
						<div class="file">
							<c:set var="original_file_name2" value="${fn:substringAfter(board.board_file2, '_')}"/>
							${original_file_name2}
							<a href="${pageContext.request.contextPath }/resources/upload/${board.board_file2}" download="${original_file_name2}"><input type="button" value="다운로드"></a><br>
						</div>
					</c:if>
					<c:if test="${not empty board.board_file3}">
						<div class="file">
							<c:set var="original_file_name3" value="${fn:substringAfter(board.board_file3, '_')}"/>
							${original_file_name3}
							<a href="${pageContext.request.contextPath }/resources/upload/${board.board_file3}" download="${original_file_name3}"><input type="button" value="다운로드"></a><br>
						</div>
					</c:if>
				</td>
			</tr>
			</table>
		</section>
		<section id="articleContentArea">
			${board.board_content }
		</section>
	</article>
	<section id="commandCell">
	
		<%-- 답변 버튼은 세션 아이디가 있을 경우에만 표시(생략) --%>
<%-- 		<c:if test="${not empty sessionscope.sId }"> --%>
<%-- 			<input type="button" value="답변" onclick="location.href='BoardReplyForm?board_num=${board.board_num}&pageNum=${param.pageNum }'"> --%>
<%-- 		</c:if> --%>


		<%-- 답변과 목록 버튼은 항상 표시 --%>
		<%-- 수정, 삭제 버튼은 세션 아이디가 있고, 작성자 아이디와 세션 아이디가 같을 경우에만 표시 --%>
		<%-- 단, 세션 아이디가 관리자일 경우에조 수정, 삭제 버튼 표시 -->
		<%-- board_num은 request로 전달받은 board 객체와 따로 전달받은 파라미터로도 가능 --%>
		<%-- 답변, 수정, 삭제는 BoardXXXForm 서블릿 요청(파라미터 : 글번호, 페이지번호) --%>
		<%-- 답변 : BoardReplyForm, 수정 : BoardModifyForm, 삭제 : BoardDeleteForm --%>
		<input type="button" value="답변" onclick="location.href='BoardReplyForm?board_num=${board.board_num}&pageNum=${param.pageNum }'">
		<c:if test="${not empty sessionScope.sId and (sessionScope.sId eq board.board_name or sessionScope.sId eq 'admin') }">
			<input type="button" value="수정" onclick="location.href='BoardModifyForm?board_num=${board.board_num}&pageNum=${param.pageNum }'">
	<%-- 		<input type="button" value="삭제" onclick="location.href='BoardDeleteForm?board_num=${board.board_num}&pageNum=${param.pageNum }'"> --%>
			<%-- 삭제 시 패스워드 확인이 불필요하여 뷰페이지가 필요없으므로 --%>
			<%-- 자바스크립트를 통해 삭제 확인 후 바로 비즈니스 로직 요청 --%>
			<%-- c:foreach 등의 반복문 안에서 자바스크립트 함수 내에서 EL로는 접근이 불가능하기에  --%>
			<%-- 함수의 파라미터로 전달하여 사용이 가능하다.--%>
			<input type="button" value="삭제" onclick="confirmDelete()">
		</c:if>
		
		<%-- 목록은 BoardList.bo 서블릿 요청(파라미터 : 페이지번호) --%>
		<input type="button" value="목록" onclick="location.href='BoardList?pageNum=${param.pageNum}'">
	</section>
</body>
</html>
















