package com.itwillbs.test2.vo;

public class TestVo {
	private String subject;
	private String content;
	// 기본 생성자
	public TestVo() {}
	// 파라미터 생성자
	public TestVo(String subject, String content) {
		super();
		this.subject = subject;
		this.content = content;
	}
	// Getter/Setter 정의
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	// toString 정의
	@Override
	public String toString() {
		return "TestVo [subject=" + subject + ", content=" + content + "]";
	}
	
	
	
}
