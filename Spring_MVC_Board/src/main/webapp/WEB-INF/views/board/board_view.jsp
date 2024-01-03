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
<script src="${pageContext.request.contextPath}/resources/js/jquery-3.7.1.js"></script>
<script type="text/javascript">
	function confirmDelete() {
		// 삭제 버튼 클릭 시 확인창을 통해 "삭제하시겠습니까?" 출력 후
		// 확인 버튼 클릭 시 "BoardDelete" 서블릿 요청(파라미터 : 글번호, 페이지번호)
		if(confirm('삭제하시겠습니까?')) {
			// EL은 자바스크립트와 별개로 동작하기에  EL문법 사용 가능
			location.href="BoardDelete?board_num=${board.board_num}&pageNum=${param.pageNum }";
		}
	}
	
	// 대댓글 작성 아이콘 클릭 시
	function reReplyWriteForm(reply_num, reply_re_ref, reply_re_lev, reply_re_seq) {
		console.log(reply_num, reply_re_ref, reply_re_lev, reply_re_seq);
// 		$.ajax({
// 			url: "",
// 			data: {
// 				reply_num:reply_num,
// 				reply_re_ref: reply_re_ref,
// 				reply_re_lev: reply_re_lev,
// 				reply_re_seq: reply_re_seq
// 			},
// 			success: function(reReply) {
				
// 			}
// 		});
	}
	
	// 댓글 삭제 아이콘 클릭 시
	function confirmReplyDelete(reply_num) {
		if(confirm('댓글을 삭제하시겠습니까?')) { // 확인 클릭 시
		// AJAX 활용하여 BoardTinyReplyDelete 서블릿 요청(파라미터 : 댓글번호)
		$.ajax({
			type: "GET",
			url: "BoardTinyReplyDelete",
			data: {
				"reply_num":reply_num
			},
			dataType: "text",
			success: function(result) {
				// 댓글 삭제 요청 결과 판별("true"/"false")
				if(result == "true") {
					// 댓글 삭제 성공 시 해당 댓글의 tr 태그 자체 삭제
					// => replyTr_ 문자열과 댓글번호를 조합하여 id 선택자 지정
					$("#replyTr_" + reply_num).remove();
				} else if(result == "false") {
					alert("댓글 삭제 실패");
				} else if(result == "invalidSession") { // 세션아이디 없을 경우
					alert("권한이 없습니다!");
					return;
				}
			},
			error: function() {
				alert("요청 실패!");
			}
		});
		}
	}
</script>
<!-- 외부 CSS 파일(css/default.css) 연결하기 -->
<link href="${pageContext.request.contextPath }/resources/css/default.css" rel="stylesheet" type="text/css">
<style type="text/css">
	#articleForm {
		width: 500px;
		height: 600px;
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
	/* --------- 댓글 영역 --------- */
	#replyArea {
		width: 500px;
		height: 150px;
		margin: auto;
		margin-top: 20px;
		margin-bottom: 50px;
	}
	
	#replyTextarea {
		width: 400px;
		height: 50px;
		resize: none; /* 사이즈 조절 막기 */
		vertical-align: middle;
	}
	
	#replySubmit {
		width: 85px;
		height: 55px;
		vertical-align: middle;
	}
	
	#replyListArea {
		font-size: 12px;
		margin-top: 20px;
	}
	
	#replyListArea table, tr, td {
		border: none;
	}
	
	.replyContent {
		width: 300px;
		text-align: left;
	}
	
	.replyContent img {
		width: 10px;
		height: 10px;
	}
	
	.replyWriter {
		width: 80px;
	}
	
	.replyDate {
		width: 100px;
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
		
		<%-- 목록은 BoardList 서블릿 요청(파라미터 : 페이지번호) --%>
		<input type="button" value="목록" onclick="location.href='BoardList?pageNum=${param.pageNum}'">
	</section>
	<section id="replyArea">
		<form action="BoardTinyReplyWrite" method="post">
		<input type="hidden" name="board_num" value="${board.board_num }">
		<input type="hidden" name="pageNum" value="${param.pageNum }">
		<%-- 만약, 아이디를 전송해야할 경우 reply_namem 파라미터 포함 --%>
		<%-- 단, 현재는 별도의 닉네임들을 사용하지 않으므로 임시로 세션아이디 전달 --%>
		<%-- (실제로 세션 아이디 사용시에는 컨트롤러에서 HttpSession 객체를 통해 접근) --%>
		<input type="hidden" name="reply_name" value="${sessionScope.sId }">
		<%-- 세션 아이디가 없을 경우(미로그인 시) 댓글 작성 차단 --%>
		<%-- textarea 및 버튼 disabled 처리 --%>
		<c:choose>
			<c:when test="${empty sessionScope.sId }"> <%-- 세션 아이디 없음 --%>
				<textarea id="replyTextarea" name="reply_content" placeholder="로그인 한 사용자만 작성 가능합니다" disabled></textarea>
				<input type="submit" value="댓글쓰기" id="replySubmit" disabled>
			</c:when>
			<c:otherwise>
				<textarea id="replyTextarea" name="reply_content" required></textarea>
				<input type="submit" value="댓글쓰기" id="replySubmit">
			</c:otherwise>
		</c:choose>
		</form>
		<div id="replyListArea">
			<table>
			<%-- 댓글 내용(reply_content), 작성자(reply_name), 작성일시(reply_date) 표시 --%>
			<%-- 반복문을 통해 List 객체로부터 Map 객체 꺼내서 출력 --%>
			<c:forEach var="tinyReplyBoard" items="${tinyReplyBoardList }">
					<%-- 댓글 1개에 대한 제어(대댓글 작성 폼 표시, 댓글 제거)를 위한 id 값 지정 --%>
					<%-- 각 댓글(tr 태그)별 고유 id 생성하기 위해 댓글 번호를 id 에 조합 --%>
				<tr id="replyTr_${tinyReplyBoard.reply_num }">
					<td class="replyContent">
						${tinyReplyBoard.reply_content }
						<%-- 세션 아이지 존재할 경우 대댓글 작성 이미지(reply-icon.png) 추가 --%>
						<c:if test="${not empty sessionScope.sId }">
							<%-- 대댓글 작성 아이콘 클릭 시 자바스크립트 함수 reReplyWriteForm() 호출 --%>
							<%-- 파라미터 : 댓글 번호, 댓글 참조글번호, 댓글 들여쓰기레벨, 댓글 순서번호 --%>
							<a href="javascript:reReplyWriteForm(${tinyReplyBoard.reply_num }, ${tinyReplyBoard.reply_re_ref }, ${tinyReplyBoard.reply_re_lev }, ${tinyReplyBoard.reply_re_seq })"> <%-- 하이퍼링크의 기본 기능으로 이동 --%>
								<img src="${pageContext.request.contextPath }/resources/images/reply-icon.png">
							</a>
							<%-- 또한, 세션 아이디가 댓글 작성자와 동일하거나 관리자일 경우 --%>
							<%-- 댓글 삭제이미지(delete-icon.png) 추가 --%>
							<c:if test="${sessionScope.sId eq tinyReplyBoard.reply_name or sessionScope.sId eq 'admin' }">
								<%-- 대댓글 작성 아이콘 클릭 시 자바스크립트 함수 confirmReplyDelete() 호출 --%>
								<%-- 파라미터 : 댓글 번호 --%>
							<a href="javascript:void(0)" onclick="confirmReplyDelete(${tinyReplyBoard.reply_num })"> <%-- void(0)으로 하이퍼링크를 막고 onclick 이벤트로 이동 --%>
								<img src="${pageContext.request.contextPath }/resources/images/delete-icon.png">
							</a>
							</c:if>
						</c:if>
					</td>
					<td class="replyWriter">${tinyReplyBoard.reply_name }</td>
					<td class="replyDate">
<%-- 						${tinyReplyBoard.reply_date } --%>
						<%-- 파싱은 문자열을 객체로 변환하는것이고 포맷은 객체를 문자열로 변환하는 것이다. --%>
						<%--
						만약, 테이블 조회 결과를 Map 타입으로 저장 시 날짜 및 시각 데이터가
						JAVA 8 부터 지원하는 LocalXXX 타입으로 관리된다! (ex. LocalDate, LocalTime, LocalDateTime)
						=> 일반 Date 타입에서 사용하는 형태로 파싱 후 다시 포맷 변경하는 작업 필요 
						=> JSTL fmt 라이브러리의 <fmt:parseDate> 태그 활용하여 파싱 후
						   <fmt:formatDate> 태그 활용하여 포맷팅 수행
						=> 1) <fmt:parseDate>
						      var : 파싱 후 해당 날짜를 다룰 객체명
						      value : 파싱될 대상 날짜 데이터
						      pattern : 파싱 대상 날짜 데이터의 형식(이 때, 시각을 표시하는 문자 T 는 단순 문자로 취급하기 위해 'T' 로 표기)
						      type : 대상 날짜 파싱 타입(time : 시각, date : 날짜, both : 둘 다)
						   2) <fmt:formatDate>
						      value : 출력(포맷팅)할 날짜 데이터
						      pattern : 포맷팅 할 날짜 형식
						--%>
						<fmt:parseDate var="parsedReplyDate" value="${tinyReplyBoard.reply_date }" pattern="yyyy-MM-dd'T'HH:mm" type="both" />						 
						<fmt:formatDate value="${parsedReplyDate }" pattern="MM-dd HH:mm"/>
					</td>
				</tr>
			</c:forEach>
			</table>
		</div>
	</section>
</body>
</html>
















