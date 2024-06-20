package project.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import project.project.dto.UserDto;
import project.project.entity.UserEntity;
import project.project.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //사용자 정보 저장
    @Transactional
    public Long saveUser(UserDto userDto) {
        //UserDto를 UserEntity로 변환하여 저장하고 그 ID를 반환
        UserEntity user = userDto.toEntity();
        return userRepository.save(user).getId();
    }

    //특정 사용자의 정보 조회
    @Transactional
    public UserDto getUserById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID")); //사용자 ID로 사용자 정보 조회
        return UserDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .password(user.getPassword())
                .build(); //UserEntity를 UserDto로 변환하여 반환
    }

    //사용자 로그인 처리
    public Long login(String username, String password) {
        UserEntity user = userRepository.findByUsernameAndPassword(username, password); //사용자 이름과 비밀번호로 사용자 정보 조회
        //사용자가 존재하면 사용자 ID 반환, 존재하지 않으면 null 반환
        if (user != null) {
            return user.getId(); // 로그인 성공한 경우 사용자 ID 반환
        } else {
            return null; // 로그인 실패한 경우 null 반환
        }
    }
}
