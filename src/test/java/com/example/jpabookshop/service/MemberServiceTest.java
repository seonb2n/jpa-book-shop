package com.example.jpabookshop.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.jpabookshop.domain.Member;
import com.example.jpabookshop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 테스트가 끝나면 트랜잭션을 롤백하기 위함
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        Member member = new Member();
        member.setName("kim");

        Long saveId = memberService.join(member);

        Member foundMember = memberRepository.findById(saveId).get();

        assertEquals(member, foundMember);
    }

    @Test
    public void propertyAcess테스트() throws Exception {
        Member member = new Member();
        member.setFirstName("kim");
        member.setLastName("sb");

        Long saveId = memberService.join(member);

        Member foundMember = memberRepository.findById(saveId).get();

        assertEquals(member, foundMember);
        assertEquals("kimsb", foundMember.getFullName());
    }
}