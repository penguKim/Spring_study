package com.itwillbs.test2.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.test2.vo.PersonVO;

@Controller
public class Test3Controller_Redirect3 {

//	@GetMapping("redirect")
//	public String redirect(Model model) {
//		String name = "hong(model)";
//		int age = 20;
//		
//		// 디스패치 방식과 동일하게 Model 객체의 addAttribute() 메서드로 데이터 저장
//		model.addAttribute("name", name);
//		model.addAttribute("age", age);
//		
//		// 파라미터 매핑 확인용 테스트값 저장
//		model.addAttribute("gender", "F");
//		
//		return "redirect:/redirectServlet";
//	}
	
	
	// =====================================================
	/*
	 * [ 리다이렉트 방식 포워딩을 매핑하는 메서드 내에서 파라미터 데이터 접근하는 방법 ]
	 * 1) HttpServletRequest 객체 활용한 기존 방법(스프링에서 사용 x)
	 *    => 컨트롤러 매핑 메서드 정의 시 HttpServletRequest 객체 타입 파라미터 선언 후
	 *        request.getParameter() 메서드로 파라미터 데이터 가져오기
	 *    => 주의! 파라미터 데이터는 setAttribute() 메서드 호출 불가!
	 */
	
//	@GetMapping("redirectServlet")
//	public String redirectServlet(HttpServletRequest request) {
//		// getAttribute() 메서드 사용 시 null 값 출력됨
//		System.out.println(request.getAttribute("name"));
//		// -------------------------------------------------------
//		// URL 파라미터로 전달받은 name, age 값 출력해보기
//		String name = request.getParameter("name");
//		int age = Integer.parseInt(request.getParameter("age"));
//		System.out.println("이름 : " + name + ", 나이 : " + age);
//		
//		return "test2/redirect";
//	}
	
	// ---------------------
	// 2) 전달받은 파라미터명과 동일한 이름의 파라미터를 매핑 메서드 선언부에 명시하면
	//    이름이 일치하는 파라미터 데이터를 자동으로 해당 변수에 저장 => GET or POST 방식 무관
//	@GetMapping("redirectServlet")
//	public String redirectServlet(String name, int age2) {
//		// "name" 파라미터값이 String 타입 name 변수에 저장되고
//		// "age" 파라미터값이 int 타입 age 변수에 저장됨(자동으로 정수형 형변환까지 처리됨)
//		// => 주의! 메서드 파라미터명과 전달되는 데이터의 파라미터명이 일치하지 않으면
//		//    매핑이 불가능하므로 선언된 파라미터 변수에 데이터 전달 불가!
//		//    따라서 String 타입의 변수일 경우 null 값 저장됨
//		//    또한, String 타입이 아닌 다른 타입일 경우 자동으로 형변환되는 과정에서
//		//    null 값에 접근하게 되므로 예외가 발생한다!
//		//    java.lang.IllegalStateException: Optional int parameter 'age2' is present but cannot be translated into a null value due to being declared as a primitive type.
//		System.out.println("이름2 : " + name + ", 나이2 : " + age2);
//		
//		return "test2/redirect";
//	}
	
	// 3) 전달받은 파라미터명과 동일한 이름의 멤버변수를 갖는 VO(DTO) 클래스를 정의하고
	//    매핑 메서드 파라미터로 해당 VO 클래스 타입 파라미터를 선언할 경우
	//    해당 VO 클래스의 인스턴스 생성 및 Setter 호출을 통한 데이터 저장까지 자동으로 수행한다!
	//    => 주의! VO 클래스에 멤버변수, 기본 생성자, Getter/Setter 필수!
//	@GetMapping("redirectServlet")
//	public String redirectServlet(PersonVO person, char gender) {
//		// PersonVO 객체 자동 생성 및 데이터 저장까지 자동으로 수행되므로
//		// 즉시 PersonVO 객체 접근하여 데이터 사용 가능
//		// => 이 때, 데이터 저장 과정에서 Setter 메서드가 자동으로 호출됨
//		// => 단, 멤버변수명과 파라미터명이 일치하지 않으면 데이터 저장 불가!
//		//    이 경우 타입별 초기값 출력
//		System.out.println("이름(Person) : " + person.getName() + ", 나이(Person) : " + person.getAge());
//		// -----------------------------------------------------
//		// 또한, VO 객체에 일치하는 파라미터가 없을 경우
//		// 별도의 변수 또는 다른 VO 객체 타입을 통해 추가적인 데이터 매핑 처리도 가능
//		System.out.println("성별(char) : " + gender);
//		
//		return "test2/redirect";
//	}
	
	// 4) java.util.Map 타입을 파라미터로 선언하는 방법
	//    => 파라미터명을 key, 파라미터값을 value 한 쌍으로 관리하는 Map 객체를 자동으로 생성 후
	//       각 파라미터 데이터를 자동으로 Map 객체에 추가
	//    => 단, Map 타입 변수만 파라미터로 선언했을 경우 단순히 Map 객체만 생성되고
	//       파라미터 데이터가 자동으로 추가되지 않는다! (Map 객체 내에는 파라미터와 일치하는 속성 없음)
	//       따라서, Map 타입 파라미터 선언부 앞에 @RequestParam 어노테이션을 명시하여
	//       해당 Map 객체가 파라미터 저장용 객체라는 용도를 명시해야한다!
	//       3번 방법에 비해 이름만으로 명확한 용도를 확인하기 어렵다.
//	@GetMapping("redirectServlet")
//	public String redirectServlet(@RequestParam Map<String, String> map) {

//		System.out.println("이름(map) : " + map.get("name") + ", 나이(map) : " + map.get("age"));
//		System.out.println("성별(char) : " + map.get("gender"));
//		
//		return "test2/redirect";
//	}
	
	
}
