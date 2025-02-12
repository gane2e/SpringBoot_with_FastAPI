package com.lms.service;

import com.lms.dto.MemberFormDto;
import com.lms.entity.Member;
import groovy.util.logging.Log4j2;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member testCreateMemberDAta() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setLoginId("admin001");
        memberFormDto.setPassword("1234");
        memberFormDto.setName("관리자");
        memberFormDto.setGender("남성");
        memberFormDto.setBirthdate(new Date());
        memberFormDto.setMobileNumber("010-1234-1234");
        memberFormDto.setAddress("서울시 어딘가");
        memberFormDto.setEmail("email@email.com");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    @Commit
    public void saveMemberTest() {

        try {
            Member member = testCreateMemberDAta();
            Member savedMember = memberService.saveMember(member);

            assertEquals(member.getLoginId(), savedMember.getLoginId());
            assertEquals(member.getName(), savedMember.getName());
            assertEquals(member.getGender(), savedMember.getGender());
            assertEquals(member.getBirthdate(), savedMember.getBirthdate());
            assertEquals(member.getMobileNumber(), savedMember.getMobileNumber());
            assertEquals(member.getAddress(), savedMember.getAddress());
            assertEquals(member.getEmail(), savedMember.getEmail());
            assertEquals(member.getRole(), savedMember.getRole());
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}