package com.lms.repository;

import com.lms.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /* Id로 회원찾기 */
    Member findByLoginId(String loginId);

    Member findByKakaoKey(String kakaoKey);

    Member findByEmail(String email);
}
