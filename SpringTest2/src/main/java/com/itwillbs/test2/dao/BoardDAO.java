package com.itwillbs.test2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.itwillbs.test2.db.JdbcUtil;
import com.itwillbs.test2.vo.BoardBean;

public class BoardDAO {
	// ------------------- 싱글톤 디자인 패턴 활용한 인스턴스 관리 --------------------
	private static BoardDAO instance = new BoardDAO();
	private BoardDAO() {}
	public static BoardDAO getInstance() {
		return instance;
	}
	// --------------------------------------------------------------------------------
	// 외부로부터 Connection 타입 객체 전달받아 저장할 멤버변수 및 Setter 정의
	Connection con;
	
	public void setConnection(Connection con) {
		this.con = con;
	}
	// --------------------------------------------------------------------------------
	// 글쓰기
	public int insertBoard(BoardBean board) {
		System.out.println("BoardDAO - insertBoard");
		int insertCount = 0;
		
		// 구문을 따로 관리하기 위하여 변수를 따로 저장
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null;
		// 새 글 번호 계산(글번호 컬럼(board_num)이 자동증가 컬럼이 아니기 때문) - SELECT
		// board 테이블의 기존 게시물들의 글번호(board_num) 중 가장 큰 번호 조회
		// => MySQL 구문의 MAX() 함수 사용(SELECT MAX(컬럼명) FROM 테이블명)
		// => 조회 결과가 있을 경우 조회 결과 + 1 값을 새 글 번호로 지정
		//    (단, 조회 결과가 없을 경우 기본값 1로 지정)
		try {
			int board_num = 1; // 새 글 번호(기본값 1)
			String sql = "SELECT MAX(board_num) FROM board";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) { // 조회 결과가 있을 경우
				// 기존 게시물이 하나라도 존재할 경우 가장 큰 번호가 리턴되므로 + 1 값을 저장하고
				// 아니면(게시물이 하나도 없을 경우), 기본값 그대로 사용
				board_num = rs.getInt(1) + 1; // 첫번째 컬럼 지정후  + 1 
			}
//			System.out.println("새 글 번호 : " + board_num);
			// ------------------------------------------------------------------
			// 전달받은 데이터(BoardBean 객체) 사용하여 글쓰기 작업 수행 - INSERT
			// => 새 글 번호는 계산된 번호(board_num 값) 사용
			// => 참조글번호(board_re_ref)는 새 글 번호와 동일한 번호 사용
			// => 들여쓰기레벨(board_re_lev)과 순서번호(board_re_seq)는 0 지정
			// => 조회수(board_readcount) 0 지정
			// => 작성일자(board_date) 는 현재 서버 시각 활용(now() 함수 사용)
			sql = "INSERT INTO board VALUE(?, ?, ?, ?, ?, ?, ?, ?, now(), ?)";
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, board_num); // 글번호(계산된 새 글 번호 사용)
			pstmt2.setString(2, board.getBoard_name()); // 작성자(전달받은 값)
			pstmt2.setString(3, board.getBoard_subject()); // 제목(전달받은 값)
			pstmt2.setString(4, board.getBoard_content()); // 내용(전달받은 값)
			pstmt2.setInt(5, board_num); // 참조글 번호(계산된 새 글 번호 사용)
			pstmt2.setInt(6, 0); // 들여쓰기레벨
			pstmt2.setInt(7, 0); // 순서번호
			pstmt2.setInt(8, 0); // 조회수
			pstmt2.setString(9, board.getWriter_ip()); // 작성자 IP 주소(전달받은 값)
//			System.out.println(pstmt2);
			insertCount = pstmt2.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - insertBoard()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			// 서로 다른 구문끼리는 어느쪽을 먼저 닫아도 상관없다.
			// 다만 명시적으로 아래쪽으로 작성한다.
			JdbcUtil.close(pstmt2); 
		}
		return insertCount;
	}
	// 글목록 조회
	public List<BoardBean> selectBoardList(int startRow, int listLimit) {
		System.out.println("BoardDAO - selectBoardList()");
		List<BoardBean> boardList = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// board 테이블의 모든 레코드 조회
			// => 정렬 기준(ORDER BY 절 사용) : 참조글번호(board_re_ref) 기준 내림차순,
			//                                  순서번호(board_re_seq) 기준 오름차순
			// => 조회 레코드 갯수 제한(LIMIT 절 사용)
			//    1) 정수 파라미터 1개(limit) : 지정한 갯수만큼 조회
			//    2) 정수 파라미터 2개(startRow, limit) : 시작행 번호부터 지정한 갯수만큼 조회
			String sql = "SELECT * FROM board "
					+ "ORDER BY board_re_ref DESC, board_re_seq ASC LIMIT ?, ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, listLimit);
			rs = pstmt.executeQuery();
			
			// 전체 레코드를 저장할 List<BoardBean> 타입 객체 생성
			// => 주의! while 문 밖에서 생성 필수! (BoardBean 객체를 누적해야하므로)
			boardList = new ArrayList<BoardBean>();
			
			while(rs.next()) {
				// 1개 레코드(1개 게시물 정보)를 저장할 BoardBean 객체 생성
				// => 새 객체 생성을 통해 새 레코드 저장(반복 필요하므로 while 문 안에서 생성)
				BoardBean board = new BoardBean();
				// BoardBean 객체에 조회 결과(1개 레코드) 저장
				board.setBoard_num(rs.getInt("board_num")); // 글번호
				board.setBoard_name(rs.getString("board_name")); // 작성자
				board.setBoard_subject(rs.getString("board_subject")); // 작성자
				board.setBoard_content(rs.getString("board_content")); // 작성자
				board.setBoard_re_ref(rs.getInt("board_re_ref")); // 참조글번호
				board.setBoard_re_lev(rs.getInt("board_re_lev")); // 들여쓰기레벨
				board.setBoard_re_seq(rs.getInt("board_re_seq")); // 순서번호
				board.setBoard_readcount(rs.getInt("board_readcount")); // 조회수
				board.setBoard_date(rs.getTimestamp("board_date")); // 작성일
				board.setWriter_ip(rs.getString("writer_ip")); // 작성자 IP주소
//				System.out.println(board);
				// 1개 레코드가 저장된 BoardBean 객체를 전체 레코드 저장하는 List 객체에 추가
				boardList.add(board);
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - selectBoardList()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		
		return boardList;
	}
	// 전체 게시물 수 조회
	public int boardListCount() {
		System.out.println("BoardDAO - boardListCount()");

		int listCount = 0;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// board 테이블의 전체 레코드 수 조회
			// => COUNT() 함수 활용
			String sql = "SELECT COUNT(*) FROM board";
			pstmt= con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				// 조회 결과가 있을 경우 첫번째 컬럼 데이터 저장
				listCount = rs.getInt(1);
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - boardListCount()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return listCount;
	}
	
	// 게시물 상세정보 조회
	public BoardBean selectBoard(int board_num) {
		System.out.println("BoardDAO - selectBoard()");
		
		BoardBean board = null;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 글번호(board_num)가 일치하는 레코드 검색 - SELECT
			String sql = "SELECT * FROM board WHERE board_num = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) { // 조회 결과가 있을 경우
				// 1개 게시물 정보를 저장할 BoardBean 객체 생성 및 데이터 저장
				board = new BoardBean();
				// BoardBean 객체에 조회 결과(1개 레코드) 저장
				board.setBoard_num(rs.getInt("board_num")); // 글번호
				board.setBoard_name(rs.getString("board_name")); // 작성자
				board.setBoard_subject(rs.getString("board_subject")); // 제목
				board.setBoard_content(rs.getString("board_content")); // 내용
				board.setBoard_re_ref(rs.getInt("board_re_ref")); // 참조글번호
				board.setBoard_re_lev(rs.getInt("board_re_lev")); // 들여쓰기레벨
				board.setBoard_re_seq(rs.getInt("board_re_seq")); // 순서번호
				board.setBoard_readcount(rs.getInt("board_readcount")); // 조회수
				board.setBoard_date(rs.getTimestamp("board_date")); // 작성일
				board.setWriter_ip(rs.getString("writer_ip")); // 작성자 IP주소
			}
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - selectBoard()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return board;
	}
	
	// 조회수 증가
	public int updateReadcount(int board_num) {
		System.out.println("BoardDAO - updateReadCount()");
		int updateCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			// 글 번호가 일치하는 레코드의 조회수(board_readcount)값 1 만큼 증가 - UPDATE
			String sql = "UPDATE board SET board_readcount = board_readcount + 1 "
					+ "WHERE board_num = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - updateReadcount()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
		}
		
		return updateCount;
	}
	
	// 글 작성자 확인
//	public boolean isBoardWriter(int board_num, String id) {
//		System.out.println("BoardDAO - isBoardWriter()");
//		boolean isBoardWriter = false;
//		
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		
//		try {
//			// 글번호와 아이디가 일치하는 레코드 조회
//			String sql = "SELECT * FROM board WHERE board_num = ? AND board_name = ?";
//			pstmt = con.prepareStatement(sql);
//			pstmt.setInt(1, board_num);
//			pstmt.setString(2, id);
//			rs = pstmt.executeQuery();
//			
//			// 조회 결과가 있을 경우 isBoardWriter 변수값을 true 로 변경
//			if(rs.next()) {
//				isBoardWriter = true;
//			}
//			
//		} catch (SQLException e) {
//			System.out.println("SQL 구문 오류 발생 - isBoardWriter()");
//			e.printStackTrace();
//		} finally {
//			JdbcUtil.close(rs);
//			JdbcUtil.close(pstmt);
//		}
//		return isBoardWriter;
//	}
	// 글 작성자 확인(단, 파라미터 타입을 BoardBean 타입으로 변경)
	// => 파라미터 : BoardBean 객체   리턴타입 : boolean(isBoardWriter)
	public boolean isBoardWriter(BoardBean board) {
		System.out.println("BoardDAO - isBoardWriter()");
		boolean isBoardWriter = false;
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 글번호와 아이디가 일치하는 레코드 조회
			String sql = "SELECT * FROM board WHERE board_num = ? AND board_name = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board.getBoard_num());
			pstmt.setString(2, board.getBoard_name());
			rs = pstmt.executeQuery();
			// 조회 결과가 있을 경우 isBoardWriter 변수값을 true 로 변경
			if(rs.next()) {
				isBoardWriter = true;
			}
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - isBoardWriter()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		return isBoardWriter;
	}
	
	// 글 삭제
	public int deleteBoard(int board_num) {
		System.out.println("BoardDAO - deleteBoard()");
		int deleteCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			String sql = "DELETE FROM board WHERE board_num = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, board_num);
			
			deleteCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - deleteBoard()");
			e.printStackTrace();
		} finally {
			// DB 자원 반환
			JdbcUtil.close(pstmt);
		}
		
		return deleteCount;
	}
	
	// 글 수정
	public int updateBoard(BoardBean board) {
		System.out.println("BoardDAO - updateBoard()");
		int updateCount = 0;
		
		PreparedStatement pstmt = null;
		
		try {
			// 글번호가 일치하는 레코드의 제목, 내용 수정 - UPDATE
			String sql = "UPDATE board "
						+ "SET board_subject = ?, board_content = ? "
						+ "WHERE board_num = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, board.getBoard_subject());
			pstmt.setString(2, board.getBoard_content());
			pstmt.setInt(3, board.getBoard_num());
			
			updateCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - updateBoard()");
			e.printStackTrace();
		} finally {
			// DB 자원 반환
			JdbcUtil.close(pstmt);
		}
		
		return updateCount;
	}
	
	// 답글 쓰기
	public int insertReplyBoard(BoardBean board) {
		System.out.println("BoardDAO - insertReplyBoard()");
		
		int insertCount = 0;
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rs = null;
		
		try {
			// 답글 쓰기에 필요한 답글 관련 정보들을 별도의 변수에 저장
			int ref = board.getBoard_re_ref();
			int lev = board.getBoard_re_lev();
			int seq = board.getBoard_re_seq();
			
			// 1. 기존 답글들에 대한 순서번호(board_re_seq) 증가 - UPDATE
			String sql ="UPDATE board "
					+ "SET board_re_seq = board_re_seq + 1 "
					+ "WHERE board_re_ref = ? "
					+ "AND board_re_seq > ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, ref);
			pstmt.setInt(2, seq);
			pstmt.executeUpdate();

			// 기존 사용 완료된 pstmt 객체는 더 이상 불필요하므로 close()
			pstmt.close(); // 예외가 발생해도 catch 블럭에서 처리한다.
			// -------------------------------------------------------
			// 2. 답글 쓰기
			// 2-1) 새 답글에 사용될 lev, seq 값은 원본값 + 1 처리
			lev++;
			seq++;
			
			// 2-2) 기존 게시물의 가장 큰 번호 조회 - SELECT => 글쓰기와 동일
			int board_num = 1; // 새 글 번호(기본값 1)
			sql = "SELECT MAX(board_num) FROM board";
			// 위에서 pstmt 객체를 닫아서 새로 사용 가능
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if(rs.next()) { // 조회 결과가 있을 경우
				// 기존 게시물이 하나라도 존재할 경우 가장 큰 번호가 리턴되므로 + 1 값을 저장하고
				// 아니면(게시물이 하나도 없을 경우), 기본값 그대로 사용
				board_num = rs.getInt(1) + 1; // 첫번째 컬럼 지정후  + 1 
			}
			// ------------------------------------------------------------------
			// 전달받은 데이터(BoardBean 객체) 사용하여 글쓰기 작업 수행 - INSERT
			// => 새 글 번호는 계산된 번호(board_num 값) 사용
			// => 참조글번호(board_re_ref)는 새 글 번호와 동일한 번호 사용
			// => 들여쓰기레벨(board_re_lev)과 순서번호(board_re_seq)는 계산된 값 사용
			// => 조회수(board_readcount) 0 지정
			// => 작성일자(board_date) 는 현재 서버 시각 활용(now() 함수 사용)
			sql = "INSERT INTO board VALUE(?, ?, ?, ?, ?, ?, ?, ?, now(), ?)";
			pstmt2 = con.prepareStatement(sql);
			pstmt2.setInt(1, board_num); // 글번호(계산된 새 글 번호 사용)
			pstmt2.setString(2, board.getBoard_name()); // 작성자(전달받은 값)
			pstmt2.setString(3, board.getBoard_subject()); // 제목(전달받은 값)
			pstmt2.setString(4, board.getBoard_content()); // 내용(전달받은 값)
			pstmt2.setInt(5, ref); // 참조글 번호(전달받은 값)
			pstmt2.setInt(6, lev); // 들여쓰기레벨(계산된 값)
			pstmt2.setInt(7, seq); // 순서번호(계산된 값)
			pstmt2.setInt(8, 0); // 조회수(기본값0)
			pstmt2.setString(9, board.getWriter_ip()); // 작성자 IP 주소(전달받은 값)
//			System.out.println(pstmt2);
			insertCount = pstmt2.executeUpdate();
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - insertReplyBoard()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
			JdbcUtil.close(pstmt2);
		}
		
		return insertCount;
	}
	
	
}
