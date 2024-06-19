package project.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import project.project.entity.BoardEntity;
import project.project.entity.UserEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private Long userId;
    private String nickname;

    @NotBlank(message = "제목을 입력하세요.")
    private String title;

    @NotBlank(message = "내용을 입력하세요.")
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer postNumber;

    //BoardDto를 BoardEntity로 변환하는 메소드
    public BoardEntity toEntity(UserEntity user) {
        return BoardEntity.builder()
                .id(id)
                .user(user)
                .title(title)
                .content(content)
                .build();
    }

    @Builder
    public BoardDto(Long id, Long userId, String nickname, String title, String content, String writer, LocalDateTime createdAt, LocalDateTime modifiedAt, Integer postNumber) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.postNumber = postNumber;
    }
}
