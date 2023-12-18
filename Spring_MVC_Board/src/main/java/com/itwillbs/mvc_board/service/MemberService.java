package com.itwillbs.mvc_board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.mvc_board.mapper.MemberMapper;
import com.itwillbs.mvc_board.vo.MemberVO;

@Service
public class MemberService {
	// MemberMapper 객체 자동 주입
	@Autowired
	MemberMapper mapper;
	
	// 회원가입 작업 요청
	public int registMember(MemberVO member) {
		// MemberMapper(인터페이스) - insertMember()
		// => 파라미터 : MemberVO 객체   리턴타입 : int
		return mapper.insertMember(member);
	}

	// 회원 상세정보 조회 요청
	public MemberVO getMember(MemberVO member) {
		// MemberMapper(인터페이스) - selectMember()
		// => 파라미터 : MemberVO 객체   리턴타입 : MemberVO
		return mapper.selectMember(member);
	}
	
	// 회원 정보 수정
	public int modifyMember( MemberVO member, String newPasswd) {
		return mapper.updateMember(member, newPasswd);
	}
	
	// 회원 탈퇴 요청
	public int withdrawMember(MemberVO member) {
		return mapper.updateWithdrawMember(member);
	}

	// ==========================================================
	// [ 관리자 ]
	// 회원 목록 조회 요청
	public List<MemberVO> getMemberList() {
		return mapper.selectMemberList();
	}


}
