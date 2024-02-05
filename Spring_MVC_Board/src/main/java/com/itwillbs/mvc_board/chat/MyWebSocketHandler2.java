package com.itwillbs.mvc_board.chat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.Gson;
import com.itwillbs.mvc_board.vo.ChatMessage;
import com.itwillbs.mvc_board.vo.ChatMessage2;

//웹소켓 핸들링을 위한 클래스 정의 - TextWebSocketHandler 클래스 상속
//=> 이 클래스의 인스턴스는 서버 당 하나의 인스턴스만 생성됨
public class MyWebSocketHandler2 extends TextWebSocketHandler {
	
	// 접속한 클라이언트(사용자)들의 정보를 관리하기 위한 Map 객체 생성
	// key : 웹소켓 세션아이디(문자열)   value : 웹소켓 세션 객체(WebSocketSession)
	// => HashMap 구현제 대신 ConcurrentHashMap 타입 사용 시 멀티쓰레드 환경에서
	//    락(Lock)을 통해 안전하게 데이터 관리
	private Map<String, WebSocketSession> wsSessions = new ConcurrentHashMap<String, WebSocketSession>();
	
	// 웹소켓 세션아이디와 사용자 아이디 정보를 관리하기 위한 Map 객체ㅉ
	private Map<String, String> users = new ConcurrentHashMap<String, String>();

	// 채팅방 1개의 정보를 관리할 Map 객체 생성
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("웹소켓 연결됨!(afterConnectionEstablished)");
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("메세지 전송됨!(handleTextMessage)");
		
		System.out.println("전송받은 메세지 : " + message.getPayload());
		
		// [ 채팅 메세지 파싱- ChatMessage2 클래스 활용]
		Gson gson = new Gson();
		ChatMessage2 chatMessage = gson.fromJson(message.getPayload(), ChatMessage2.class);
		System.out.println("ChatMessage : " + chatMessage);		
		System.out.println("메세지 도착 시각 : " + getDateTimeForNow());
		
		// ----------------------------------------------------------------------
		// 사용자 아이디와 상대방 아이디를 변수에 저장
		String sender_id = chatMessage.getSender_id();
		String receiver_id = chatMessage.getReceiver_id();
		
		// ==========================================================
		// 메세지 타입 판별
		// 1) TYPE_INIT - 초기화
		 // 채팅 페이지 진입(로그아웃 후 로그인 및 새로고침 등 포함)
		if(chatMessage.getType().equals(ChatMessage2.TYPE_INIT)) {
			// 기존 사용자 목록에서 사용자 아이디와 일치하는 세션 가져온 후
			// 세션 목록에서 일치하는 세션 정보 제거
			// 단, 사용자 세션 목록이 하나라도 존재할 경우 수행
			if(users.size() > 0 && wsSessions.size() > 0) { 
				// users 객체의 키 중에서 사용자 아이디가 존재하고
				// wsSession 객체의 키 중에서 사용자 아이디에 해당하는 세션 아이디가 존재할 경우
				// 해당 세션 정보만 제거
				// => Map 객체의 users.containsKey() 메서드 활용
				if(users.containsKey(sender_id) && wsSessions.containsKey(users.get(sender_id))) {
					wsSessions.remove(users.get(sender_id));
				}
			}
			// 사용자 목록(users)에 자신의 아이디와 웹소켓 세션 아이디 저장
			users.put(sender_id, session.getId());
			// 세션 목록(wsSessions)에 세션 아이디와 세션 객체 저장
			wsSessions.put(session.getId(), session);
			
//			System.out.println("사용자 목록 : " + users);
//			System.out.println("사용자 목록 수 : " + users.size());
//			System.out.println("사용자 세션 목록 : " + wsSessions);
//			System.out.println("사용자 세션 목록 수 : " + wsSessions.size());
		} else if(chatMessage.getType().equals(ChatMessage2.TYPE_START)) {
			// 새 채팅방 생성 후 생성된 채팅방 정보 전송
			// 1. 새 채빙방의 방번호(room_id) 생성 - UUID 활용 또는 현재 웹소켓 세션 아이디 활용도 가능
			chatMessage.setRoom_id(UUID.randomUUID().toString());
			
			// 2. 사용자에게 채팅방 생성 메세지 전송을 위해 메세지 설정
			chatMessage.setMessage(receiver_id + " 님과의 채팅방 생성");
			
			// 채팅방 관련 정보 추가
			// --------------------------------------------
			// 사용자에게 새 채팅방 알림을 위한 메세지 전송
			// 채팅 메세지 타입을 LiIST_ADD 로 변경 후 메세지 전송
			chatMessage.setType(ChatMessage2.TYPE_LIST_ADD);
			sendMessage(session, chatMessage);
			
			// 채팅방 시작 정보를 알리기 위해 메세지 타입을 다시 START 로 변경
			chatMessage.setType(ChatMessage2.TYPE_START);
		}
		
		sendMessage(session, chatMessage);
		
	}
	
	// 채팅 메세지를 JSON 형식으로 변환하여 현재 세션에게 전송하는 메서드
	private void sendMessage(WebSocketSession session, ChatMessage2 chatMessage) throws IOException {
		Gson gson = new Gson();
		session.sendMessage(new TextMessage(gson.toJson(chatMessage)));
	}
	
	// 현재 시스템의 날짜 및 시각 정보를 문자열로 리턴하는 메서드 정의
	private String getDateTimeForNow() {
		LocalDateTime dateTIme = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		return dateTIme.format(dtf);
		
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("웹소켓 오류 발생!(handleTransportError)");
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		System.out.println("웹소켓 연결 해제됨!(afterConnectionClosed)");
	}
	

	
	
	
	
}
