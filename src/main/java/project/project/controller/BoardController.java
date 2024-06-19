package project.project.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.project.dto.BoardDto;
import project.project.dto.UserDto;
import project.project.service.BoardService;
import project.project.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Controller
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;
    private final HttpSession httpSession;

    public BoardController(BoardService boardService, UserService userService, HttpSession httpSession) {
        this.boardService = boardService;
        this.userService = userService;
        this.httpSession = httpSession;
    }

    //공통적으로 필요한 속성을 모델에 추가
    private void addCommonAttributes(Model model, Long userId) {
        UserDto userDto = userService.getUserById(userId);
        model.addAttribute("userId", userId);
        model.addAttribute("nickname", userDto.getNickname());
        model.addAttribute("today", LocalDate.now());
    }

    //게시글 리스트를 보여주는 메소드
    @GetMapping("/")
    public String list(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        //로그인 여부를 확인하고 로그인되지 않았다면 로그인 페이지로 리다이렉트
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        addCommonAttributes(model, userId);
        List<BoardDto> boardDtoList = boardService.getBoardList(pageNum);
        model.addAttribute("boardList", boardDtoList);
        model.addAttribute("hasPreviousPage", boardService.hasPreviousPage(pageNum));
        model.addAttribute("hasNextPage", boardService.hasNextPage(userId, pageNum, null, false));
        model.addAttribute("currentPage", pageNum);

        return "board/list.html";
    }

    //글 작성 페이지를 보여주는 메소드
    @GetMapping("/post")
    public String write(Model model) {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        addCommonAttributes(model, userId);

        return "board/write.html";
    }

    //글 작성 폼을 제출받아 저장하는 메소드
    @PostMapping("/post")
    public String write(@Valid BoardDto boardDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/write.html";
        } else {
            Long userId = (Long) httpSession.getAttribute("userId");
            if (userId == null) {
                return "redirect:/login";
            }

            boardDto.setUserId(userId);
            boardService.savePost(boardDto);

            return "redirect:/";
        }
    }

    //특정 게시글의 상세 내용을 보여주는 메소드
    @GetMapping("/post/{postId}")
    public String detail(@PathVariable("postId") Long id, Model model) {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        addCommonAttributes(model, userId);
        model.addAttribute("boardDto", boardService.getPost(id));

        return "board/detail.html";
    }

    //특정 게시글의 내용을 수정 부분을 보여주는 메소드
    @GetMapping("/post/edit/{postId}")
    public String edit(@PathVariable("postId") Long id, Model model) {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        addCommonAttributes(model, userId);
        model.addAttribute("boardDto", boardService.getPost(id));

        return "board/update.html";
    }

    //특정 게시글의 내용을 수정하는 메소드
    @PutMapping("/post/edit/{postId}")
    public String update(BoardDto boardDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "board/update.html";
        } else {
            Long userId = (Long) httpSession.getAttribute("userId");
            if (userId == null) {
                return "redirect:/login";
            }

            boardDto.setUserId(userId);
            boardService.savePost(boardDto);

            return "redirect:/";
        }
    }

    //게시글 제목을 검색하는 메소드
    @GetMapping("/board/search")
    public String search(@RequestParam(value = "keyword") String keyword, @RequestParam(value = "page", defaultValue = "1") Integer pageNum, Model model) {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        addCommonAttributes(model, userId);
        List<BoardDto> boardDtoList = boardService.searchPosts(keyword, pageNum);
        model.addAttribute("boardList", boardDtoList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("hasPreviousPage", boardService.hasPreviousPage(pageNum));
        model.addAttribute("hasNextPage", boardService.hasNextPage(userId, pageNum, keyword, false));
        model.addAttribute("currentPage", pageNum);

        return "board/list.html";
    }

    //내가 쓴 게시글 목록을 보여주는 메소드
    @GetMapping("/mylist")
    public String showMyList(Model model, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        addCommonAttributes(model, userId);
        List<BoardDto> myBoardDtoList = boardService.getMyPost(userId, pageNum);
        model.addAttribute("boardList", myBoardDtoList);
        model.addAttribute("hasPreviousPage", boardService.hasPreviousPage(pageNum));
        model.addAttribute("hasNextPage", boardService.hasNextPage(userId, pageNum, null, true));
        model.addAttribute("currentPage", pageNum);

        return "board/mylist.html";
    }

    //내가 쓴 게시글 목록에서 게시글 제목을 검색하는 메소드
    @GetMapping("/mylist/search")
    public String searchMyPost(Model model, @RequestParam(value = "keyword") String keyword, @RequestParam(value = "page", defaultValue = "1") Integer pageNum) {
        Long userId = (Long) httpSession.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        addCommonAttributes(model, userId);
        List<BoardDto> myBoardDtoList = boardService.searchMyPosts(userId, keyword, pageNum);
        model.addAttribute("boardList", myBoardDtoList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("hasPreviousPage", boardService.hasPreviousPage(pageNum));
        model.addAttribute("hasNextPage", boardService.hasNextPage(userId, pageNum, keyword, true));
        model.addAttribute("currentPage", pageNum);

        return "board/mylist.html";
    }
}
