package com.itwillbs.mvc_board.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.mvc_board.HomeController;
import com.itwillbs.mvc_board.service.BankService;
import com.itwillbs.mvc_board.vo.ResponseTokenVO;
import com.itwillbs.mvc_board.vo.ResponseUserInfoVO;

@Controller
public class FintechController {
	@Autowired
	BankService bankService;
	
	// 로그 출력을 위한 기본 라이브러리(org.slf4j.Logger 타입) 변수 선언
	// => org.slf4j.LoggerFactory.getLogger() 메서드 호출하여 Logger 객체 리턴받아 사용 가능
	//    파라미터 : 로그를 사용하여 다룰 현재 클래스 지정(해당 클래스에서 발생한 로그로 처리)
	private static final Logger logger = LoggerFactory.getLogger(FintechController.class);
	// => Logger 객체의 다양한 로그 출력 메서드(info, debug, warn, error 등) 활용하여 로그 출력 가능
	//    (각 메서드는 로그의 심각도(레벨)에 따라 구별하는 용도로 사용)
	// -------------------------------------------------------------------------
	// "/FintechMain" 매핑 => fintech/main.jsp 페이지 포워딩
	
	@GetMapping("FintechMain")
	public String fintechMain(HttpSession session, Model model) {
		
		if(session.getAttribute("sId") == null) {
			model.addAttribute("msg", "로그인이 필요합니다!");
			// targetURL 속성명으로 로그인 폼 페이지 서블릿 주소 저장
			model.addAttribute("targetURL", "MemberLoginForm");
			return "forward";
		}
		
		// 랜덤값을 활용하여 32바이트 난수 생성 및 세션에 저장 후 메인페이지로 전달
		String rNum = RandomStringUtils.randomNumeric(32);
//		logger.info("난수 : " + rNum);
		
		session.setAttribute("state", rNum);
		
		return "fintech/main";
	}
	
	@GetMapping("callback")
	public String callback(@RequestParam Map<String, String> authResponse, HttpSession session, Model model) {
		// 콜백을 통해 전달되는 응답 데이터 3가지(code, scope, client_info) 파라미터값이
		// Map 객체에 자동으로 저장됨
		logger.info("authResponse" + authResponse.toString());
		
		// ----------------------------------------------------
		String id = (String)session.getAttribute("sId");
		if(id == null) {
			model.addAttribute("msg", "로그인이 필요합니다!");
			model.addAttribute("isClose", true); // 현재 창(서브 윈도우) 닫도록 명령
			return "fail_back";
		}
		// ----------------------------------------------------
		// 응답 데이터 중 state 값이 요청 시 사용된 값인지 판별
		if(session.getAttribute("state") == null || !session.getAttribute("state").equals(authResponse.get("state"))) {
			model.addAttribute("msg", "잘못된 요청입니다!");
			return "fail_back";
		}
		
		// 확인 완료된 세션의 state 값 삭제
		session.removeAttribute("state");
		// ----------------------------------------------------
		// 2.1.2. 토큰발급 API - 사용자 토큰 발급 API 요청
		// BankApiService - requestAccessToken() 메서드 호출
		// => 파라미터 : 토큰 발급 요청에 필요한 정보(인증코드 요청 결과 Map 객체)
		//    리턴타입 : ResponseTokenVO(responseToken)
		ResponseTokenVO responseToken = bankService.requestAccessToken(authResponse);
		// 요청이 실패해도 ResponseTokenVO 객체는 null값이 아니다.
		logger.info("엑세스토큰 : " + responseToken);
		
		// ResponseTokenVO 객체가 null이거나 엑세스토큰 값이 null 일 경우 에러 처리
		// "fail_back.jsp" 페이지로 포워딩 시 "isClose" 값을 true 로 설정하여 전달
		// state 값 갱신을 위해 "FintechMain" 서블릿 주소 설정
		if(responseToken == null || responseToken.getAccess_token() == null) {
			model.addAttribute("msg", "토큰 발급 실패! 다시 인증하세요!");
			model.addAttribute("isClose", true);
			model.addAttribute("targetURL", "FintechMain");
			return "forward";
		}
		
		// BankApiService - registAccessToken() 메서드 호출하여 토큰 관련 정보 저장 요청
		// => 파라미터 : 세선아이디, responseToken
		// => 만약, 하나의 객체로 전달할 경우(Map 객체 활용)
//		bankApiService.registAccessToken(id, responseToken);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("token", responseToken);
		bankService.registAccessToken(map);
		
		// 세션 객체에 엑세스 토큰(access_token), 사용자번호(user_seq_no) 저장
		session.setAttribute("access_token", responseToken.getAccess_token());
		session.setAttribute("user_seq_no", responseToken.getUser_seq_no());
		
		// "forward.jsp" 페이지 포워딩을 통해
		// "계좌 인증 완료" 메세지 출력 후 인증 창 닫고 "FintechUserInfo" 서블릿 요청
		model.addAttribute("msg", "계좌 인증 완료!");
		model.addAttribute("isClose", true);
		model.addAttribute("targetURL", "FintechUserInfo");
		
		return "forward";
	}
	
	// 2.2.1 사용자정보조회 API
	@GetMapping("FintechUserInfo")
	public String fintechUserInfo(HttpSession session, Model model) {
		// 세션아이디가 null 일 경우 로그인 페이지 이동 처리
		// 엑세스토큰이 null 일 경우 "계좌 인증 필수!" 메세지 출력 후 "forward.jsp" 페이지 포워딩
		if(session.getAttribute("sId") == null) {
			model.addAttribute("msg", "로그인 필수!");
			model.addAttribute("targetURL", "MemberLoginForm");
			return "forward";
		} else if(session.getAttribute("access_token") == null) {
			model.addAttribute("msg", "계좌 인증 필수!");
			model.addAttribute("targetURL", "FintechMain");
			return "forward";
		}
		
		// Map 객체에 세션에 저장된 엑세스 토큰(access_token)과 사용자번호(user_seq_no) 저장
		Map<String, String> map = new HashMap<String, String>();
		map.put("access_token", (String)session.getAttribute("access_token"));
		map.put("user_seq_no", (String)session.getAttribute("user_seq_no"));
		
		// 2.2. 사용자/서비스 관리 - 2.2.1. 사용자정보조회 API 요청
		// BankService - requestUserInfo() 메서드 호출하여 핀테크 사용자 정보조회 요청
		// => 파라미터 : Map 객체   리턴타입 : ResponseUserInfoVO(userInfo)
//		ResponseUserInfoVO userInfo = bankService.requestUserInfo(map);
		
		// 만약, 응답데이터를 Map 타입으로 처리할 경우(Map<String, Object> 타입 사용)
		Map<String, Object> userInfo = bankService.requestUserInfo(map);
		logger.info(">>>>>> userInfo : " + userInfo);
		
		// Model 객체에 ResponseUserInfoVO 객체 저장
		model.addAttribute("userInfo", userInfo);
		
		return "fintech/fintech_user_info";
	}
	
	// 2.3.1 잔액조회 API
	@PostMapping("BankAccountDetail")
	public String accountDetail(@RequestParam Map<String, String> map, HttpSession session, Model model) {
		if(session.getAttribute("sId") == null) {
			model.addAttribute("msg", "로그인 필수!");
			model.addAttribute("targetURL", "MemberLoginForm");
			return "forward";
		} else if(session.getAttribute("access_token") == null) {
			model.addAttribute("msg", "계좌 인증 필수!");
			model.addAttribute("targetURL", "FintechMain");
			return "forward";
		}		
		
		// 요청에 사용할 엑세스토큰을 Map 객체에 추가
		map.put("access_token", (String)session.getAttribute("access_token"));
		
		// BankService - requestAccountDetail() 메서드 호출하여 계좌 상세정보 조회 요청
		// => 파라미터 : Map 객체   리턴타입 : Map<String, Object>(accountDetail)
		Map<String, Object> accountDetail = bankService.requestAccountDetail(map);
		
		// 조회결과(Map 객체, 이름, 계좌번호) 저장
		model.addAttribute("accountDetail", accountDetail);
		model.addAttribute("user_name", map.get("user_name"));
		model.addAttribute("account_num_masked", map.get("account_num_masked"));
		
		
		return "fintech/fintech_account_detail";
	}
	
	// 2.5.1. 출금이체 API
	@PostMapping("BankPayment")
	public String bankPayment(@RequestParam Map<String, String> map, HttpSession session, Model model) {
//		logger.info(">>>>>>>>>>> payment : " + map);
		
		String id = (String)session.getAttribute("sId");
		// 세션아이디가 null 일 경우 로그인 페이지 이동 처리
		// 엑세스토큰이 null 일 경우 "계좌 인증 필수!" 메세지 출력 후 "forward.jsp" 페이지 포워딩
		if(id == null) {
			model.addAttribute("msg", "로그인 필수!");
			model.addAttribute("targetURL", "MemberLoginForm");
			return "forward";
		} else if(session.getAttribute("access_token") == null) {
			model.addAttribute("msg", "계좌 인증 필수!");
			model.addAttribute("targetURL", "FintechMain");
			return "forward";
		}	
		
		// 요청에 필요한 엑세스토큰과 세션 아이디를 Map 객체에 추가
		map.put("access_token", (String)session.getAttribute("access_token"));
		map.put("id", id);
		
		// BankService - requestWithdraw() 메서드 호출하여 상품 구매에 대한 지불(출금이체) 요청
		// => 파라미터 : Map 객체   리턴타입 : Map<String, Object>(withdrawResult)
		Map<String, Object> withdrawResult = bankService.requestWithdraw(map);
		logger.info(">>>>>>> withdrawResult : " + withdrawResult);
		// 요청 결과를 model 객체에 저장
		model.addAttribute("withdrawResult", withdrawResult);
		
		return "fintech/fintech_payment_result";
		
	}
	
}










