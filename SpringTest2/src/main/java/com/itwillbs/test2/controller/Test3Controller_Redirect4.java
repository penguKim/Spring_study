package com.itwillbs.test2.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.test2.vo.PersonVO;

@Controller
public class Test3Controller_Redirect4 {

	@GetMapping("redirect")
	public String redirect(Model model) {
		String name = "hong(model)";
		int age = 20;
		
		// 디스패치 방식과 동일하게 Model 객체의 addAttribute() 메서드로 데이터 저장
		model.addAttribute("name", name);
//		model.addAttribute("age", age); // age 파라미터 자체가 전달되지 않도록 주석 처리
		
		// 파라미터 매핑 확인용 테스트값 저장
		model.addAttribute("gender", "F");
		
		return "redirect:/redirectServlet";
	}
	
	// ---------------------
//	@GetMapping("redirectServlet")
//	public String redirectServlet(String name, int age) {
//		// "name" 파라미터값이 String 타입 name 변수에 저장되고
//		// "age" 파라미터값이 int 타입 age 변수에 저장됨(자동으로 정수형 형변환까지 처리됨)
//		// => 주의! 메서드 파라미터명과 전달되는 데이터의 파라미터명이 일치하지 않으면
//		//    매핑이 불가능하므로 선언된 파라미터 변수에 데이터 전달 불가!
//		//    따라서 String 타입의 변수일 경우 null 값 저장됨
//		//    또한, String 타입이 아닌 다른 타입일 경우 자동으로 형변환되는 과정에서
//		//    null 값에 접근하게 되므로 예외가 발생한다!(HTTP 500 오류)
//		//    java.lang.IllegalStateException: Optional int parameter 'age' is present but cannot be translated into a null value due to being declared as a primitive type.
//		System.out.println("이름 : " + name + ", 나이 : " + age);
//		
//		return "test2/redirect";
//	}
	
	// 해결방법1) 모든 파라미터 타입을 String 타입으로 선언하고 
	//            필요에 따라 메서드 내에서 직접 형변환 작업 추가
	// 해결방법2) 파라미터 선언 시 @RequestParam 어노테이션을 사용하여 
	//            해당 변수가 파라미터 데이터 저장용이라는 표시를 달고
	//            어노테이션 뒤에 소괄호() 기술 후 내부에 defaultValue = "기본값" 형식으로 기본값 설정
	//            => @RequestParam(defaultValue = "기본값") 데이터타입 변수명
	//               (단, 모든 기본값은 문자열 형식으로 지정하며, 파라미터 전달 시 기본값 무시됨)
	@GetMapping("redirectServlet")
	public String redirectServlet(
			@RequestParam(defaultValue = "") String name, 
			@RequestParam(defaultValue = "0") int age) {
		// name, age 파라미터 중 전달되지 않은 파라미터 있을 경우 지정된 기본값 사용됨
		System.out.println("이름 : " + name + ", 나이 : " + age);
		
		return "test2/redirect";
	}
	
	
}
