package com.itwillbs.test2.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Test3Controller_Redirect2 {
	// [ 리다이렉트 방식 포워딩 시 데이터 전송 ]
	// 1) HttpServletRequest 객체를 통해 데이터 전송하는 경우
	// => 리다이렉트 과정에서 새로운 요청으로 인해 새 request 객체가 생성되므로
	//    이전 요청 정보는 사라지고 새로운 요청 정보만 남기 때문에 이전 데이터 접근 불가
	// => request.getAttribute() 메서드로 접근 시 데이터 없음
	
//	@GetMapping("redirect")
//	public String redirect(HttpServletRequest request) {
//		// request 객체에 "msg" 라는 속성명으로 "Hello, World!" 저장
//		request.setAttribute("msg", "Hello, World!");
//		
//		return "redirect:/redirectServlet";
//	}
	
	// 2) 리다이렉트 주소(URL) 지정 시 URL 파라미터로 전송할 데이터 지정하는 경우 
//	@GetMapping("redirect")
//	public String redirect() {
//		String name = "hong";
//		int age = 20;
//		// 리다이렉트 URL 뒤에 파라미터 데이터 연결하여 전달
//		return "redirect:/redirectServlet?name=" + name + "&age=" + age;
//	}
	
	// 3) 스프링 전용 데이터 공유 객체인 Model 객체를 사용하여 데이터 전송하는 경우
	// => Model 객체에 데이터 담아 리다이렉트 시 자동으로 URL 파라미터로 변환하여 전달됨
	//    디스패치 포워딩 시 request.getAttribute() 메서드로 호출가능
//	@GetMapping("redirect")
//	public String redirect(Model model) {
//		String name = "hong(model)";
//		int age = 20;
//		
//		// 디스패치 방식과 동일하게 Model 객체의 addAttribute() 메서드로 데이터 저장
//		model.addAttribute("name", name);
//		model.addAttribute("age", age);
//		
//		return "redirect:/redirectServlet";
//	}
	
	
	// =====================================================
//	@GetMapping("redirectServlet")
//	public String redirectServlet(HttpServletRequest request) {
//		// HttpServletRequest 타입 파라미터를 통해 request 객체 전달받아 속성값 출력해보기
////		System.out.println("msg 속성값 : " + request.getAttribute("msg"));
//		
//		// URL 파라미터로 전달받은 name, age 값 출력해보기
//		String name = request.getParameter("name");
//		int age = Integer.parseInt(request.getParameter("age"));
//		System.out.println("이름 : " + name + "나이 : " + age);
//		
//		return "test2/redirect";
//	}
	
	
	
}
