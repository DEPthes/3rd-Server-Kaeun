package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class MemoryMemberRepositoryTest {
    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test //import org.junit.jupiter.api.Test
    public void save() {
        Member member = new Member(); //새로운 member 생성
        member.setName("spring"); //만든 member의 name을 spring으로 설정

        repository.save(member); //리포지토리에 member 저장

        //만든 member가 제대로 저장되었는지 검증
        Member result = repository.findById(member.getId()).get(); //메모리에 저장된 값을 result로 저장
        assertThat(member).isEqualTo(result); //생성한 member와 result가 같은지 확인 import static org.assertj.core.api.Assertions.*
    }

    @Test
    public void findByName() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        Member result = repository.findByName("spring1").get(); //spring1 이름을 가진 member를 result에 저장
        //Member result = repository.findByName("spring2").get(); //이렇게 작성하면 테스트 실패
        assertThat(result).isEqualTo(member1); //검증
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("spring1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("spring2");
        repository.save(member2);

        List<Member> result = repository.findAll();
        assertThat(result.size()).isEqualTo(2); //저장된 member의 인원수로 전체 멤버를 제대로 반환했는지 검증
    }
}
