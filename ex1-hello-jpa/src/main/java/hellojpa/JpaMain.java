package hellojpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        //persistence 클래스가 persistence.xml을 읽고, EntityManagerFactory 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        //EntityManagerFactory가 EntityManager를 생성
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction(); //트랜잭션 추가
        tx.begin();

       try {
           Member member = new Member();
           member.setId(2L);
           member.setName("Hello");

           em.persist(member);

           tx.commit();
       } catch(Exception e) {
           tx.rollback();
       } finally {
           em.close();
       }
        emf.close();
    }
}
