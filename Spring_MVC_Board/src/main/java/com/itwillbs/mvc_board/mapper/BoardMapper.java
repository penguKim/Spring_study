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

}
