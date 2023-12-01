package com.itwillbs.test3_mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.test3_mybatis.vo.StudentVO;

// 매퍼클래스 역할을 수행하는 인터페이스 정의 시 @Mapper 어노테이션 지정(@Component 사용 가능)
@Mapper
public interface StudentMapper {
	/*
	 * 서비스 클래스로부터 호출받아 SQL 구문 실행을 위해 XML 파일과 연결될 추상메서드 정의
	 * 주의! 추상메서드명과 XML 태그의 각 태그 내의 id 속성값이 일치해야한다!
	 */
	int insertStudent(StudentVO student);

}
