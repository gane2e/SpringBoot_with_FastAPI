package com.lms.oauth2;

import com.lms.entity.Member;
import com.lms.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class PrincipalDetailsService  implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

            Member member = memberRepository.findByLoginId(loginId);

            //email 해당하는 member 없으면
            if(member == null) {
                throw new UsernameNotFoundException(loginId);
            }
            return new PrincipalDetails(member);
        }
}
