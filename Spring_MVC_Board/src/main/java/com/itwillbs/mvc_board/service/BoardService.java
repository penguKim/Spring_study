package com.itwillbs.mvc_board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itwillbs.mvc_board.mapper.BoardMapper;
import com.itwillbs.mvc_board.vo.BoardVO;

@Service
public class BoardService {
	@Autowired
	BoardMapper mapper;

	// 게시물 등록 요청
	public int registBoard(BoardVO board) {
		return mapper.insertBoard(board);
	}

	// 게시물 목록 조회
	public List<BoardVO> getBoardList(String searchType, String searchKeyword, int startRow, int listLimit) {
		return mapper.selectBoardList(searchType, searchKeyword, startRow, listLimit);
	}

	// 게시물 갯수 조회
	public int getBoardListCount(String searchType, String searchKeyword) {
		return mapper.selectBoardListCount(searchType, searchKeyword);
	}

	// 게시글 상세 조회
//	public BoardVO getBoard(int board_num) {
//		return mapper.selectBoard(board_num);
//	}
	
	// 단, 조회수 증가 여부를 컨트롤러에서 제어하기 위해 isIncreaseReadcount 파라미터 추가
	public BoardVO getBoard(int board_num, boolean isIncreaseReadcount) {
		BoardVO board = mapper.selectBoard(board_num);
		
		// 조회 결과가 존재하고 isIncreaseReadcount 가 true 일 경우 조회수 증가 작업 요청
		if(board != null && isIncreaseReadcount) {
			// 전달한 객체와 리턴할 객체가 동일하기에 sql 구문 실행후 증가한 조회수도 객체에 저장되어 반영된다.
			mapper.updateReadcount(board);
		}
		
		
		return board;
	}

	// 게시글 삭제 작업 요청
	public int removeBoard(BoardVO board) {
		return mapper.deleteBoard(board);
	}

	// 게시글 수정 작업 요청
	public int modifyBoard(BoardVO board) {
		return mapper.updateBoard(board);
	}


}
