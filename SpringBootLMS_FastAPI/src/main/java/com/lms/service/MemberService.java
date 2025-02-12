package com.lms.service;

import com.lms.dto.MemberFormDto;
import com.lms.entity.Member;
import com.lms.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MemberService{
    
    private final MemberRepository memberRepository;

    /* 회원가입 service */
    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    } 
    
    /* 아이디 중복체크 */
    public void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByLoginId(member.getLoginId());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }
    /* 아이디 중복체크 */
    public boolean checkloginId(String loginId) {
        Member findMember = memberRepository.findByLoginId(loginId);
        if (findMember != null) {
            return false;
        } else
            return true;
    }

    public MemberFormDto modifyMember(String loginId) {
        Member member = memberRepository.findByLoginId(loginId);
        MemberFormDto memberFormDto = MemberFormDto.of(member);
        return memberFormDto;
    }

    /* 이메일인증 체크 */
    public boolean emailCheck(String email) {

        Member findMember = memberRepository.findByEmail(email);
        if (findMember != null)
            return true;
        else
            return false;
    }
    
    /* 아이디인증 체크 */
    public boolean loginIdCheck(String loginId) {

        Member findMember = memberRepository.findByLoginId(loginId);
        if (findMember != null)
            return true;
        else
            return false;
    }

    /* 회원 id로 기본키찾기 */
    public Long userId(String loginId) {
        Member findMember = memberRepository.findByLoginId(loginId);
        return findMember.getId();
    }

    /* 회원 id로 성명 찾기 */
    public String userName(String loginId) {
        Member findMember = memberRepository.findByLoginId(loginId);
        return findMember.getName();
    }

    public Member create(String loginId, String password, String email) {
        Member member = new Member();
        member.setLoginId(loginId);
        member.setPassword(password);
        member.setEmail(email);
        return null;
    }

    
}
