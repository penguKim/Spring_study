package com.itwillbs.test3_mybatis.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.test3_mybatis.mapper.StudentMapper;
import com.itwillbs.test3_mybatis.vo.StudentVO;

//스프링에서 서비스 역할을 수행할 클래스 정의 시 @Service 어노테이션 지정(@Component 사용 가능)
//=> @Service 어노테이션이 적용된 클래스는 스프링이 관리하는 스프링 빈으로 등록되어
// 컨트롤러 클래스에서 DI(자동 주입)을 통해 객체를 주입받을 수 있다!
@Service
public class StudentService {
	// 마이바티스를 통해 SQL 구문 처리를 담당할 XXXMapper.xml 파일과 연동되는
	// XXXMapper 객체(인터페이스)를 자동 주입받기 위해 @Autowired 어노테이션으로 멤버변수 선언
	// => 단, 매퍼 인터페이스에 @Mapper 어노테이션 적용 필수! (클래스 정의가 아닌 인터페이스 정의)
	@Autowired
	private StudentMapper mapper;

	public int registStudent(StudentVO student) {
		/*
		 * DB 작업을 수행할 Mapper 객체의 메서드를 호출하여 SQL 구문 실행 요청
		 * => DAO 클래스 없이 마이바티스 활용을 위한 Mapper 객체의 메서드 호출 후
		 *    리턴되는 결과값을 전달받아 Controller 클래스로 다시 리턴해주는 역할 수행
		 * => 단, 별도의 추가적인 작업이 없으므로 return 문 뒤에 메서드 호출 코드를 직접 기술하고
		 *    만약, 메서드 호출 전후 추가적인 작업이 필요할 경우 호출 코드와 리턴문을 분리
		 * ---------------------------------------------------------------------------------------
		 * Mapper 역할을 수행하는 XXXMapper 인터페이스는 인스턴스 생성이 불가능하며
		 * 스프링(마이바티스)에서 자동 주입으로 객체를 전달받아 사용
		 */
		System.out.println("StudentService - registStudent()");
		
		// StudentMapper - insertStudent() 메서드를 호출하여 학생정보 등록 요청
		// => 파라미터 : StudentVO 객체   리턴타입 : int(insertCount)
		return mapper.insertStudent(student);
	}
	
	// 학생 상세정보 조회 요청
	public StudentVO getStudentInfo(int idx) {
		// StudentMapper - selectStudent() 메서드 호출하여 학생 상세정보 조회 요청
		return mapper.selectStudent(idx);
	}

	// 학생 목록 조회 요청
	public List<StudentVO> getStudentList() {
		// StudentMapper - selectStudentList() 메서드 호출하여 학생 목록 조회 요청
		return mapper.selectStudentList();
	}

	// 학생 정보 수정 요청
	public int modifyStudent(int idx, String name, String email, int grade) {
		// StudentMapper - updateStudent() 메서드 호출하여 학생 정보 수정 요청
		return mapper.updateStudent(idx, name, email, grade);
	}

	// 학생 정보 삭제 요청
	public int removeStudent(Map<String, String> map) {
		// StudentMapper - deleteStudent() 메서드 호출하여 학생 정보 삭제 요청
		return mapper.deleteStudent(map);
	}

	
}
