package com.itwillbs.test2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.itwillbs.test2.db.JdbcUtil;
import com.itwillbs.test2.vo.MemberBean;
import com.itwillbs.test2.vo.MemberStatus;

//실제 비즈니스 로직을 수행하는 MemberDAO 클래스 정의
//=> 각 Service 클래스 인스턴스에서 MemberDAO 인스턴스에 접근 시 고유 데이터가 불필요하므로
// MemberDAO 인스턴스는 애플리케이션에서 단 하나만 생성하여 공유해도 된다!
// 따라서, 싱글톤 디자인 패턴을 적용하여 클래스를 정의하면
// 하나의 인스턴스를 모두가 공유하므로 메모리 낭비를 최소화 할 수 있다!
public class MemberDAO {
	// --------- 싱글톤 디자인 패턴을 활용한 MemberDAO 인스턴스 생성 작업 ----------
	// 1. 외부에서 인스턴스 생성이 불가능하도록 생성자를 private 접근제한자로 선언
	private MemberDAO() {} // 외부에서 생성자 호출이 불가능하므로 인스턴스 생성 불가
	
	// 2. 자신의 클래스 내에서 직접 인스턴스 생성하여 멤버변수에 저장
	//    => 멤버변수는 외부에서 접근이 불가능하도록 private 접근제한자로 선언
	//    => 인스턴스 생성 없이도 클래스가 메모리에 로딩될 때 함께 로딩되어
	//       클래스명만으로 접근 가능하도록 static 멤버변수로 선언
	//    => static 메서드에서 접근 가능하기 위해서도 static 멤버변수로 선언해야함
	private static MemberDAO instance = new MemberDAO();
	
	// 3. 생성된 인스턴스를 외부로 리턴하는 Getter 메서드 정의
	//    => 누구나 접근 가능하도록 public 접근제한자로 선언
	//    => 인스턴스 생성 없이도 클래스가 메모리에 로딩될 때 함께 로딩되어
	//       클래스명만으로 접근 가능하도록 static 메서드로 선언
	//       (주의! 메서드 내에서 접근할 변수도 반드시 static 메서드로 선언되어야 한다!)
	public static MemberDAO getInstance() {
		return instance;
	}
	// -------------------------------------------------------------------
	// DB 접근에 사용될 Connection 객체를 Service 로부터 전달받기 위한
	// Connection 타입 멤버변수 선언 및 Setter 메서드 정의
	private Connection con;

	public void setConnection(Connection con) {
		this.con = con;
	}
	// -------------------------------------------------------------------
	// 회원 가입 작업 수행 - INSERT
	// => 파라미터 : MemberBean 객체   리턴타입 : int(insertCount)
	public int insertMember(MemberBean member) {
		System.out.println("MemberDAO - insertMember()");
		int insertCount = 0;
		
		// 주의! Connection 객체는 이미 외부(Service)로부터 전달받은 상태이므로
		// 다시 JdbcUtil.getConnection() 메서드를 호출하지 않도록 해야한다! (서로 다른 객체)
		
		// DB 작업에 필요한 변수 선언
		PreparedStatement pstmt = null;
		
		try {
			// 3단계. SQL 구문 작성 및 전달
			// member 테이블에 모든 데이터 추가 - INSERT
			// => 번호(idx) 컬럼은 자동 증가(AUTO_INCREMENT) 컬럼이므로 null 값 전달
			// => 등록일(reg_date) 컬럼은 DB 서버의 현재 날짜 및 시각 활용(now() 함수 호출)
			String sql = "INSERT INTO member VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?)";
			// PreparedStatement 객체의 setXXX() 메서드 호출하여 파라미터(?) 데이터 교체
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getId());
			pstmt.setString(3, member.getPasswd());
			pstmt.setString(4, member.getJumin());
			pstmt.setString(5, member.getAddress());
			pstmt.setString(6, member.getEmail());
			pstmt.setString(7, member.getJob());
			pstmt.setString(8, member.getGender());
			pstmt.setString(9, member.getHobby());
			pstmt.setString(10, member.getMotivation());
			pstmt.setInt(11, MemberStatus.ACTIVE); // 회원 상태(기본값 1 로 처리 = 정상 회원)
			// 4단계. SQL 구문 실행 및 결과 처리
			// => INSERT 구문이므로 PreparedStatement 객체의 executeUpdate() 메서드 호출
			insertCount = pstmt.executeUpdate();
		} catch (SQLException e) {
			// 3단계 or 4단계에서 SQLException 예외 발생 시 실행될 코드 기술
			System.out.println("INSERT 구문 오류 발생! - insertMember()");
			e.printStackTrace();
		} finally {
			// 예외 발생 여부와 관계없이 항상 실행될 코드 기술
			// DB 자원 반환
			// => 주의! Connection 객체는 Service 에서 반환하므로 DAO 에서 반환 금지!
			JdbcUtil.close(pstmt);
		}
		// INSERT 작업 결과 리턴
		return insertCount; // MemberJoinProService 로 리턴
	}
	
	// 로그인 판별 수행 - SELECT
	public boolean selectCorrectUser(MemberBean member) {
		System.out.println("MemberDAO - selectCorrectUser()");
		boolean isCorrectUser = false;
		
		// DB 작업에 필요한 변수 선언
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		// 3단계. SQL 구문 작성 및 전달
		// 전달받은 아이디와 패스워드가 일치하는 레코드 검색 - SELECT
		try {
			String sql = "SELECT * FROM member WHERE id = ? AND passwd = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPasswd());
			
			// 4단계. SQL 구문 실행 및 결과 처리
			rs = pstmt.executeQuery();
			
			// 조회 결과가 있을 경우 로그인 성공(isCorrectUsert 를 true 로 변경) 처리
			if(rs.next()) {
				isCorrectUser = true;
			}
			// => 단, rs.next() 결과값과 isCorrectUser 에 저장할 값이 동일하므로
			//    if 문 없이 rs.next() 메서드 리턴값을 바로 isCorrectUser 변수에 저장 가능
//			isCorrectUser = rs.next();
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - selectCorrectUser()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return isCorrectUser; // MemberLoginProService 로 리턴
	}
	
	// 회원 상세정보 조회 - SELECT
	public MemberBean selectMember(String id) {
		System.out.println("MemberDAO - selectMember()");
		
		MemberBean member = null;
		
		// DB 작업에 필요한 변수 선언
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			// 3단계. SQL 구문 작성 및 전달
			// 전달받은 아이디와 일치하는 레코드 검색 - SELECT
			String sql = "SELECT * FROM member WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			// 4단계. SQL 구문 실행 및 결과 처리
			// 조회 결과가 존재할 경우 MemberBean 객체에 각 컬럼 데이터 저장
			rs = pstmt.executeQuery();
			
			if(rs.next()) { // 조회 결과가 존재할 경우
				// MemberBean 객체 생성
				member = new MemberBean();
				// ResultSet 객체의 getXXX() 메서드를 호출하여 조회된 컬럼에 접근 후
				// 해당 컬럼 데이터를 MemberBean 객체의 setXXX() 메서드 호출하여 저장
				member.setIdx(rs.getInt(1));
				member.setName(rs.getString(2));
				member.setId(rs.getString(3));
				member.setPasswd(rs.getString(4));
				member.setJumin(rs.getString(5));
				member.setAddress(rs.getString(6));
				member.setEmail(rs.getString(7));
				member.setJob(rs.getString(8));
				member.setGender(rs.getString(9));
				member.setHobby(rs.getString(10));
				member.setMotivation(rs.getString(11));
				member.setReg_date(rs.getDate(12)); // 시간 포함 시 rs.getTimestamp() 메서드 사용
//				System.out.println(member);
			}
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - selectMember()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(rs);
			JdbcUtil.close(pstmt);
		}
		
		return member;
	}
	// 회원 상태 수정 - UPDATE
	public int UpdateMemberStatus(String id, int status) {
		System.out.println("MemberDAO - UpdateMemberStatus()");
		int updateCount = 0;
		
		// DB 작업에 필요한 변수 선언
		PreparedStatement pstmt = null;
		
		try {
			// 3단계. SQL 구문 작성 및 전달
			// 아이디가 일치하는 레코드의 status 컬럼값을 변경 - UPDATE
			// => status 컬럼값은 전달받은 status 변수값 활용
			String sql = "UPDATE member SET status = ? WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, status);
			pstmt.setString(2, id);
			// 4단계. SQL 구문 실행 및 결과 처리
			updateCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - UpdateMemberStatus()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
		}
		
		return updateCount;
	}
	
	// 회원 정보 수정
	public int updateMember(MemberBean member) {
		System.out.println("MemberDAO - UpdateMember()");
		int updateCount = 0;
		
		// DB 작업에 필요한 변수 선언
		PreparedStatement pstmt = null;
		
		try {
			// 3단계. SQL 구문 작성 및 전달
			// 아이디 일치하는 레코드의 이름, 주소, 이메일, 직업, 성별, 취미, 가입동기 수정 - UPDATE
			// 단, 패스워드는 입력되었을 경우에만 변경 처리
			String sql = "";
//			if(member.getPasswd().equals("")) { // 패스워드가 없을 경우(변경하지 않을 경우)
//				sql = "UPDATE member "
//						+ "SET name = ?, address = ?, email = ?, job = ?, gender = ?, hobby = ?, motivation = ? "
//						+ "WHERE id = ?";
//			} else { // 패스워드가 있을 경우(변경할 경우)
//				sql = "UPDATE member "
//						+ "SET name = ?, address = ?, email = ?, job = ?, gender = ?, hobby = ?, motivation = ?ㄴ "
//						+ ", passwd = ? "
//						+ "WHERE id = ?";
//			}
			
			// ------------------------------------------------------------------------
			// 공통 문장은 그대로 두고, 문자열 결합 형태로 패스워드만 추가하는 방법
//			sql = "UPDATE member "
//					+ "SET name = ?, address = ?, email = ?, job = ?, gender = ?, hobby = ?, motivation = ? ";
//			
//			// 패스워드 있을 경우(변경할 경우) 패스워드 변경 SET 절을 문자열 결합 추가
//			if(!member.getPasswd().equals("")) { 
//				sql += ", passwd = ?";
//			} 
//			
//			sql += "WHERE id = ?";
			// ------------------------------------------------------------------------
			// 공통 문장은 그대로 두고 추가할 SET 절을 WHERE 앞에 추가(치환)(수정필요)
			sql = "UPDATE member "
					+ "SET name = ?, address = ?, email = ?, job = ?, gender = ?, hobby = ?, motivation = ? "
					+ "WHERE id = ?";
			if(!member.getPasswd().equals("")) { 
				// replace() 메서드를 통해 WHERE 절 앞에 패스워드 항목 추가
				// => 치환된 문자열을 다시 sql 변수에 저장
				sql = sql.replace("WHERE", ", passwd = ? WHERE");
			} 
			
//			String sql = "UPDATE member "
//					+ "SET name = ?, address = ?, email = ?, job = ?, gender = ?, hobby = ?, motivation = ? "
//					+ "WHERE id = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getName());
			pstmt.setString(2, member.getAddress());
			pstmt.setString(3, member.getEmail());
			pstmt.setString(4, member.getJob());
			pstmt.setString(5, member.getGender());
			pstmt.setString(6, member.getHobby());
			pstmt.setString(7, member.getMotivation());
			
			// 패스워드 유무에 따라 만능문자 인덱스도 달라지므로 조건 변경 필요
			if(member.getPasswd().equals("")) { // 패스워드가 없을 경우(변경하지 않을 경우)
				// 기존 문장 그대로 8번 인덱스에 아이디 치환
				pstmt.setString(8, member.getId());
			} else { // 패스워드가 있을 경우(변경할 경우)
				// 기존 문장에서 8번 인덱스에 패스워드가 추가되고 아이디는 9번 인덱스로 변경
				pstmt.setString(8, member.getPasswd());
				pstmt.setString(9, member.getId());
				
			}
			
			// 4단계. SQL 구문 실행 및 결과 처리
			updateCount = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("SQL 구문 오류 발생 - UpdateMember()");
			e.printStackTrace();
		} finally {
			JdbcUtil.close(pstmt);
		}
		
		return updateCount;
	}
	
	
}
