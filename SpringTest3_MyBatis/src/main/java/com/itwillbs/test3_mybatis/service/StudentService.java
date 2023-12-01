package com.itwillbs.test3_mybatis.service;

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
		System.out.println("StudentService - registStudent()");
		
		// StudentMapper - insertStudent() 메서드를 호출하여 학생정보 등록 요청
		// => 파라미터 : StudentVO 객체   리턴타입 : int(insertCount)
		return mapper.insertStudent(student);
	}
	
}
