package com.itwillbs.test3_mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.test3_mybatis.vo.StudentVO;

// 매퍼클래스 역할을 수행하는 인터페이스 정의 시 @Mapper 어노테이션 지정(@Component 사용 가능)
/*
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * ~~~~~~~~~~~~~~~~~~~~~~~~~
 */
@Mapper
public interface StudentMapper {
	/*
	 * 서~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 * 주의!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	int insertStudent(StudentVO student);
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	
	// 학생 상세정보 조회 추상메서드 정의
	StudentVO selectStudent(int idx);

	// 학생 목록 조회
	List<StudentVO> selectStudentList();

	// 학생 정보 수정
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ~~~~~~~~~~~~~~~~~~~~~~~~
	int updateStudent(@Param("idx") int idx, 
					  @Param("name") String name, 
					  @Param("email") String email, 
					  @Param("grade") int grade);

	// 학생 정보 삭제
	int deleteStudent(Map<String, String> map);
}
