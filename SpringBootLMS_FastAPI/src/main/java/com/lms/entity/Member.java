package com.lms.entity;

import com.lms.constant.Role;
import com.lms.dto.MemberFormDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO) /*자동증가*/
    private Long id;

    /* 로그인 아이디 */
    @Column(nullable = false, length = 50, unique = true)
    private String loginId;

    @Column(nullable = false)
    private String password;

    /* 사용자 이름 */
    @Column(nullable = false, name = "name")
    private String name;

    /* '남성', '여성' */
    @Column(nullable = false)
    private String gender;

    /* 생년월일 */
    /*@Column(nullable = false)*/
    private Date birthdate;

    /* 휴대폰 번호 */
    @Column(nullable = false)
    private String mobileNumber;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String email;

    /* 카카오연동키 */
    private String kakaoKey;
    
    private String accessToken; //로그인 토큰

    private String provider;
    private String providerId;

    @Enumerated(EnumType.STRING) //
    private Role role; //권한

    //MemberFormDto -> Member로 변환(Dto로 받은 객체 매핑)
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setLoginId(memberFormDto.getLoginId());
        member.setPassword(passwordEncoder.encode(memberFormDto.getPassword()));
        member.setName(memberFormDto.getName());
        member.setGender(memberFormDto.getGender());
        member.setBirthdate(memberFormDto.getBirthdate());
        member.setMobileNumber(memberFormDto.getMobileNumber());
        member.setAddress(memberFormDto.getAddress());
        member.setEmail(memberFormDto.getEmail());
        member.setRole(Role.USER);

        return member;
    }

    public void updatePassword(PasswordEncoder passwordEncoder, String newPassword) {
        this.password = passwordEncoder.encode(newPassword);
    }
}
