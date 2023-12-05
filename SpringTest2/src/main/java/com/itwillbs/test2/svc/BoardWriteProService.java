package com.itwillbs.test2.svc;

import java.sql.Connection;

import com.itwillbs.test2.dao.BoardDAO;
import com.itwillbs.test2.db.JdbcUtil;
import com.itwillbs.test2.vo.BoardBean;

public class BoardWriteProService {
	// 글쓰기 작업 요청을 수행하는 registBoard()
	public boolean registBoard(BoardBean board) {
		System.out.println("BoardWriteProService - registBoard()");
		
		boolean isWriteSuccess = false;
		
		Connection con = JdbcUtil.getConnection();
		BoardDAO dao = BoardDAO.getInstance();
		dao.setConnection(con);
		
		// MemberDAO - insertBoard() 메서드 호출하여 글쓰기 작업 요청
		// => 파라미터 : BoardBean 객체   리턴타입 : int(insertCount)
		int insertCount = dao.insertBoard(board);
		
		// 작업 처리 결과에 따른 트랜잭션 처리
		if(insertCount > 0) {
			JdbcUtil.commit(con);
			isWriteSuccess = true;
		} else {
			JdbcUtil.rollback(con);
		}
		
		JdbcUtil.close(con);

		return isWriteSuccess;
	}

}
