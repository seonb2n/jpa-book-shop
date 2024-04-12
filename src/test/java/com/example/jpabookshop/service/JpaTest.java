package com.example.jpabookshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.jpabookshop.domain.Address;
import com.example.jpabookshop.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JpaTest {

    @Autowired
    EntityManager em1;

    @PersistenceContext
    EntityManager em2;

    @Test
    @Transactional
    void testFirstLevelCacheIsolation() {
        assertNotEquals(em1, em2);

        Member member = createMember(); // Transactional 이기에, 영속화된 엔티티는 1차 캐시에 저장된다.
        Member foundMemberUseAnthoerEm = em2.find(Member.class, 1L);

        assertEquals(member, foundMemberUseAnthoerEm); // 두 엔티티는 동일하다.
    }

    @Test
    @Transactional
    Member testFirstLevelCacheIsolation2() {
        Member member = createMember();
        assertNotNull(member);
        return member;
    }

    @Test
    @Transactional
    Member testFirstLevelCacheIsolation3() {
        Member member = findMember();
        assertNull(member);
        return member;
    }

//    @Test
//    void createMemberTest() throws Exception {
//        ExecutorService executorService = Executors.newFixedThreadPool(2);
//
//        // 첫 번째 쓰레드: createMember 메서드 실행
//        Future<Member> futureMember = executorService.submit(() -> testFirstLevelCacheIsolation2());
//
//        // 두 번째 쓰레드: findMember 메서드 실행
//        Future<Member> futureFoundMember = executorService.submit(() -> testFirstLevelCacheIsolation3());
//
//        // 쓰레드 실행 완료 대기
//        Member member = futureMember.get();
//        Member foundMemberUseAnotherEm = futureFoundMember.get();
//
//        // 생성된 멤버는 null이 아닌지 확인
//        assertNotNull(member);
//
//        // findMember는 null을 반환해야 함
//        assertNull(foundMemberUseAnotherEm);
//
//        executorService.shutdown();
//    }

    Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강남대로", "23"));
        em1.persist(member);
        return member;
    }

    private Member findMember() {
        return em2.find(Member.class, 1L);
    }
}
