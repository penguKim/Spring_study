package com.itwillbs.test2.svc;

import java.sql.Connection;

import com.itwillbs.test2.dao.MemberDAO;
import com.itwillbs.test2.db.JdbcUtil;
import com.itwillbs.test2.vo.MemberBean;

public class MemberLoginProService {
	
	// 로그인 판별 요청을 수행할 isCorrectUser() 메서드
	public boolean isCorrectUser(MemberBean member) {
		System.out.println("MemberLoginProService - isCorrectUser()");
		
		// 1. 작업 결과를 리턴할 변수 선언
		boolean isCorrectUser = false;
		
		// 2. JdbcUtil 클래스로부터 커넥션풀에 저장된 Connection 객체 가져오기(공통)
		Connection con = JdbcUtil.getConnection();
		
		// 3. MemberDAO 클래스로부터 MemberDAO 객체 가져오기(공통)
		MemberDAO dao = MemberDAO.getInstance();
		
		// 4. MemberDAO 객체의 setConnection() 메서드를 호출하여 Connection 객체 전달(공통)
		// => 파라미터 : Connection 객체   리턴타입 : void
		dao.setConnection(con);

		// 5. MemberDAO 객체의 selectCorrectUser() 메서드 호출하여 로그인 작업 요청
		// => 파라미터 : MemberBean 객체   리턴타입 : boolean
		isCorrectUser = dao.selectCorrectUser(member);
		// SELECT 작업 수행은 커밋, 롤백 수행이 불필요하므로 트랜잭션 처리 생략
		
		// 6. 작업 완료 후 Connection 객체 반환(공통)
		JdbcUtil.close(con);
		
		// 7. 작업 결과 리턴
		return isCorrectUser; // MemberLoginProAction 으로 리턴
	}

}
