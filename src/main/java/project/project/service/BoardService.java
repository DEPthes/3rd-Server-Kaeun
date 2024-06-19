package project.project.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.project.dto.BoardDto;
import project.project.entity.BoardEntity;
import project.project.entity.UserEntity;
import project.project.repository.BoardRepository;
import project.project.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private BoardRepository boardRepository;
    private UserRepository userRepository;

    private static final int PAGE_POST_COUNT = 11; //한 페이지에 존재하는 게시글 수

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    //게시글 저장 기능
    @Transactional
    public Long savePost(BoardDto boardDto) {
        UserEntity user = userRepository.findById(boardDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID")); //게시글 작성자 정보 조회
        boardDto.setNickname(user.getNickname()); //작성자 닉네임을 boardDto에 설정
        return boardRepository.save(boardDto.toEntity(user)).getId(); //게시글을 저장하고 그 ID 반환
    }

    //페이지네이션된 게시글 목록 반환
    @Transactional
    public List<BoardDto> getBoardList(Integer pageNum) {
        //페이지네이션 처리된 게시글 목록 조회
        Page<BoardEntity> page = boardRepository
                .findAll(PageRequest
                        .of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<BoardEntity> boardEntityList = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        int startNum = (pageNum - 1) * PAGE_POST_COUNT + 1; //게시글 번호의 시작 번호 계산

        //Entity를 DTO로 변환하여 리스트에 추가
        for (int i = 0; i < boardEntityList.size(); i++) {
            BoardEntity boardEntity = boardEntityList.get(i);
            BoardDto boardDto = this.convertEntityToDto(boardEntity);
            boardDto.setPostNumber(startNum + i);
            boardDtoList.add(boardDto);
        }

        return boardDtoList;
    }

    //이전 페이지가 존재하는지 여부 반환
    public boolean hasPreviousPage(Integer curPageNum) {
        return curPageNum > 1;
    }

    //다음 페이지가 존재하는지 여부 반환
    public boolean hasNextPage(Long userId, Integer curPageNum, String keyword, boolean isMyList) {
        //전체 게시글 수 계산
        Double postsTotalCount;
        if(isMyList) {
            postsTotalCount = (keyword != null && !keyword.isEmpty())
                    ? Double.valueOf(this.getUserSearchBoardCount(userId, keyword))
                    : Double.valueOf(this.getUserBoardCount(userId));
        } else {
            postsTotalCount = (keyword != null && !keyword.isEmpty())
                    ? Double.valueOf(this.getSearchBoardCount(keyword))
                    : Double.valueOf(this.getBoardCount());
        }
        Integer totalLastPageNum = (int) (Math.ceil(postsTotalCount / PAGE_POST_COUNT)); //전체 페이지 수 계산
        return curPageNum < totalLastPageNum; //다음 페이지 존재 여부 반환
    }

    //전체 게시글 수 반환
    @Transactional
    public Long getBoardCount() {
        return boardRepository.count();
    }

    //특정 키워드를 포함하는 게시글 수 반환
    @Transactional
    public Long getSearchBoardCount(String keyword) {
        return boardRepository.countByTitleContaining(keyword);
    }

    //특정 사용자의 게시글 수 반환
    @Transactional
    public Long getUserBoardCount(Long userId) {
        return boardRepository.countByUserId(userId);
    }

    //특정 사용자의 특정 키워드를 포함하는 게시글 수 반환
    @Transactional
    public Long getUserSearchBoardCount(Long userId, String keyword) {
        return boardRepository.countByUserIdAndTitleContaining(userId, keyword);
    }

    //특정 게시글을 조회하고 DTO로 변환하여 반환
    @Transactional
    public BoardDto getPost(Long id) {
        //게시글 조회
        Optional<BoardEntity> boardWrapper = boardRepository.findById(id);
        BoardEntity boardEntity = boardWrapper.get();

        //Entity를 DTO로 변환
        BoardDto boardDto = BoardDto.builder()
                .id(boardEntity.getId())
                .userId(boardEntity.getUser().getId())
                .nickname(boardEntity.getUser().getNickname())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .createdAt(boardEntity.getCreatedAt())
                .build();

        return boardDto;
    }

    //특정 키워드를 포함하는 게시글을 페이지네이션하여 반환
    @Transactional
    public List<BoardDto> searchPosts(String keyword, Integer pageNum) {
        //특정 키워드를 포함하는 게시글 페이지네이션 조회
        PageRequest pageRequest = PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BoardEntity> page = boardRepository.findByTitleContaining(keyword, pageRequest);
        List<BoardEntity> boardEntityList = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        //조회된 게시글을 DTO로 변환하여 리스트에 추가
        if(boardEntityList.isEmpty())
            return boardDtoList;

        int startNum = (pageNum - 1) * PAGE_POST_COUNT + 1; //게시글 번호의 시작 번호 계산

        //Entity를 DTO로 변환하여 리스트에 추가
        for (int i = 0; i < boardEntityList.size(); i++) {
            BoardEntity boardEntity = boardEntityList.get(i);
            BoardDto boardDto = this.convertEntityToDto(boardEntity);
            boardDto.setPostNumber(startNum + i);
            boardDtoList.add(boardDto);
        }

        return boardDtoList;
    }

    //특정 사용자의 게시글 목록을 페이지네이션하여 반환
    @Transactional
    public List<BoardDto> getMyPost(Long userId, Integer pageNum) {
        //특정 사용자의 게시글 목록 페이지네이션 조회
        Page<BoardEntity> page = boardRepository.findByUserId(userId, PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdAt")));
        List<BoardEntity> boardEntityList = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        int startNum = (pageNum - 1) * PAGE_POST_COUNT + 1; //게시글 번호의 시작 번호 계산

        //Entity를 DTO로 변환하여 리스트에 추가
        for (int i = 0; i < boardEntityList.size(); i++) {
            BoardEntity boardEntity = boardEntityList.get(i);
            BoardDto boardDto = this.convertEntityToDto(boardEntity);
            boardDto.setPostNumber(startNum + i);
            boardDtoList.add(boardDto);
        }

        return boardDtoList;
    }

    //특정 사용자의 특정 키워드를 포함하는 게시글 목록을 페이지네이션하여 반환
    @Transactional
    public List<BoardDto> searchMyPosts(Long userId, String keyword, Integer pageNum) {
        //특정 사용자의 특정 키워드를 포함하는 게시글 목록 페이지네이션 조회
        PageRequest pageRequest = PageRequest.of(pageNum - 1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<BoardEntity> page = boardRepository.findByUserIdAndTitleContaining(userId, keyword, pageRequest);
        List<BoardEntity> boardEntityList = page.getContent();
        List<BoardDto> boardDtoList = new ArrayList<>();

        //조회된 게시글을 DTO로 변환하여 리스트에 추가
        if(boardEntityList.isEmpty())
            return boardDtoList;

        int startNum = (pageNum - 1) * PAGE_POST_COUNT + 1; //게시글 번호의 시작 번호 계산

        //Entity를 DTO로 변환하여 리스트에 추가
        for (int i = 0; i < boardEntityList.size(); i++) {
            BoardEntity boardEntity = boardEntityList.get(i);
            BoardDto boardDto = this.convertEntityToDto(boardEntity);
            boardDto.setPostNumber(startNum + i);
            boardDtoList.add(boardDto);
        }

        return boardDtoList;
    }

    //BoardEntity를 BoardDto로 변환하는 메소드
    private BoardDto convertEntityToDto(BoardEntity boardEntity) {
        return BoardDto.builder()
                .id(boardEntity.getId())
                .userId(boardEntity.getUser().getId())
                .nickname(boardEntity.getUser().getNickname())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .createdAt(boardEntity.getCreatedAt())
                .build();
    }
}
