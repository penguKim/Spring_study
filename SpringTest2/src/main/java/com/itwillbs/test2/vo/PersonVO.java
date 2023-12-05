package com.itwillbs.test2.vo;

public class PersonVO {
	// 멤버변수 선언 필수!
	// 파라미터명과 멤버변수명이 일치하도록 선언
	private String name;
	private int age;
	
	// 생성자 정의 필수!
	// 기본 생성자는 다른 생성자를 정의하지 않을 경우 자동 생성
	
	// Getter/Setter 정의 필수!
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	
	
	@Override
	public String toString() {
		return "PersonVO [name=" + name + ", age=" + age + "]";
	}
	
	
	
}
