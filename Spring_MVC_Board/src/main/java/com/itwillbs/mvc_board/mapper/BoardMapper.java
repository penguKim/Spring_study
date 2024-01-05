package com.itwillbs.mvc_board.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.mvc_board.vo.BoardVO;

@Mapper
public interface BoardMapper {

	// 게시물 등록 작업
	int insertBoard(BoardVO board);

	// 게시물 목록 조회 작업
	List<BoardVO> selectBoardList(@Param("searchType") String searchType, 
								  @Param("searchKeyword") String searchKeyword, 
								  @Param("startRow") int startRow, 
								  @Param("listLimit") int listLimit);
	// 게시물 갯수 조회 작업
	int selectBoardListCount(@Param("searchType") String searchType, 
							 @Param("searchKeyword") String searchKeyword);

	// 게시글 상세 조회 작업
	BoardVO selectBoard(int board_num);

	// 조회수 증가 작업
	void updateReadcount(BoardVO board);

	// 게시글 삭제 작업
	int deleteBoard(BoardVO board);


	// 게시물 수정 - 파일 삭제 작업
	int updateBoardFile(BoardVO board);

	// 게시글 수정 작업
	int updateBoard(BoardVO board);

	// 기존 답글 순서번호 조정
	void updateBoardReSeq(BoardVO board);
	
	// 답글 등록 작업
	int insertReplyBoard(BoardVO board);

	// 댓글 등록 작업
	int insertTinyReplyBoard(Map<String, String> map);

	// 댓글 목록 조회 작업
	List<Map<String, Object>> selectTinyReplyBoardList(int board_num);
//	List<Map<String, String>> selectTinyReplyBoardList(int board_num);

	// 댓글 작성자 조회
	Map<String, String> selectTinyReplyWriter(Map<String, String> map);
	
	// 댓글 삭제 작업
	int deleteTinyReplyBoard(Map<String, String> map);

	// 기존 댓글들의 순서 조정
	void updateTinyReplyBoardSeq(Map<String, String> map);
	
	// 대댓글 작성 작업
	int insertTinyReReplyBoard(Map<String, String> map);


}
