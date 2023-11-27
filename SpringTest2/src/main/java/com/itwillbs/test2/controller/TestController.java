package com.itwillbs.test2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

// 컨트롤러 역할을 수행하는 클래스 정의
// => @Controller 어노테이션을 적용하면 스프링에서 컨트롤러 역할 클래스로 인식(매핑에 활용)
@Controller
public class TestController {
	// "/main" 서블릿 주소 요청 시 자동으로 호출되는 requestMain() 메서드 정의(매핑)
	// => 파라미터 : 없음   리턴타입 : String
	// => GET 방식 요청 시 "views/main.jsp" 페이지 포워딩(디스패치)
	// @RequestMapping(value = "서블릿주소", method = 요청방식)
//	@RequestMapping(value = "/main", method = RequestMethod.GET)
//	public String requestMain() { // GET 방식의 "/main" 서블릿 주소 요청 시 자동 호출되는 메서드
//		System.out.println("TestController - requestMain()");
//		// WEB-INF/views/main.jsp 페이지로 포워딩(디스패치)하기 위해
//		// 기본 경로 상에서의 파일명만 return 문에 기술
//		return "main"; // "/WEB-INF/views/main.jsp" 문자열이 생성되어 포워딩 수행
//	}
	
	// "/main" 서블릿 주소 요청을 POST 방식 요청으로 변경 처리
//	@RequestMapping(value = "/main", method = RequestMethod.POST)
//	public String requestMain() { // GET 방식의 "/main" 서블릿 주소 요청 시 자동 호출되는 메서드
//		System.out.println("TestController - requestMain()");
//		// WEB-INF/views/main.jsp 페이지로 포워딩(디스패치)하기 위해
//		// 기본 경로 상에서의 파일명만 return 문에 기술
//		return "main"; // "/WEB-INF/views/main.jsp" 문자열이 생성되어 포워딩 수행
//	}
	// => 주의! POST 방식 지정 시 GET 방식으로 요청 발생할 경우 일치하는 메서드가 없게 되므로
	//    해당 요청을 처리할 수 없게 되어 오류 발생
	//    HTTP 상태 405 – 허용되지 않는 메소드(Request method 'GET' not supported)
	
	
	// -----------------------------------------
	// @RequestMapping 어노테이션에서 method 속성 생략 시 기본값 GET
//	@RequestMapping(value = "/main") // GET 방식 요청 매핑
//	public String requestMain() {
//		return "main";
//	}
	
	// @RequestMapping 어노테이션에서 속성명을 생략 시 기본 속성명 value 적용됨
	@RequestMapping("/main")
	public String requestMain() {
		return "main";
	}
	
	// -----------------------------------------
	// "/test1" 서블릿 주소 요청을 처리할 test1() 메서드 정의
	// 자주 사용하는 메서드 속성의 요청 방식(GET, POST)을 어노테이션으로 구현함
	// => @GetMapping 어노테이션을 사용하여 "GET 방식 요청 [전용]" 매핑 수행
	// => POST 방식 요청 발생 시 처리 불가능하므로 에러 발생
	@GetMapping("/test1") // 단일 파라미터명일 경우 value 속성명 생략 가능
	public String test1() {
		// test 디렉토리의 test1.jsp 페이지로 포워딩(디스패치)
		return "test/test1";
	}
	
	// -------------------------------------------------
	// 서블릿 주소가 동일하더라도 요청 방식이 다를 경우 각각 다른 메서드로 매핑(처리) 가능
	// "/test2" 서블릿 주소 요청을 처리할 test2() 메서드 정의(GET, POST 모두 요청)
	// => @GetMapping 어노테이션을 사용하여 "GET 방식 요청 전용" test2_get() 메서드 정의
	// => @PostMapping 어노테이션을 사용하여 "POST 방식 요청 전용" test2_post() 메서드 정의
	// => 두 메서드 모두 "test/test2.jsp" 페이지로 포워딩
//	@GetMapping("/test2")
//	public String test2_get() {
//		System.out.println("GET 방식");
//		return "test/test2";
//	}
//	@PostMapping("/test2")
//	public String test2_post() {
//		System.out.println("POST 방식");
//		return "test/test2";
//	}
	
	// @RequestMapping 어노테이션 사용 시 서로 다른 요청 방식을
	// 하나의 메서드에서 매핑하여 처리가 가능하다.
	// => method 속성에 중괄호 사용하여 배열 형식으로 요청 방식 복수개 지정 가능
	// => value 속성에 복수개의 경로 매핑도 가능(=> String[] 타입)
	@RequestMapping(value = "/test2", method = {RequestMethod.GET, RequestMethod.POST})
	public String test2() {
		System.out.println("@RequestMapping - GET & POST");
		return "test/test2";
	}
	
	
	
}










