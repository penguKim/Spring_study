package com.itwillbs.mvc_board.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.mvc_board.service.MemberService;
import com.itwillbs.mvc_board.vo.MemberVO;

@Controller
public class MemberController {
	// MemberService 객체 자동 주입
	@Autowired
	MemberService memberService;
	
	// [ 회원 가입 ]
	// "MemberJoinForm" 요청 joinForm() 메서드 정의(GET)
	// => "member/member_join_form.jsp" 페이지 포워딩
	@GetMapping("MemberJoinForm")
	public String joinForm() {
		return "member/member_join_form";
	}
	
	// "MemberJoinPro" 요청에 대한 비즈니스 로직 처리 수행할 joinPro() 메서드 정의(POST)
	// => 폼 파라미터를 통해 전달받은 회원정보를 MemberVO 타입으로 전달받기
	// => 데이터 공유 객체 Model 타입 파라미터 추가
	@PostMapping("MemberJoinPro")
	public String joinPro(MemberVO member, Model model) {
//		System.out.println(member);
		
		// ------------------------------------------------------------------------
		// BCryptPasswordEncoder 를 활용한 패스워드 암호화
		// 입력받은 패스워드는 암호화 필요 => 복호화가 불가능하도록 단방향 암호화(해싱) 수행
		// => 평문(Clear Text, Plain Text) 을 해싱 후 MemberVO 객체의 passwd 에 덮어쓰기
		// => org.springframework.security.crypto.bcrypt 패키지의 BCryptPasswordEncoder 클래스 활용
		//    (spring-security-crypto 또는 spring-security-web 라이브러리 추가)
		//    주의! JDK 11 일 때 5.x.x 버전 필요
		// => BCryptPasswordEncoder 활용한 해싱은 솔팅(Salting) 기법을 통해
		//    동일한 평문(원본 암호)이더라도 매번 다른 결과(암호문)를 얻을 수 있다!
		// 1. BCryptPasswordEncoder 클래스 인스턴스 생성
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// 2. BCryptPasswordEncoder 객체의 encode() 메서드를 호출하여
		//    원문(평문) 패스워드에 대한 해싱(= 암호화) 수행 후 결과값 저장
		//    => 파라미터 : MemberVO 객체의 패스워드(평문 암호)   리턴타입 : String(암호문)
		String securePasswd = passwordEncoder.encode(member.getPasswd());
//		System.out.println("평문 패스워드 : " + member.getPasswd()); // 1234
//		System.out.println("암호화 된 패스워드 : " + securePasswd); // $2a$10$wvO.wV.ZHbRFVr9q4ayFbeGGCRs7XKxsN8wmll/.5YFlA/N7hFYl2(매번 다름)
		// 3. 암호화 된 패스워드를 MemberVO 객체에 저장
		member.setPasswd(securePasswd);
		// -------------------------------------------------------------------------------------
		// MemberService - registMember() 메서드 호출하여 회원가입 작업 요청
		// => 파라미터 : MemberVO 객체   리턴타입 : int(insertCount)
		int insertCount = memberService.registMember(member);
		
		// 회원가입 성공/실패에 따른 페이징 처리
		// => 성공 시 "MemberJoinSuccess" 서블릿 주소 리다이렉트
		// => 실패 시 "fail_back.jsp" 페이지 포워딩("msg" 속성값으로 "회원 가입 실패!" 저장)
		
		if(insertCount > 0) {
			// -------- 인증메일 발송 작업 추가 --------
			
			return "redirect:/MemberJoinSuccess";
		} else {
			// 실패 시 메세지 출력 및 이전페이지로 돌아가는 기능을 모듈화 한 fail_back.jsp 페이지
			model.addAttribute("msg", "회원 가입 실패!");
			return "fail_back";
		}
	}
	
	// "MemberCheckDupId" 요청에 대한 아이디 중복 검사 비즈니스 로직 처리
	// 응답데이터를 디스패치 또는 리다이렉트 용도가 아닌 응답 데이터 body 로 그대로 활용하려면
	// @ResponseBody 어노테이션을 적용해야한다! => 응답 데이터를 직접 전송하도록 지정한다.
	// => 이 어노테이션은 AJAX 와 상관없이 적용 가능하지만 AJAX 일 때는 대부분 사용한다.
	@ResponseBody 
	@GetMapping("MemberCheckDupId")
	public String checkDupId(MemberVO member) {
		System.out.println(member.getId());
		
		// MemberService - getMember() 메서드 호출하여 아이디 조회(기존 메서드 재사용)
		// (MemberService - getMemberId() 메서드 호출하여 아이디 조회 메서드 정의 가능)
		// => 파라미터 : MemberVO 객체   리턴타입 : MemberVO(dbMember)
		MemberVO dbMember = memberService.getMember(member);
		
		// 조회 결과 판별
		// => MemberVO 객체가 존재할 경우 아이디 중복, 아니면 사용 가능한 아이디
		if(dbMember == null) { // 사용 가능한 아이디
			return "false"; // 중복이 아니라는 의미로 "false" 값 전달
		} else { // 아이디 중복
			return "true"; // 중복이라는 의미로 "true" 값 전달
		}
	}
	
	// ================================================================================
	// [ 로그인 ]
	// "MemberJoinSuccess" 요청에 대해 "member/member_join_success" 페이지 포워딩(GET)
	@GetMapping("MemberJoinSuccess")
	public String joinSuccess() {
		// 아이디 : hong, 비번 : 1111
		return "member/member_join_success";
	}
	
	// "MemberLoginForm" 요청에 대해 "member/login_form" 페이지 포워딩(GET)
	@GetMapping("MemberLoginForm")
	public String loginForm() {
		return "member/member_login_form";
	}
	 
	// "MemberLoginPro" 요청에 대한 비즈니스 로직 처리 수행할 loginPro() 메서드 정의(POST)
	// => 폼 파라미터를 통해 전달받은 회원정보를 MemberVO 타입으로 전달받기
	// => 세션 활용을 위해 HttpSession 타입 파라미터 추가
	// => 데이터 공유 객체 Model 타입 파라미터 추가
	@PostMapping("MemberLoginPro")
	public String loginPro(MemberVO member, HttpSession session, Model model) {
		System.out.println(member);
		
		// MemberService - getMember() 메서드 호출하여 회원 정보 조회 요청
		// => 파라미터 : MemberVO 객체   리턴타입 : MemberVO(dbMember)
		MemberVO dbMember = memberService.getMember(member);
//		System.out.println(dbMember);
		
		// BCryptPasswordEncoder 객체를 활용한 패스워드 비교
		// => 입력받은 패스워드(평문)와 DB 에 저장된 패스워드(암호문)는 직접적인 문자열 비교 불가
		// => 일반적인 경우 전달받은 평문과 DB 에 저장된 암호문을 복호화하여 비교하면 되지만
		//    단방향 암호화가 된 패스워드의 경우 평문을 암호화(해싱)하여 결과값을 비교해야함
		//    (단, 솔팅값이 서로 다르므로 직접적인 비교(String 의 equals())가 불가능)
		// => BCryptPasswordEncoder 객체의 matches() 메서드를 통한 비교 필수!
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		// => 단, 아이디가 일치하지 않을 경우(null 값 리턴됨) 또는 패스워드 불일치 시
		//    fail_back.jsp 페이지 포워딩("로그인 실패!")
		// null 값을 먼저 비교하지 않으면 NullPointException 발생
		if(dbMember == null || !passwordEncoder.matches(member.getPasswd(), dbMember.getPasswd())) {
			// 로그인 실패 처리
			model.addAttribute("msg", "로그인 실패!");
			return "fail_back";
		} else {
			// 세션 객체에 로그인 성공한 아이디를 "sId" 속성으로 추가
			session.setAttribute("sId", member.getId());
			// 메인페이지로 리다이렉트
			return "redirect:/";
		}
		
	}
	
	// "MemberLogout" 요청에 대한 로그아웃 비즈니스 로직 처리
	@GetMapping("MemberLogout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:/";
	}
	
	// ===============================================================
	// [ 회원 상세정보 조회 ]
	// "MemberInfo" 서블릿 요청 시 회원 상세정보 조회 비즈니스 로직 처리
	// 회원 정보 조회
	@GetMapping("MemberInfo")
	public String info(MemberVO member, HttpSession session, Model model) {
		// 세션 아이디가 없을 경우 "fail_back" 페이지를 통해 "잘못된 접근입니다" 출력 처리
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "잘못된 접근입니다!");
			return "fail_back";
		}
		
		member.setId(id);
		
		// MemberService - getMember() 메서드 호출하여 회원 상세정보 조회 요청
		// => 파라미터 : MemberVO 객체   리턴타입 : MemberVO(dbMember)
		MemberVO dbMember = memberService.getMember(member);
//		System.out.println(member);
		
		// 조회 결과 Model 객체에 저장
		model.addAttribute("member", dbMember);
		// 회원 상세정보 조회 페이지 포워딩
		return "member/member_info";
	}
	
	// 회원 정보 수정 폼
	@GetMapping("MemberModifyForm")
	public String modifyForm(MemberVO member, HttpSession session, Model model) {
		String id = (String)session.getAttribute("sId");
		
		if(id == null) {
			model.addAttribute("msg", "잘못된 접근입니다.");
			return "fail_back";
		}
		member.setId(id);
		
		MemberVO dbMember = memberService.getMember(member);
//		System.out.println(member);
		
		model.addAttribute("member", dbMember);
		
		return "member/member_modify_form";
	}
	
	// 회원 정보 수정 작업
	@PostMapping("MemberModifyPro")
	public String modifyPro(MemberVO member, HttpSession session, Model model) {
		
		String id = (String)session.getAttribute("sId");
		
		if(id == null) {
			model.addAttribute("msg", "잘못된 접근입니다.");
			return "fail_back";
		}
		
//		int updateCount = 0;
//		
//		if(member.getPasswd() == null) {
//			updateCount = memberService.modifyMember(member);
//		} else {
//			
//		}
		
		return "redirect:/MemberInfo";
	}
	
	// 회원 삭제 폼
	@GetMapping("MemberWithdrawForm")
	public String withdrawForm(HttpSession session, Model model) {
		
		String id = (String)session.getAttribute("sId");
		
		if(id == null) {
			model.addAttribute("msg", "잘못된 접근입니다.");
			return "fail_back";
		}
		
		return "member/member_withdraw_form";
	}
	
	// 회원 삭제 작업
	@PostMapping("MemberWithdrawPro")
	public String withdrawPro(MemberVO member, HttpSession session, Model model) {
		String id = (String)session.getAttribute("sId");
		
		if(id == null) {
			model.addAttribute("msg", "잘못된 접근입니다.");
			return "fail_back";
		}
		member.setId(id);
		
		MemberVO dbMember = memberService.getMember(member);
		
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		boolean isCorrect = passwordEncoder.matches(member.getPasswd(), dbMember.getPasswd());
		if(isCorrect) {
			
			int updateCount = memberService.modifyMember(dbMember);
			System.out.println(updateCount);
			if(updateCount > 0) {
				session.invalidate();
			}
		}
		
		
		
		
		return "redirect:/";
	}
	
	
	
}
