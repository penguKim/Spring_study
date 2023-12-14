package com.itwillbs.mvc_board.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.mvc_board.vo.MemberVO;

@Mapper
public interface MemberMapper {

	// 회원 가입
	int insertMember(MemberVO member);
	
	// 회원 상세정보 조회
	MemberVO selectMember(MemberVO member);

	int updateMember(MemberVO member);

}
