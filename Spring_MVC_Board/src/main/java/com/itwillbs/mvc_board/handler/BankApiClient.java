package com.itwillbs.mvc_board.handler;

import java.net.URI;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.itwillbs.mvc_board.vo.ResponseTokenVO;
import com.itwillbs.mvc_board.vo.ResponseUserInfoVO;

@Component
public class BankApiClient {
	@Autowired
	private BankValueGenerator bankValueGenerator; 
	
	private static final Logger logger = LoggerFactory.getLogger(BankApiClient.class);

	// 2.1.2. 토큰발급 API - 사용자 토큰 발급 API 요청
	// https://testapi.openbanking.or.kr/oauth/2.0/token(=> POST 방식)
	public ResponseTokenVO requestAccessToken(Map<String, String> authResponse) {
		// 금융결제원 오픈API 요청 작업 처리
		// POST 방식 요청을 수행하기 위해 URL 정보 생성
		// 자기자신을 리턴하며 객체를 만들어나가는 빌더 패턴
		URI uri = UriComponentsBuilder
					.fromUriString("https://testapi.openbanking.or.kr/oauth/2.0/token") // 기본 주소
					.encode()
					.build() // UriComponents 객체 생성
					.toUri();
		
		// POST 방식 요청의 경우 파라미터(데이터)를 URL 에 결합하지 않고
		// 별도로 body 에 포함시켜 전달해야한다.
		// 따라서, 해당 파라미터 데이터를 별도의 객체로 생성 필요
		// 요청에 필요한 파라미터를 LinkedMultiValueMap 객체 활용하여 설정
		LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<String, String>();
		// LinkedMultiValueMap 객체의 add() 메서드를 호출하여 파라미터 전달(키, 값 형식으로 설정)
		parameters.add("code", authResponse.get("code")); // 인증코드(인증코드 요청을 통해 응답받은 데이터)
		parameters.add("client_id", "4066d795-aa6e-4720-9383-931d1f60d1a9");
		parameters.add("client_secret", "36b4a668-94ba-426d-a291-771405e498e4");
		parameters.add("redirect_uri", "http://localhost:8081/mvc_board/callback");
		parameters.add("grant_type", "authorization_code");
		
		// HttpEntity 객체를 활용하여 요청에 사용될 파라미터를 관리하는 객체를 요청 형식에 맞게 변환
		// => 헤더 정보와 바디 정보를 함꼐 관리해 주는 객체
		// => 제네릭타입은 파라미터를 관리하는 객체 타입으로 지정하고
		//    생성자 파라미터로 파라미터 관리 객체 전달
		// => 바디 정보만 설정하고 헤더 정보는 기본 헤더 사용하므로 헤더 생략
		HttpEntity<LinkedMultiValueMap<String, String>> httpEntity = 
					new HttpEntity<LinkedMultiValueMap<String,String>>(parameters);
		
		// REST API 요청을 위해 RestTemplate 객체를 활용하여 요청 수행
		// 1) RestTemplate 객체 생성
		RestTemplate restTemplate = new RestTemplate();
		// 2) TestTemplate 객체의 exchange() 메서드 호출하여 POST 방식 요청 수행 가능
		//    => 파라미터 : 요청 URL을 관리하는 URI 객체, 요청 메서드(httpMethod.XXX)
		//               요청 정보(해더, 바디)를 관리하는 HttpEntity 객체
		//               요청에 대한 응답 전달 시 JSON 타입의 응답 데이터를 자동으로 파싱할 클래스
		//    => 이 메서드 호출 시점에 실제 HTTP 요청 발생함
		//    리턴타입 : 응답 정보를 관리할 ResponseEntity<T> 타입이며, 
		//               이 때 제네릭 타입은 XXX.class 로 지정한 클래스 타입 지정
		//               (주의! JSON 타입 응답 데이터 자동 파싱을 위해 Gson, Jackson 등 라이브러리 필요)
		//               (자동 파싱 불가능할 경우 org.springframework.web.client.UnknownContentTypeException: 
		//                Could not extract response: no suitable HttpMessageConverter found for response type [class com.itwillbs.mvc_board.vo.ResponseTokenVO] and content type [application/json;charset=UTF-8] 예외 발생)
		ResponseEntity<ResponseTokenVO> responseEntity = 
				restTemplate.exchange(uri, HttpMethod.POST, httpEntity, ResponseTokenVO.class);
		
		// 응답 정보 확인(ResponseEntity 객체 메서드 활용)
		logger.info("응답 코드 : " + responseEntity.getStatusCode());
		logger.info("응답 헤더 : " + responseEntity.getHeaders());
		logger.info("응답 본문 : " + responseEntity.getBody());
		
		// ResponseEntity 객체의 getBody() 메서드를 호출하여 요청에 대한 응답 결과 리턴
		// => 리턴타입 : ResponseEntity 객체가 관리하는 제네릭타입(ResponseTokenVO)
		return responseEntity.getBody();
	}

	// 2.2. 사용자/서비스 관리 - 2.2.1. 사용자정보조회 API 요청(GET)
	// https://testapi.openbanking.or.kr/v2.0/user/me
//	public ResponseUserInfoVO requestUserInfo(Map<String, String> map) {
	public Map<String, Object> requestUserInfo(Map<String, String> map) {
//		logger.info("requestUserInfo() - 파라미터 : " + map);
		
		// GET 방식 요청에 대한 헤더 정보(엑세스 토큰)와 파라미터 설정
		// 1. 사용자 정보 조회 시 엑세스 토큰 값을 헤더에 담아 전송하므로
		//    org.springframework.http.HttpHeaders 객체 생성 후 
		//    add() 메서드를 통해 헤더에 정보 추가
		HttpHeaders headers = new HttpHeaders();
		// 헤더명 : "Authorization", 헤더값 : "Bearer " 문자열 뒤에 엑세스토큰 결합(공백으로 구분)
		headers.add("Authorization", "Bearer " + map.get("access_token"));
		
		// 2. 헤더 정보를 갖는 HttpEntity 객체 생성(제네릭타입은 String 지정)
		//    => 파라미터 : 헤더 정보가 저장되어 있는 HttpHeaders 객체
		//    => GET 방식 요청에서는 파라미터만 존재할 경우 body 생략이 가능함
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		
		// 3. 요청에 필요한 URI 정보 생성
		//    => GET 방식 요정에 사용할 파라미터는 UriComponentsBuilder 클래스의 queryParam() 메서드 사용
		URI uri = UriComponentsBuilder
					.fromUriString("https://testapi.openbanking.or.kr/v2.0/user/me")
					.queryParam("user_seq_no", map.get("user_seq_no")) // 파라미터(사용자번호)
					.encode() // 파라미터에 대한 인코딩 처리
					.build() // UriComponents 객체 생성
					.toUri(); // URI 타입 객체로 변환
		
		// 4. RestTemplate 객체 생성
		RestTemplate restTemplate = new RestTemplate();
		
		// 5. RestTemplate 객체의 exchange() 메서드 호출하여 HTTP 요청 수행(GET 방식)
		// => 파라미터 : URI 객체, 요청방식, HttpEntity 객체, 응답데이터 관리 클래스(ResponseUserInfoVO)
		// => 리턴타입 : ResponseEntity<ResponseUserInfoVO>
//		ResponseEntity<ResponseUserInfoVO> responseEntity
//				= restTemplate.exchange(uri, HttpMethod.GET, httpEntity, ResponseUserInfoVO.class);
		
		// 만약, 응답데이터를 Map 타입으로 처리할 경우
		// => 응답 처리 클래스 타입을 ParameterizedTypeReference 클래스의 익명 객체 생성 형태로
		//    제네릭타입을 Map<String, Object> 타입으로 지정
		ResponseEntity<Map<String, Object>> responseEntity
				= restTemplate.exchange(uri, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
		
		// ResponseEntity 객체의 getBody() 메서드 호출하여 응답 데이터 리턴
		return responseEntity.getBody();
	}

	// 2.3.1 잔액조회 API 요청(GET)
	// https://testapi.openbanking.or.kr/v2.0/account/balance/fin_num
	public Map<String, Object> requestAccountDetail(Map<String, String> map) {
		// 파라미터로 사용할 난수 생성하여 리턴받기
		String bank_tran_id = bankValueGenerator.getBankTranId();
//		logger.info(">>>>>>>>> 은행거래고유번호(bank_tran_id) : " + bank_tran_id);
		
		String tran_dtime = bankValueGenerator.getBankTranDTime();
//		logger.info(">>>>>>>>> 요청일시(tran_dtime) : " + tran_dtime);
		
		// GET 방식 요청에 대한 헤더 정보(엑세스 토큰)와 파라미터 설정
		// 1. 사용자 정보 조회 시 엑세스 토큰 값을 헤더에 담아 전송하므로
		//    org.springframework.http.HttpHeaders 객체 생성 후 
		//    add() 메서드를 통해 헤더에 정보 추가
		HttpHeaders headers = new HttpHeaders();
		// 헤더명 : "Authorization", 헤더값 : "Bearer " 문자열 뒤에 엑세스토큰 결합(공백으로 구분)
		headers.add("Authorization", "Bearer " + map.get("access_token"));
		
		// 2. 헤더 정보를 갖는 HttpEntity 객체 생성(제네릭타입은 String 지정)
		//    => 파라미터 : 헤더 정보가 저장되어 있는 HttpHeaders 객체
		//    => GET 방식 요청에서는 파라미터만 존재할 경우 body 생략이 가능함
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		
		// 3. 요청에 필요한 URI 정보 생성
		//    => GET 방식 요정에 사용할 파라미터는 UriComponentsBuilder 클래스의 queryParam() 메서드 사용
		URI uri = UriComponentsBuilder
					.fromUriString("https://testapi.openbanking.or.kr/v2.0/account/balance/fin_num")
					.queryParam("bank_tran_id", bank_tran_id) // 거래고유번호(참가기관)
					.queryParam("fintech_use_num", map.get("fintech_use_num")) // 핀테크이용번호
					.queryParam("tran_dtime", tran_dtime) // 요청일시
					.encode() // 파라미터에 대한 인코딩 처리
					.build() // UriComponents 객체 생성
					.toUri(); // URI 타입 객체로 변환
		
		// 4. RestTemplate 객체 생성
		RestTemplate restTemplate = new RestTemplate();
		
		// 5. RestTemplate 객체의 exchange() 메서드 호출하여 HTTP 요청 수행(GET 방식)
		// => 파라미터 : URI 객체, 요청방식, HttpEntity 객체, 응답데이터 관리 클래스(ResponseUserInfoVO)
		// => 리턴타입 : ResponseEntity<Map<String, Object>>
		ResponseEntity<Map<String, Object>> responseEntity
				= restTemplate.exchange(uri, HttpMethod.GET, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
		
		// ResponseEntity 객체의 getBody() 메서드 호출하여 응답 데이터 리턴
		return responseEntity.getBody();
	}

	// 2.5. 계좌이체 서비스
	// 2.5.1. 출금이체 API
	// https://testapi.openbanking.or.kr/v2.0/transfer/withdraw/fin_num
	public Map<String, Object> requestWithdraw(Map<String, String> map) {
		
		// GET 방식 요청에 대한 헤더 정보(엑세스 토큰)와 파라미터 설정
		// 1. 사용자 정보 조회 시 엑세스 토큰 값을 헤더에 담아 전송하므로
		//    org.springframework.http.HttpHeaders 객체 생성 후 
		//    add() 메서드를 통해 헤더에 정보 추가
		HttpHeaders headers = new HttpHeaders();
		// 1-1. 엑세스 토큰 전달
		// 헤더명 : "Authorization", 헤더값 : "Bearer " 문자열 뒤에 엑세스토큰 결합(공백으로 구분)
//		headers.add("Authorization", "Bearer " + map.get("access_token"));
		// 인증에 사용할 토큰을 Bearer 형식으로 Access Token 전달 시 setBearerAuth() 메서드 활용 가능
		headers.setBearerAuth(map.get("access_token"));
		
		// 1-2. 컨텐츠 타입 설정(변경)
		// => setContentType() 메서드를 호출하여 변경할 컨텐츠 타입 설정
		// => 기본값 : application/x-www-form-urlencoded; charset=UTF-8
		// => 만약, JSON 타입으로 변경할 경우 MediaType 클래스의 APPLICATION_JSON 상수 활용
		//    (주의! APPLICATION_JSON_UTF8 은 사용하지 않음 => deprecated 처리됨)
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		// 2. 요청에 필요한 URL 정보 생성 => 문자열로 바로 지정
		String url = "https://testapi.openbanking.or.kr/v2.0/transfer/withdraw/fin_num";
		
		// 3. 요청 파라미터를 JSON 형식 데이터로 생성(기본 라이브러리 org.json 활용)
		// 3.1) 출금 정보를 저장할 JSONObject 객체 생성
		JSONObject jo = new JSONObject();
		
		// 3-2) JSONObject 객체의 put() 메서드 호출하여 요청 파라미터 저장
		jo.put("bank_tran_id", bankValueGenerator.getBankTranId()); // 거래고유번호(자동생성)
		
		// ---------- 핀테크 이용기관 정보 ----------
		// 약정계좌번호를 약정계좌관리 메뉴의 "출금계좌" 항목에 등록 필수!
		jo.put("cntr_account_type", "N"); // 약정 계좌/계정 구분("N" : 계좌, "C" : 계정 => N 고정)
		jo.put("cntr_account_num", "23062003999");
		jo.put("dps_print_content", map.get("id")); // 입금계좌인자내역(결제 요청 사용자 아이디 확인으로 활용)
		
		// ---------- 요청 고객(출금 대상) 정보 ----------
		jo.put("fintech_use_num", map.get("fintech_use_num")); // 출금계좌 핀테크이용번호
		jo.put("wd_print_content", "아이티윌_송금"); // 출금계좌인자내역(이용기관에서 이용계약 시 설정하는 통장기재내용)
		jo.put("tran_amt", map.get("tran_amt")); // 거래금액
		jo.put("tran_dtime", bankValueGenerator.getBankTranDTime()); // 요청일시(자동생성)
		jo.put("req_client_name", map.get("req_client_name")); // 요청고객성명(출금계좌 예금주명)
		jo.put("req_client_fintech_use_num", map.get("fintech_use_num")); // 요청고객 핀테크이용번호(출금계좌)
		// => 주의! 은행기관코드&계좌번호 또는 핀테크이용번호 둘 중 하나만 설정 
		jo.put("req_client_num", map.get("id").toUpperCase()); // 요청고객 회원번호(아이디 활용) => 단, 영문자는 모두 대문자 사용 
		jo.put("transfer_purpose", "ST"); // 이체용도(송금(TR), 결제(ST), 충전(RC) 등) 
		
		// ---------- 수취 고객(실제 최종 입금 대상) 정보 ----------
		// 최종적으로 이 돈을 수신하는 계좌에 대한 정보
		// 이 정보(3개)는 피싱 등의 사고 발생 시 지급 정지를 위한 정보(현재 오픈뱅킹에서 검증수행은 X)
		// 이체용도 필드값이 송금(“TR”) 및 결제(“ST”)인 경우 이 값을 설정한다.
		jo.put("recv_client_name", "이연태"); // 
		jo.put("recv_client_bank_code", "004"); //
		jo.put("recv_client_account_num", "23062003999"); // 
		
		logger.info(">>>>> 출금 이체 요청 JSON 데이터" + jo.toString());
		
		// 4. HttpEntity 객체 생성(제네릭타입은 String 지정)
		//    => 파라미터 : 바디 정보로 사용할 JSON 데이터(문자열로 변환하여 전달),
		//                  헤더 정보가 저장되어 있는 HttpHeaders 객체
		//    => GET 방식 요청에서는 파라미터만 존재할 경우 body 생략이 가능함
		HttpEntity<String> httpEntity = new HttpEntity<String>(jo.toString(), headers);
		
		// 5. RestTemplate 객체 생성
				RestTemplate restTemplate = new RestTemplate();
				
		// 5. RestTemplate 객체의 exchange() 메서드 호출하여 HTTP 요청 수행(POST 방식)
		// => 파라미터 : URL(문자열), 요청방식, HttpEntity 객체, 응답데이터 관리 클래스(ResponseUserInfoVO)
		// => 리턴타입 : ResponseEntity<Map<String, Object>>
		ResponseEntity<Map<String, Object>> responseEntity
				= restTemplate.exchange(url, HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
		
		// ResponseEntity 객체의 getBody() 메서드 호출하여 응답 데이터 리턴
		return responseEntity.getBody();
		
		/*
		 * [ 테스트 데이터(출금이체) 등록 방법 ]
		 * 사용자 일련번호, 핀테크 이용번호 : 자신(요청 고객)의 정보 선택
		 * => 출금기관 대표코드, 출금 계좌번호 자동으로 입력됨
		 * 송금인 실명 : req_client_name 값 입력(자신(요청 고객)의 정보 입력)
		 * 거래금액 : tran_amt 값 입력
		 * 입금계좌 인자내역 : dps_print_content 값 입력(고객 아이디로 전달하도록 설정되어 있음)
		 * 수취인 성명 : 핀테크 이용기관의 계좌 예금주명 입력(최종 수취인과 다를 수 있음!)
		 */
		
		
	}
	
}
