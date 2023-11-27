package com.itwillbs.test2.svc;

import java.sql.Connection;

import com.itwillbs.test2.dao.MemberDAO;
import com.itwillbs.test2.db.JdbcUtil;
import com.itwillbs.test2.vo.MemberBean;

// Action 클래스로부터 작업 요청 받아 DAO 클래스와 상호 작용을 통해
// 실제 비즈니스 로직(DB 작업)을 수행하도록 요청하는 클래스
// 또한, DB 작업 수행 후 결과 판별을 통해 트랜잭션(Commit or Rollback) 처리도 수행
// => Service 클래스의 역할은 플랫폼(웹 or 모바일 or 응용프로그램)과 관계없이
// 동일한 DB 작업을 처리할 수 있도록 하는 역할
// => Action 클래스의 역할은 웹과 
public class MemberJoinProService {
	
	// 회원가입 작업 요청을 위한 registMember() 메서드 정의
	// => 파라미터 : MemberBean 객체   리턴타입 : boolean(isJoinSuccess)
	public boolean registMember(MemberBean member) {
		System.out.println("MemberJoinProService - registMember()");
		
		// 1. 작업 요청 처리 결과를 저장할 boolean 타입 변수 선언
		boolean isJoinSuccess = false;
		
		// 2. db.JdbcUtil 클래스로부터 커넥션풀에 저장된 Connection 객체 가져오기(공통)
		// => 트랜잭션 관리를 DAO 객체 대신 Service 객체가 수행해야하므로(commit or rollback)
		//    DAO 가 아닌 Service 객체가 Connection 객체를 관리해야함
		// => 단, JdbcUtil 클래스에서 Connection 객체의 Auto Commit 기능 해제 필요
		//    또한, Commit 과 Rollback 작업을 수행할 메서드 정의도 필요
		Connection con = JdbcUtil.getConnection();
		
		// 3. MemberDAO (공통)
//		MemberDAO dao = new MemberDAO(); // 호출할 때마다 객체가 생성되어 메모리 낭비
		// => 싱글톤 디자인 패턴으로 구현되어 있는 객체를 getInstance() 메서드로 리턴받기
		MemberDAO dao = MemberDAO.getInstance();
		
		// 4. MemberDAO 객체의 setConnection() 메서드를 호출하여 Connection 객체 전달(공통)
		// => 파라미터 : Connection 객체   리턴타입 : void
		dao.setConnection(con);
		
		// 5. MemberDAO 객체의 xxx() 메서드 호출하여 xxx 작업 수행 요청 및 결과 리턴받기
		//    MemberDAO - insertMember() 메서드 호출하여 회원가입 작업 요청 및 결과 리턴받기
		//    => 파라미터 : MemberBean 객체   리턴타입 : int(insertCount)
		int insertCount = dao.insertMember(member);
		
		// 6. DB 작업 요청 처리 결과에 따른 트랜잭션 처리
		if(insertCount > 0) {
			// 트랜잭션 처리 중 현재 작업을 반영하기 위한 Commit 작업 수행을 위해
			// JdbcUtil 클래스의 commit() 메서드 호출
			// => 파라미터 : Connection 객체
			JdbcUtil.commit(con);
			// 작업 처리 결과를 성공으로 변경하여 리턴하기 위해
			// isJoinSuccess 변수값을 true 로 변경
			isJoinSuccess = true;
		} else { // INSERT 작업 실패
			// 트랜잭션 처리 중 이전 상태로 되돌리기 위한 Rollback 작업 수행을 위해
			// JdbcUtil 클래스의 rollback() 메서드 호출
			// => 파라미터 : Connection 객체
			JdbcUtil.rollback(con);
		}
		
		// 7. 작업 완료 후 Connection 객체 반환(공통)
		JdbcUtil.close(con);
		
		// 8. 작업 요청 처리 결과 리턴
		return isJoinSuccess; // MemberJoinProAction 로 리턴
	}
}
