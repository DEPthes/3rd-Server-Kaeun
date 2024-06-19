package project.project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.project.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    //Page<BoardEntity> findByTitleContaining(String keyword, Pageable pageable); //제목에 특정 키워드가 포함된 게시글을 페이지네이션하여 검색
    //위의 쿼리 메소드를 JPQL로 변환
    @Query("SELECT b FROM BoardEntity b WHERE b.title LIKE %:keyword%")
    Page<BoardEntity> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);
    Long countByTitleContaining(String keyword); //제목에 특정 키워드가 포함된 게시글의 총 개수를 계산
    Page<BoardEntity> findByUserId(Long userId, Pageable pageable); //특정 사용자의 게시글을 페이지네이션하여 검색
    Long countByUserId(Long userId); //특정 사용자의 게시글의 총 개수를 계산
    Long countByUserIdAndTitleContaining(Long userId, String keyword); //특정 사용자의 게시글 중에서 제목에 특정 키워드가 포함된 게시글의 총 개수를 계산
    Page<BoardEntity> findByUserIdAndTitleContaining(Long userId, String keyword, Pageable pageable); //특정 사용자의 게시글 중에서 제목에 특정 키워드가 포함된 게시글을 페이지네이션하여 검색
}
