package com.itwillbs.test2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.itwillbs.test2.vo.PersonVO;
import com.itwillbs.test2.vo.TestVo;

@Controller
public class Test4Controller_MAV {
	/*
	 * [ ModelAndView 객체 ]
	 * - 데이터 저장 용도의 Model 객체와 view 페이지 포워딩 정보를 함께 관리하는 객체
	 * - 매핑 메서드 정의 시 리턴타입을 ModelAndView 타입으로 지정
	 * - 데이터 저장 작업은 Map(HashMap) 객체 활용
	 * - 데이터 저장 및 뷰페이지로 포워딩(디스패치)하는 작업을 일관된 방식으로 처리 가능
	 */
	
//	@GetMapping("mav")
//	public ModelAndView mav() {
//		// ModelAndView 객체 사용 시 데이터 저장 객체로 Map 타입 객체 활용
//		// => Map<String, 저장할데이터타입> 형식으로 제네릭타입 설정
//		// Map 객체(map) 에 "person" 이라는 키로 PersonVO 객체 1개 저장		PersonVO person = new PersonVO();
//		PersonVO person = new PersonVO();
//		person.setName("홍길동");
//		person.setAge(20);
//		
////	Map<String, PersonVO> map = new HashMap<String, PersonVO>();
////	map.put("person", person);
//		// --------------------------------------------------
//		// 만약, PersonVO 객체와 함께 TestVO 객체도 Map 객체에 추가 시
//		// Map 타입 객체 생성 시 제네릭타입을 <String, Object> 타입 지정
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("person", person);
//		TestVo test = new TestVo("제목입니다", "내용입니다");
//		map.put("test", test);
//		
//		// ---------------------------------------------------------------------
//		// ModelAndView 객체 생성 시 포워딩 할 뷰페이지 지정 및 전송할 객체 지정
//		// => 객체 생성 : new ModelAndView("이동할뷰페이지명", "저장데이터속성명", 저장데이터);
//		// => 포워딩 방식은 디스패치 방식이며, 경로 지정은 return 문 뒤에 경로 기술 방법과 동일
//		// map 객체로 전달시 파라미터 두개의 생성자도 가능
//		// redirect 포워딩은 String 데이터만 전달 가능하기에 디스패치 방식으로 포워딩
//		ModelAndView mav = new ModelAndView("test2/model_and_view", "map", map);
//		
//		return mav;
//	}
	
	// -----------------------------------------------------------------------------
	// 매핑 메서드 내에서 공통으로 사용될 객체가 존재할 경우(ex. Map, Model, List 등)
	// 해당 인스턴스를 메서드 내에서 직접 생성할 필요없이 메서드 파라미터로 선언만 하면
	// 자동으로 객체 생성 수행해준다!
	@GetMapping("mav")
	public ModelAndView mav(Map<String, Object> map) {
		PersonVO person = new PersonVO();
		person.setName("홍길동");
		person.setAge(20);
		
//		Map<String, PersonVO> map = new HashMap<String, PersonVO>();
//		map.put("person", person);
		TestVo test = new TestVo("제목입니다", "내용입니다");
		
		map.put("person", person);
		map.put("test", test);
		ModelAndView mav = new ModelAndView("test2/model_and_view", "map", map);
		
		return mav;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
