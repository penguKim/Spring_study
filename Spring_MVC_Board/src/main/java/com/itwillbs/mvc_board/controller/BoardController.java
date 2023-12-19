package com.itwillbs.mvc_board.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.mvc_board.service.BoardService;
import com.itwillbs.mvc_board.vo.BoardVO;

@Controller
public class BoardController {
	@Autowired
	BoardService boardService;
	
	// ==================================================================================
	@GetMapping("BoardWriteForm")
	public String writeForm(HttpSession session, Model model) {
		// 세션 아이디 없을 경우 "로그인이 필요합니다" 처리를 위해 "forward.jsp" 페이지 포워딩
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "로그인이 필요합니다!");
			// targetURL 속성명으로 로그인 폼 페이지 서블릿 주소 저장
			model.addAttribute("targetURL", "MemberLoginForm");
			return "forward";
		}
		return "board/board_write_form";
	}
	
	
	// "BoardWritePro" 서블릿 요청에 대한 글쓰기 비즈니스 로직 처리
	// 주의! 파일 업로드를 위한 multipart/form-data 타입 지정 시
	// 일반적인 request 객체를 통한 파라미터 접근이 불가능하다!
	// => JSP 등을 통해 접근 시 MultipartRequest 객체등을 활용하여 별도의 처리를 수행해야한다!
//	@PostMapping("BoardWritePro")
//	public String writePro(HttpServletRequest request) {
//		// enctype="multipart/form-data" 속성으로 파라미터 전달시 request 객체로는 받아올 수 없다.
//		System.out.println("board_name : " + request.getParameter("board_name"));
//		return "";
//	}
	
	// 스프링의 경우 파라미터 매핑을 자동으로 수행하므로 별도로 수행할 추가작업은 없으나
	// 다만, 파일 처리를 위해서는 MultipartFile 등의 타입을 통해 추가 처리는 필요함
	// => 파일 업로드에 사용되는 모든 파라미터를 BoardVO 타입으로 처리
	// => file1=MultipartFile[] 형식으로 업로드 된 파일이 별도의 객체로 관리됨
	@PostMapping("BoardWritePro")
	public String writePro(BoardVO board, HttpSession session, Model model, HttpServletRequest request) {
//		System.out.println(board);
		String sId = (String)session.getAttribute("sId");
		if(sId == null) {
			model.addAttribute("msg", "로그인이 필요합니다!");
			// targetURL 속성명으로 로그인 폼 페이지 서블릿 주소 저장
			model.addAttribute("targetURL", "MemberLoginForm");
			return "forward";
		}
		
		// ---------------------------------------------------
		// 작성자 IP 주소 가져오기
		board.setWriter_ip(request.getRemoteAddr()); // IPv6 방식으로 가져온다.
		System.out.println(board.getWriter_ip()); // 0:0:0:0:0:0:0:1
		// ---------------------------------------------------
		// 실제 파일 업로드를 수행하기 위해 프로젝트 상의 가상 업로드 디렉토리(upload) 생성 필요
		// => 외부에서 접근 가능하도록 resources 디렉토리 내에 생성
		// => D:\Shared\Spring\workspace_spring5\Spring_MVC_Board\src\main\webapp\resources\ upload
		String uploadDir = "/resources/upload"; // 가상의 경로(이클립스 프로젝트 상에 생성한 경로)
		// 가상 디렉토리에 대한 실제 경로 알아내기
//		String saveDir = request.getServletContext().getRealPath(uploadDir); // 또는 
		String saveDir = session.getServletContext().getRealPath(uploadDir); // 또는 
//		System.out.println("실제 업로드 경로 : " + saveDir);
		// => D:\Shared\Spring\workspace_spring5\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Spring_MVC_Board\resources\ upload
		
		// 업로드 파일들에 대한 관리의 용이성을 증대시키기 위해
		// 서브(하위) 디렉토리를 활용하여 파일들을 분산 관리 필요
		// => 날짜별로 파일들을 분류하면 관리가 매우 편함
		String subDir = "";
		
		// 날짜별 서브디렉토리 생성
		// => java.util.Date 클래스보다 java.time 패키지의 LocalXXX 클래스 활용이 더 효율적이다!
		// 1. 현재 시스템의 날짜 정보 객체 생성
		// 1-1) java.util.Date 클래스 활용
//		Date now = new Date(); // 기본생성자 활용하여 시스템의 현재 날짜 및 시각 정보 생성
//		System.out.println(now); // Tue Dec 19 12:20:46 KST 2023
		
		// 1-2) java.time.LocalXXX 클래스 활용
		// => 날짜 정보만 관리할 경우 LocalDate, 시각 정보 LocalTime, 날짜 및 시각 정보 LocalDateTime 사용
		LocalDate now = LocalDate.now();
//		System.out.println(now); // 2023-12-19
		// -----------------
		// 2. 날짜 포맷을 "yyyy/MM/dd" 형식으로 변경 
		// => 해당 날짜를 디렉토리 구조로 바로 활용하기 위해 날짜 구분을 슬래시(/) 기호로 지정 
//		// Date 타입 객체의 날짜 포맷을 변경하려면 java.text.SimpleDateFormat 클래스 활용
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//		System.out.println(sdf.format(now)); // 2023/12/19
		
		// LocalXXX 타입 객체의 날짜 포맷을 변경하려면 java.time.format.DateTimeFormatter 클래스 활용
//		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy" + File.separator + "MM" + File.separator + "dd");
		// => 경로로 활용 시 File 클래스의 경로구분자를 가져다 사용하지 않고 슬래시(/) 기호 직접 지정도 가능
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
//		System.out.println(now.format(dtf)); // 2023/12/19
		
		// 3. 지정한 포맷을 적용하여 날짜 형식 변경한 결과를 변수(subDir)에 저장
		// SimpleDateFormat 과 DateTimeFormatter 사용 시 메서드 호출 주체가 다르다!
//		subDir = sdf.format(now);
		subDir = now.format(dtf);
		
		// 4. 기존 업로드 경로(실제 경로)에 서브디렉토리(날짜 경로) 결합
		saveDir += File.separator + subDir; // File.separator 대신 / 또는 \ 지정도 가능
//		System.out.println(saveDir);
		// => D:\Shared\Spring\workspace_spring5\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\Spring_MVC_Board\resources\ upload\2023/12/19

		try {
			// 5. 해당 디렉토리 존재하지 않을 경우 자동 생성
			// 5-1) Paths.get() 메서드 호출하여 업로드 경로에 해당하는 Path 객체 리턴받기
			Path path = Paths.get(saveDir); // 파라미터로 업로드 경로 전달
			
			// 5-2) Files.createDirectories() 메서드 호출하여 실제 경로 생성
			// => 이 때, 중간 경로 중 존재하지 않는 경로들을 모두 생성
			Files.createDirectories(path); // 파라미터로 Path 객체 전달
		} catch (IOException e) {
			e.printStackTrace();
		}
		// -------------------
		// BoardVO 객체에 전달(저장)된 실제 파일 정보가 관리되는 MultipartFile 타입 객체 꺼내기
		MultipartFile mFile1 = board.getFile1();
		MultipartFile mFile2 = board.getFile2();
		MultipartFile mFile3 = board.getFile3();
		// MultipartFile 객체의 getOriginalFilename() 메서드 호출 시 업로드 된 파일명 리턴
		System.out.println("원본파일명1 : " + mFile1.getOriginalFilename());
		System.out.println("원본파일명2 : " + mFile2.getOriginalFilename());
		System.out.println("원본파일명3 : " + mFile3.getOriginalFilename());
		
		// --------------------------
		// [ 파일명 중복방지 대책 ]
		// - 파일명 앞에 난수를 결합하여 다른 사용자의 파일과 중복되지 않도록 구분 가능
		// - 일반적인 숫자로 된 난수보다 문자와 숫자를 활용하는 것이 더 효율적
		// - 난수 생성 라이브러리를 활용하거나 UUID 클래스 활용하여 생성
		//   => UUID : 현재 시스템(서버)에서 랜덤ID 값을 추출하여 제공하는 클래스
		//      (UUID 는 Universally Unique Identifier 의 약자로 범용 고유 식별자라고 함)
		String uuid = UUID.randomUUID().toString();
		System.out.println("uuid : " + uuid); // uuid : ef3e51e8-af4d-4d73-989b-1e1e64271ac7
		
		return "";
	}
	
	
	
	
	
	@GetMapping("BoardList")
	public String list() {
		
		return "board/board_list";
	}
}
