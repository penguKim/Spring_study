package com.itwillbs.test2.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.test2.vo.TestVo;

@Controller
public class Test2Controller_push {
	
	/*
	 * 스프링에서 다른 곳으로 Dispatcher 방식 포워딩 시 데이터를 전달하는 방법 2가지
	 *  1) 기존에 사용하던 HttpServletRequest 객체의 setAttribute() 사용
	 *     => JSP 파일이 아니므로 내장 객체 request 가 존재하지도 않고
	 *        컨트롤러 메서드 파라미터에 HttpServletRequest 객체가 명시되어 있지도 않음
	 *        따라서, 외부로부터 request 객체를 전달받아야 함
	 *     => 스프링에서는 의존주입(DI = Dependency Injection) 을 통해 전달받을 수 있음
	 *     => push() 메서드 내의 파라미터 타입으로 HttpServletRequest 타입 변수를 선언하면
	 *        스프링에 의해 해당 객체가 자동으로 전달(= 주입) 됨
	 *  2) 스프링 전용 Model 객체의 addAttribute() 사용
	 *     => org.springframework.ui.Model 타입을 파라미터로 지정 시
	 *        데이터 저장이 가능한 Model 객체를 자동으로 주입받을 수 있음
	 *     => HttpServletRequest 객체와 성격이 유사하며,
	 *        java.util.Map 객체 기반으로 만든 스프링에서 제공하는 데이터 공유 전용 객체
	 *     => Model 객체에 데이터 저장 시 스프링이 자동으로 HttpServletRequest 객체에 저장(후처리)
	 */
	
//	// 1번 방법) 매핑 메서드 파라미터를 통해 필요한 객체(HttpServletRequest 타입) 주입받기
//	@GetMapping("push") // 매핑 주소 지정 시 "/" 기호 생략 가능
//	public String push(HttpServletRequest request) {
//		// 매핑 과정에서 메서드(push()) 호출 시점에 자동으로 HttpServletRequest 객체 전달(주입) 됨
//		// => JSP 의 MVC 패턴에서 서블릿 클래스(XXXFrontController) 정의 시
//		//    doGet(), doPost() 메서드가 request, response 객체를 자동으로 전달받는 것과 동일
//		// ---------------------------------------------------------------------------------------
//		// request 객체에 "msg" 라는 속성명으로 "Hello, World! - request" 문자열 저장
//		request.setAttribute("msg", "Hello, World! - request");
//		// request 객체에 "test" 라는 속성명으로 TestVO 객체 1개 저장
//		TestVo test = new TestVo("제목", "내용");
//		request.setAttribute("test", test);
//		request.setAttribute("test", new TestVo("제목", "내용"));
//		
//		// 디스패치 방식 포워딩을 통해 "WEB-INF/views/test2/push.jsp" 페이지로 포워딩
//		// => URL 유지, request 객체 유지
//		return "test2/push";
//	}
	
	
	// 2번 방법) 매핑 메서드 파라미터를 통해 Model 주입받기
	@GetMapping("push") // 매핑 주소 지정 시 "/" 기호 생략 가능
	public String push(Model model) {
		// 매핑 과정에서 메서드(push()) 호출 시점에 자동으로 Model 객체 전달(주입) 됨
		// request 객체와 마찬가지로 데이터를 담아 전달하는 용도의 데이터 전달자
		// => 스프링 전용 객체(라이브러리)이며, java.util.Map 을 기반으로 정의
		// => request.setAttribute() 와 마찬가지로 model.addAttribute() 로 저장
		//    (파라미터 형식이 완벽하게 동일함)
		// => request 객체와 범위(Scope) 동일(하나의 요청에 대한 응답 발생 지점까지)
		// => request 객체와 동시 사용 불가(일반적 데이터 저장 시 request 객체보다 Model 객체 사용)
		// ---------------------------------------------------------------------------------------
		// model 객체에 "msg" 라는 속성명으로 "Hello, World! - model" 문자열 저장
		model.addAttribute("msg", "Hello, World! - model");
		// model 객체에 "test" 라는 속성명으로 TestVO 객체 1개 저장
		model.addAttribute("test", new TestVo("제목 - model", "내용 - model"));
		
		// 디스패치 방식 포워딩을 통해 "WEB-INF/views/test2/push.jsp" 페이지로 포워딩
		// => URL 유지, request 객체 유지
		return "test2/push";
	}
	
}
