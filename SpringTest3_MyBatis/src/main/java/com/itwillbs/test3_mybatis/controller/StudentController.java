package com.itwillbs.test3_mybatis.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
	// 3번방법) 의존성 주입받을 멤버변수 선언 시 @Autowired 어노테이션을 지정하여
	// 자동으로 의존 주입을 수행한다.
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
	public String registStudentPro(StudentVO student, Model model) {
		// Setter 메서드가 없다면 객체에 데이터 저장 및 호출 불가
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
		// StudentService - registStudent() 메서드 호출하여 학생정보 등록 요청
		// => 파라미터 : StudentVO 객체   리턴타입 : int(insertCount)
		int insertCount = service.registStudent(student);
//		System.out.println("INSERT 실행 결과 : " + insertCount);
		
		// 등록 실패 시 fail_back.jsp 페이지로 포워딩(디스패치)
		// => 포워딩 출력할 오류메세지를 "msg" 라는 속성명으로 Model 객체에 저장
		//    (현재 메서드 파라미터에 Model 타입 파라미터 변수 선언 필요)
		if(insertCount == 0) {
			model.addAttribute("msg", "학생정보 등록 실패!");
			return "fail_back";
		}
		// 등록 성공 시 "studentList" 서블릿 주소 요청 => 리다이렉트
		return "redirect:/studentList";
	}
	
	// 학생 목록 조회
	// "studentList" 서블릿 주소 매핑(GET) - studentList() 메서드 정의
	// 메서드 파라미터 : 데이터 공유를 위한 Model 객체
	@GetMapping("studentList")
	public String StudentList(Model model) {
		// StudentService - getStudentList() 메서드 호출하여 학생 목록 조회 요청
		// => 파라미터 : 없음   리턴타입 : List<StudentVO>(studentList)
		List<StudentVO> studentList = service.getStudentList();
		System.out.println(studentList);
		// 리턴받은 List 객체를 Model 객체에 저장(속성명 : "studentList")
		model.addAttribute("studentList", studentList);
		
		return "student_list";
	}
	
	// 학생 상세정보 조회
	// "studentInfo" 서블릿 주소 매핑(GET) - studentInfo() 메서드 정의
	// 메서드 파라미터 : 번호(idx) 전달받을 idx 변수, 데이터 공유를 위한 Model 객체
	@GetMapping("studentInfo")
	public String studentInfo(int idx, Model model) {
		System.out.println("입력한 번호 : " + idx);
		// StudentService - getStudentInfo() 메서드 호출하여 학생 상세정보 조회 요청
		// => 파라미터 : 번호(idx)   리턴타입 : StudentVO
		StudentVO student = service.getStudentInfo(idx);
		// 리턴받은 StudentVO 객체를 Model 객체에 저장(속성명 : "student") 후
		// student_info.jsp 페이지로 포워딩
		model.addAttribute("student", student);
		
		return "student_info";
	}
	
	// 학생 정보 수정
	// "editStudent" 서블릿 주소 매핑(POST) - editStudent() 메서드 정의
	// 메서드 파라미터 : 번호(idx), 이름(name), 이메일(email), 학년(grade), Model 객체
	@PostMapping("editStudent")
	public String editStudent(int idx, String name, String email, int grade, Model model) {
		System.out.println(idx + name + email + grade);
		
		// StudentService - modifyStudent() 메서드 호출하여 학생 정보 수정 요청
		// => 파라미터 : 번호(idx), 이름(name), 이메일(email), 학년(grade)
		//    리턴타입 : int(updateCount)
		int updateCount = service.modifyStudent(idx, name, email, grade);
		
		// 수정 실패 시 "학생 정보 수정 실패!" 문자열을 Model 객체에 저장(속성명 "msg") 후
		// 이전페이지로 돌아가기 처리를 위해 fail_back.jsp 페이지 포워딩
		if(updateCount == 0) {
			model.addAttribute("msg", "학생 정보 수정 실패!");
			return "fail_back";
		}
		
		// 아니면, 성공 시 수정된 학생 정보 조회를 위해
		// "studentInfo" 서블릿 주소 리다이렉트 => 파라미터로 번호(idx) 전달
		model.addAttribute("idx", idx);
		return "redirect:/studentInfo";
		// => 리다이렉트시에도 Model 객체에 데이터 전달 시 URL 파라미터로 변환되어 전달됨
	}
	
	// 학생 정보 삭제
	@PostMapping("removeStudent")
	public String removeStudent(@RequestParam Map<String, String> map, Model model) {
		System.out.println(map);
		// StudentService - removeStudent() 메서드 호출하여 학생 정보 삭제 요청
		// => 파라미터 : Map 객체(map)   리턴타입 : int(deleteCount)
		int deleteCount = service.removeStudent(map);
		
		// 삭제 실패 시 "학생 정보 삭제 실패!" 메세지를 fail_back 페이지로 전달(포워딩)
		if(deleteCount == 0) {
			model.addAttribute("msg", "학생 정보 삭제 실패!");
			return "fail_back";
		}
		
		// 아니면, 학생 목록 조회를 위한 "studentList" 서블릿 주소 리다이렉트
		return "redirect:/studentList";
	}
	
	
}
