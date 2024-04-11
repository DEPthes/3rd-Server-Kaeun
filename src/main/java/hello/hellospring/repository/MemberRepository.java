package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member); //회원이 저장소에 저장

    Optional<Member> findById(Long id); //id로 회원 찾기

    Optional<Member> findByName(String name); //name으로 회원 찾기

    List<Member> findAll(); //지금까지 저장된 모든 회원 list 반환
}
