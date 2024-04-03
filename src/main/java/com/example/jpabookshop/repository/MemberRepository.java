package com.example.jpabookshop.repository;

import com.example.jpabookshop.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository // repository @ 에 의해서 스프링 빈으로 등록되고, JPA 전용 예외가 발생하면 스프링이 추상화된 예외로 변환해준다.
public class MemberRepository {

    @PersistenceContext // 컨테이너가 제공한 EntityManager 를 사용할 수 있다.
    EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member where m.name = :name", Member.class)
            .setParameter("name", name).getResultList();
    }

}
