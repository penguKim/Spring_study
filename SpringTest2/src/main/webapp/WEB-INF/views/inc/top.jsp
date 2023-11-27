<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
	#member_area {
		text-align: right;
	}
	
	#menu_area {
		text-align: center;
	}
</style>
<div id="member_area">
	<a href="./">HOME</a>
	<%-- 로그인 여부(세션 아이디 존재 여부) 판별하여 각각 다른 링크 표시 --%>
	<%-- EL 의 sessionScope 내장객체 에 접근하여 "sId" 속성값 존재 여부 판별 --%>
	<c:choose>
		<c:when test="${empty sessionScope.sId }"> <%-- 미 로그인 시 --%>
			| <a href="MemberLoginForm">로그인</a>
			| <a href="MemberJoinForm">회원가입</a>
		</c:when>
		<c:otherwise>
			<%-- 아이디 클릭 시 회원정보 상세조회를 위한 MemberInfo.me 서블릿 요청 --%>
			| <a href="MemberInfo">${sessionScope.sId }</a>
			<%-- 로그아웃 클릭 시 자바스크립트 confirmLogout() 함수 실행 --%>
			| <a href="javascript:confirmLogout()">로그아웃</a>
		</c:otherwise>
	</c:choose>
</div>
<hr>
<div id="menu_area">
	<a href="./">홈</a>
	<a href="main">메인페이지</a>
	<a href="push">데이터전달</a> <%-- Test2Controller - test2/push.jsp (GET) --%>
	<a href="redirect">리다이렉트</a> <%-- Test3Controller - test2/redirect.jsp (GET) --%>
	<a href="mav">Model&View</a> <%-- Test4Controller - test2/mav.jsp (GET) --%>
</div>