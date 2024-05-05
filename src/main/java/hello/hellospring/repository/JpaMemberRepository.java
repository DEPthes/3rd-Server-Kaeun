package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {
    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    //pk값인 id와 관련된 회원 등록과 id에 따른 회원 탐색 기능의 경우, EntityManager에서 제공하는 메서드를 사용해서 구현할 수 있음
    @Override
    public Member save(Member member) {
        em.persist(member); //persist는 영구 저장
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    //pk 기반이 아닌 findByName, findAll은 JPQL이라는 객체지향 쿼리문으로 작성, 엔티티를 대상으로 쿼리를 날림
    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }
}
