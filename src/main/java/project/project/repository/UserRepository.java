package project.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.project.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsernameAndPassword(String username, String password); //사용자 이름과 비밀번호로 사용자를 검색
}
