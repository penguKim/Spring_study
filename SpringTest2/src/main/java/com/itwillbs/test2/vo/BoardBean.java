package com.itwillbs.test2.vo;

import java.sql.Timestamp;

/*
mvc_board5.board 테이블 생성
------------------------------------
글번호(board_num) - 정수, PK
작성자(board_name) - 문자열(16), NN
제목(board_subject) - 문자열(100), NN
내용(board_content) - 문자열(2000), NN
참조글번호(board_re_ref) - 정수, NN
들여쓰기레벨(board_re_lev) - 정수, NN
순서번호(board_re_seq) - 정수, NN
조회수(board_readcount) - 정수, NN
작성일(board_date) - 날짜 및 시각, NN
작성자 IP주소(writer_ip) - 문자열(20), NN
-------------------------------------
CREATE TABLE board (
	board_num INT PRIMARY KEY,
 	board_name VARCHAR(16) NOT NULL,
 	board_subject VARCHAR(50) NOT NULL,
 	board_content VARCHAR(2000) NOT NULL,
 	board_re_ref INT NOT NULL,
 	board_re_lev INT NOT NULL,
 	board_re_seq INT NOT NULL,
 	board_readcount INT NOT NULL,
 	board_date DATETIME NOT NULL,
 	writer_ip VARCHAR(20) NOT NULL
);
*/
public class BoardBean {
	private int board_num;
	private String board_name;
	private String board_subject;
	private String board_content;
	private int board_re_ref;
	private int board_re_lev;
	private int board_re_seq;
	private int board_readcount;
	private Timestamp board_date;
	private String writer_ip;
	
	public int getBoard_num() {
		return board_num;
	}
	public void setBoard_num(int board_num) {
		this.board_num = board_num;
	}
	public String getBoard_name() {
		return board_name;
	}
	public void setBoard_name(String board_name) {
		this.board_name = board_name;
	}
	public String getBoard_subject() {
		return board_subject;
	}
	public void setBoard_subject(String board_subject) {
		this.board_subject = board_subject;
	}
	public String getBoard_content() {
		return board_content;
	}
	public void setBoard_content(String board_content) {
		this.board_content = board_content;
	}
	public int getBoard_re_ref() {
		return board_re_ref;
	}
	public void setBoard_re_ref(int board_re_ref) {
		this.board_re_ref = board_re_ref;
	}
	public int getBoard_re_lev() {
		return board_re_lev;
	}
	public void setBoard_re_lev(int board_re_lev) {
		this.board_re_lev = board_re_lev;
	}
	public int getBoard_re_seq() {
		return board_re_seq;
	}
	public void setBoard_re_seq(int board_re_seq) {
		this.board_re_seq = board_re_seq;
	}
	public int getBoard_readcount() {
		return board_readcount;
	}
	public void setBoard_readcount(int board_readcount) {
		this.board_readcount = board_readcount;
	}
	public Timestamp getBoard_date() {
		return board_date;
	}
	public void setBoard_date(Timestamp board_date) {
		this.board_date = board_date;
	}
	public String getWriter_ip() {
		return writer_ip;
	}
	public void setWriter_ip(String writer_ip) {
		this.writer_ip = writer_ip;
	}
	
	@Override
	public String toString() {
		return "BoardBean [board_num=" + board_num + ", board_name=" + board_name + ", board_subject=" + board_subject
				+ ", board_content=" + board_content + ", board_re_ref=" + board_re_ref + ", board_re_lev="
				+ board_re_lev + ", board_re_seq=" + board_re_seq + ", board_readcount=" + board_readcount
				+ ", board_date=" + board_date + ", writer_ip=" + writer_ip + "]";
	}
	
}
