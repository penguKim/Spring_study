package com.itwillbs.test2.controller;



import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.test2.svc.MemberJoinProService;
import com.itwillbs.test2.svc.MemberLoginProService;
import com.itwillbs.test2.vo.MemberBean;

@Controller
public class MemberController {

	@GetMapping("MemberJoinForm")
	public String joinForm() {
		System.out.println("회원가입");
		
		return "member/member_join_form";
	}
	
	@GetMapping("MemberLoginForm")
	public String loginForm() {
		System.out.println("로그인");
		
		return "member/member_login_form";
	}
	
	// "MemberJoinPro" 서블릿 주소 매핑 - joinPro()
	// => 전달받은 모든 파라미터 확인
	// => 메인페이지로 리다이렉트
	// 1) 파라미터명과 일치하는 변수를 파라미터로 선언
//	@PostMapping("MemberJoinPro")
//	public String joinPro(String name, String id) {
//		System.out.println("이름 : " + name);
//		System.out.println("아이디 : " + id);
//		
//		return "redirect:/main";
//	}
	
	// 2) MemberBean 타입을 파라미터로 선언
	@PostMapping("MemberJoinPro")
	public String joinPro(MemberBean member, String jumin1, String jumin2, 
			String postCode, String address1, String address2, String email1, String email2) {
		
		// 임시) 파라미터명이 다른 파라미터들을 하나로 결합하여 저장
		// 주민번호 결합하여 MemberBean - jumin 멤버변수에 저장
		// 주소 결합하여 MemberBean - address 멤버변수에 저장
		// 이메일 결합하여 MemberBean - email 멤버변수에 저장
		// 취미항복은 배열로 관리되는데 이것도 스프링에서 자동 관리 후 저장 
		member.setJumin(jumin1 + "-" + jumin2);
		member.setAddress(postCode + "/" + address1 + "/" + address2);
		member.setEmail(email1 + "@" + email2);
//		System.out.println(member);
		
		MemberJoinProService service = new MemberJoinProService();
		boolean isJoinSuccess = service.registMember(member);
		
		return "redirect:/main";
	}
	
	// 3) Map 타입을 파라미터로 선언(주의! @RequestParam 어노테이션 필수!)
//	@PostMapping("MemberJoinPro")
//	public String joinPro(@RequestParam Map<String, String> map) {
//		System.out.println("이름 : " + map.get("name"));
//		System.out.println("아이디 : " + map.get("id"));
//		System.out.println("패스워드 : " + map.get("passwd"));
//		
//		return "redirect:/main";
//	}
	
	
	// =========================================================
	// "MemberLoginPro" 서블릿 주소 매핑 - loginPro()
	// => 전달받은 아이디, 패스워드 파라미터 확인
	// => 메인페이지 리다이렉트
	@PostMapping("MemberLoginPro")
	public String loginPro(MemberBean member, HttpSession session, Model model) {
//		System.out.println(member);
		
		// MemberLoginProService - isCorrectUser() 메서드 호출하여 로그인 판별 작업 요청
		// => 파라미터 : MemberBean 객체   리턴타입 : boolean(isCorrectUser)
		MemberLoginProService service = new MemberLoginProService();
		boolean isCorrectUser = service.isCorrectUser(member);
		
		// 로그인 결과 판별
		// => 실패 시 "views/fail_back.jsp" 페이지로 포워딩
		// => 성공 시 세션에 아이디 저장 후 메인페이지로 리다이렉트
		if(!isCorrectUser) { // 실패
			// fail_back 페이지 포워딩 시 오류 메세지(데이터)를 함께 전달
			// => Model 객체에 "로그인 실패!" 문자열을 "msg" 라는 속성명으로 저장
			model.addAttribute("msg", "로그인 실패!");
			
			return "fail_back"; // 디스패치(객체 형태로 데이터 전송 가능)
		} else { // 성공
			// 세션 객체를 사용하여 아이디를 저장하려면
			// 매핑 메서드 정의 시 파라미터로 HttpSession 타입 변수 선언 시 자동으로 객체 전달됨
			session.setAttribute("sId", member.getId());
			
			return "redirect:/main";
		}
	}
	
}
