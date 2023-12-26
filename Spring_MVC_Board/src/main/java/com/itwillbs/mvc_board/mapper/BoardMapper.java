package com.itwillbs.mvc_board.mapper;

import java.util.List;

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
}
