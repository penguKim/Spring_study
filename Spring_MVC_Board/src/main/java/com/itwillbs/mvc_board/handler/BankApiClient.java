package com.itwillbs.mvc_board.handler;

import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
	
}
