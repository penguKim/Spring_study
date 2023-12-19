<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script>
	// 전달받은 msg 속성값으로 alert() 함수로 출력
	alert("${msg}");
	// 전달받은 targetURL 속성값으로 이동하기
	location.href="${targetURL}";
</script>