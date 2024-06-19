package project.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import project.project.entity.UserEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank
    @Size(min=2, message = "닉네임은 최소 2글자 이상이어야 합니다.")
    private String nickname;

    @NotBlank
    @Size(min=6, message = "아이디는 최소 6글자 이상이어야 합니다.")
    private String username;

    @NotBlank
    @Size(min=8, message = "비밀번호는 최소 8글자 이상이어야 합니다.")
    private String password;

    //UserDto를 UserEntity로 변환하는 메소드
    public UserEntity toEntity() {
        return UserEntity.builder()
                .id(id)
                .nickname(nickname)
                .username(username)
                .password(password)
                .build();
    }

    @Builder
    public UserDto(Long id, String nickname, String username, String password) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }
}
