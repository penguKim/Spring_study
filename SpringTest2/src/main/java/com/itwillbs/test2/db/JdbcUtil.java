package com.itwillbs.test2.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

// 데이터베이스 작업 준비 및 해제(= 자원 반환) 작업을 공통으로 수행할
// 공통 메서드를 갖는 JdbcUtil 클래스 정의
public class JdbcUtil {
	// 데이터베이스 접근을 통해 Connection 객체를 생성 및 외부로 리턴하는
	//    getConnection() 메서드 정의(DB 작업 1단계 & 2단계에 해당)
	// => 파라미터 : 없음   리턴타입 : java.sql.Connection
	// => 단, JdbcUtil 클래스의 인스턴스 생성 없이도 메서드 호출이 가능하도록
	//    static 메서드로 정의
	public static Connection getConnection() {
		Connection con = null;
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/mvc_board5";
			String user = "root";
			String password = "1234";
			
			Class.forName(driver);
			System.out.println("드라이버 로드 성공!");
						
			con =  DriverManager.getConnection(url, user, password);
			System.out.println("DB 연결 성공!");
			
			con.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
	
	// 데이터베이스 작업에 대한 Commit, Rollback 기능을 수행할 메서드 정의
	// => 파라미터 : Connection 객체(con)   리턴타입 : void
	public static void commit(Connection con) {
		try {
			// Connection 객체의 commit() 메서드 호출하여 Commit 작업 수행
			con.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void rollback(Connection con) {
		try {
			// Connection 객체의 rollback() 메서드 호출하여 Rollback 작업 수행
			con.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// --------------------------------------------------------------------------
	// 데이터베이스 자원 반환(close())을 공통으로 수행할 close() 메서드 정의
	// => 파라미터 : Connection 타입(con), PreparedStatement(pstmt), ResultSet 타입(rs)
	// => 리턴타입 : void
	// => 각각의 파라미터를 따로 전달받아 각각 close() 작업을 수행하도록 메서드 정의
	//    이 때, 각 메서드의 이름은 모두 close() 로 통일하고 파라미터만 다르게 정의
	//    = 메서드 오버로딩(Method Overloading)
	// => 인스턴스 생성 없이도 메서드 호출이 가능하도록 static 메서드 정의
	public static void close(Connection con) {
		try {
			// 전달받은 Connection 객체 반환
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(PreparedStatement pstmt) {
		try {
			// 전달받은 PreparedStatement 객체 반환
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void close(ResultSet rs) {
		try {
			// 전달받은 ResultSet 객체 반환
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
