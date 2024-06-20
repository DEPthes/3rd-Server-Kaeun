package project.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import project.project.entity.UserEntity;

@Getter
@Setter
@ToString
@NoArgsConstructor //파라미터가 없는 디폴트 생성자를 생성
public class UserDto {
    private Long id;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.") //null 과 "" 과 " " 모두 허용하지 않는다
    @Size(min=2, max=8, message = "닉네임은 2~8자이어야 합니다.") //길이 제한
    private String nickname;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min=6, max=20, message = "아이디는 6~20자이어야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Size(min=8, max=20, message = "비밀번호는 8~20자이어야 합니다.")
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
