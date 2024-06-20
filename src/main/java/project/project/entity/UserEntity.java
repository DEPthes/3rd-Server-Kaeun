package project.project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length=8, nullable = false, unique = true)
    @Size(min=2, max=8, message = "닉네임은 2~8자이어야 합니다.")
    private String nickname;

    @Column(length=20, nullable = false, unique = true)
    @Size(min=6, max=20, message = "아이디는 6~20자이어야 합니다.")
    private String username;

    @Column(length=20, nullable = false)
    @Size(min=8, max=20, message = "비밀번호는 8~20자이어야 합니다.")
    private String password;

    @Builder
    public UserEntity(Long id, String nickname, String username, String password) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
    }
}
