package com.itwillbs.test2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test3Controller_Redirect {
	/*
	 * 스프링에서 리다이렉트 방식의 포워딩을 수행하려면
	 * return 문에 "redirect:/리다이렉트주소" 형식으로 지정
	 * => 새로운 요청이 발생(새로운 서블릿 주소 요청) 시 사용
	 * => 리다이렉트 방식 포워딩 시 request 객체를 통해 데이터 전송 불가
	 *    (새로운 요청에 의해 새로운 request 객체가 생성되기 때문)
	 *    따라서, URL 파라미터를 활용하여 전달해야함
	 */
	
//	@GetMapping("redirect")
//	public String redirect() {
//		System.out.println("redirect - GET");
//		// 리다이렉트 방식으로 포워딩 할 서블릿 주소 "redirectServlet" 지정
//		return "redirect:/redirectServlet";
//		// => 브라우저 주소표시줄 주소가 "http://localhost:8081/test2/redirectServlet" 로 변경
//	}
//	
//	// "redirectServlet" 서블릿 주소 요청에 대해
//	// 디스패치 방식으로 포워딩 수행할 redirectServlet() 메서드 매핑
//	// => "test/redirect.jsp" 페이지로 포워딩
//	@GetMapping("redirectServlet")
//	public String redirectServlet() {
//		return "test2/redirect";
//	}
}
