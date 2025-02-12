package com.lms.oauth2;

import com.lms.constant.Role;
import com.lms.entity.Member;
import com.lms.oauth2.userInfo.GoogleUserInfo;
import com.lms.oauth2.userInfo.KakaoUserInfo;
import com.lms.oauth2.userInfo.NaverUserInfo;
import com.lms.oauth2.userInfo.OAuth2UserInfo;
import com.lms.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;

    // 구글로부터 받은 userRequest 데이터에 대한 후처리되는 함수
    // 함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            oAuth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }
        else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("우리는 구글과 네이버와 카카오만 지원합니다.");
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId; // google_10021320120
        String password = passwordEncoder.encode("1234");
        String email = oAuth2UserInfo.getEmail();
        Role role = Role.USER;
        String fullName = oAuth2UserInfo.getFullName();

        Member memberEntity = memberRepository.findByLoginId(username);

        //회원가입  진행
        if(memberEntity == null) {
            System.out.println("소셜 로그인이 최초입니다.");
            Member member = new Member();
            member.setLoginId(username);
            member.setPassword(password);
            member.setEmail(email);
            member.setName(fullName);
            member.setAddress("임시주소");
            member.setGender("남성");
            member.setBirthdate(new Date());
            member.setMobileNumber("010-1234-1234");
            member.setRole(role);
            member.setProvider(provider);
            member.setProviderId(providerId);
            memberRepository.save(member);
            return new PrincipalDetails(member, oAuth2User.getAttributes());
        } else {
            System.out.println("소셜로그인을 이미 한적이 있습니다. 당신은 자동회원가입이 되어 있습니다.");
            return new PrincipalDetails(memberEntity, oAuth2User.getAttributes());
        }

        }

}
