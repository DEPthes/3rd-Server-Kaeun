package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository {
    private static Map<Long, Member> store = new HashMap<>(); //회원을 저장

    private static long sequence = 0L; //0, 1, 2 등 키 값 생성

    @Override
    public Member save(Member member) {
        member.setId(++sequence); //member id는 시스템이 생성, name은 회원가입 시 입력 받으므로 여기서 지정하지 않음
        store.put(member.getId(), member); //member id와 member를 한 쌍으로 Map으로 선언한 store에 저장
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); //Optional.ofNullable을 통해 반환 값이 null이더라도 클라이언트에서 처리가 가능하도록 함
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name)) //파라미터로 넘어온 name과 member의 name이 같은지 확인
                .findAny(); //findAny는 가장 먼저 탐색되는 요소를 리턴하여 하나라도 반환함. 없을 경우 null이 Optional로 감싸져 반환
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); //store member들을 전부 반환
    }

    public void clearStore() {
        store.clear();
    }
}
