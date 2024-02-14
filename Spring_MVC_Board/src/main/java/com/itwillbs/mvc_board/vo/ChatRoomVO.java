package com.itwillbs.mvc_board.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/*
 채팅방 목록 중 1개의 채팅방 정보를 관리
 [ chat_room_list 테이블 생성 ]
 CREATE TABLE chat_room_list(
 	room_id VARCHAR(50) NOT NULL,
 	sender_id VARCHAR(16) NOT NULL,
 	receiver_id VARCHAR(16) NOT NULL,
 	title VARCHAR(50) NOT NULL,
 	status VARCHAR(16)
  );
 */
@Data
@AllArgsConstructor
public class ChatRoomVO {
	private String room_id; // 5ac751f8-6699-443e-85eb-cc397e4b159d
	private String sender_id; // 16글자
	private String receiver_id; // 16글자
	private String title; // 50글자
	private String status; // 16글자
}
