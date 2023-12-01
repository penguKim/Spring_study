package com.itwillbs.test3_mybatis.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.test3_mybatis.service.StudentService;
import com.itwillbs.test3_mybatis.vo.StudentVO;

@Controller
public class StudentController {
	// [ 스프링에서 의존 관계에 있는 객체 생성 방법]
	// - 컨트롤러 클래스가 서비스 클래스를 필요로 할 때 "서비스 클래스에 의존적이다" 라고 함
	// - 이 때, 서비스 클래스에 대한 인스턴스를 직접 생성하지 않고
	//   의존성 자동 주입(DI)을 통해 객체를 접근(사용)할 수 있다!
	// 0. 직접 해당 클래스의 인스턴스를 생성하는 방법
	// ------------
	// 1. 생성자를 통해 의존성 객체를 주입받는 방법
	// 2. Setter 메서드를 통해 의존성 객체를 주입받는 방법(메서드에 @Autowired 어노테이션을 지정)
	// 3. 의존성 주입받을 멤버변수 선언 시 @Autowired 어노테이션을 지정
	// => 1 ~ 3번 방법 모두 서비스 클래스(StudentService)에 @Service 어노테이션 지정 필수!
	// ----------------------------------------------------
	// 1번방법) 생성자를 통해 의존성 객체를 주입받는 방법
	// 의존성 객체 타입을 멤버변수로 선언
//	private StudentService service;
	// 생성자를 정의하여 자동 주입받을 객체 타입 파라미터를 명시하면
	// 컨트롤러 인스턴스 생성 시점에 서비스 클래스의 인스턴스를 주입받을 수 있다!
//	public StudentController(StudentService service) {
//		super();
//		this.service = service;
//	}
	// ----------------------------------------------------
	// 2번방법) Setter 메서드를 통해 의존성 객체를 주입받는 방법
//	private StudentService service;
//	// 자동 주입받을 객체 타입 멤버변수 선언 후 Setter 메서드 정의 시
//	// Setter 메서드에 @Autowired 어노테이션을 적용하여 자동으로 메서드가 호출되도록 하면
//	// 해당 메서드가 호출되는 시점에 자동으로 해당 객체를 파라미터로 주입받을 수 있다!
//	@Autowired
//	public void setService(StudentService service) {
//		this.service = service;
//	}
	// ----------------------------------------------------
	// 3번방법) 의존성 주입받을 멤버변수 선언 시 @Autowired 어노테이션을 지정
	@Autowired
	private StudentService service;

	// ====================================================
	@GetMapping("registStudent")
	public String registStudent() {
		return "regist_form";
	}
	

	// "/registStudentPro" 서블릿 매핑
	// => 전송되는 파라미터(이름, 이메일, 학년) 값 전달받아 출력
	// 1) 폼 파라미터 데이터 전달받는 방법 - 파라미터 변수를 각각 선언하는 방법
//	@PostMapping("registStudentPro")
//	public String registStudentPro(String name, String email, int grade) {
//		System.out.println("이름 : " + name);
//		System.out.println("이메일 : " + email);
//		System.out.println("학년 : " + grade);
//		return "";
//	}
	
	// 2) 폼 파라미터 데이터 전달받는 방법 - Map 타입 변수를 선언하는 방법(@RequestParam 선언 필요)
//	@PostMapping("registStudentPro")
//	public String registStudentPro(@RequestParam Map<String, String> map) {
//		System.out.println("이름 : " + map.get("name"));
//		System.out.println("이메일 : " + map.get("email"));
//		System.out.println("학년 : " + map.get("grade"));
//		return "";
//	}
	
	// 3) 폼 파라미터 데이터 전달받는 방법 - VO 타입 변수를 선언하는 방법
	@PostMapping("registStudentPro")
	public String registStudentPro(StudentVO student) {
//		System.out.println("이름 : " + student.getName());
//		System.out.println("이메일 : " + student.getEmail());
//		System.out.println("학년 : " + student.getGrade());
		
		// StudentService - registStudent() 메서드 호출하여 학생정보 등록 요청
		// => 파라미터 : StudentVO 객체   리턴타입 : int(insertCount)
		// -------------------------------------------------------------------
		// [ 스프링에서 의존 관계에 있는 객체 생성 방법]
		// 0. 직접 해당 클래스의 인스턴스를 생성하는 방법
//		StudentService service = new StudentService();
		// => 단, 스프링에서 의존 관계 객체는 의존성 주입(DI, 자동) 기능으로 객체 직접 생성 불필요
		// ----------------------------
		// 1 ~ 3번은 클래스 선언 부 바로 밑에 설명되어있음
		// -----------------------------------------------
		int insertCount = service.registStudent(student);
		
		
		
		return "";
	}
	
	@GetMapping("studentList")
	public String StudentList() {
		return "student_list";
	}
	
	
}
