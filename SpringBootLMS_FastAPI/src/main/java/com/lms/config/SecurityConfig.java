package com.lms.config;

import com.lms.oauth2.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig { //시큐리티 설정

    PrincipalOauth2UserService principalOauth2UserService;

    @Autowired
    public SecurityConfig(@Lazy PrincipalOauth2UserService principalOauth2UserService) {
        this.principalOauth2UserService = principalOauth2UserService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // http.csrf().disable(); -> 람다식 변환필요(버전상향으로인해)
        // csrf 공격방지
        http.csrf(config -> config.disable());
        http
                .authorizeHttpRequests(config -> {
                    config
                            .requestMatchers("/css/**", "/js/**", "/img/**", "/slick/**",  "/error").permitAll()
                            .requestMatchers("/", "/id-find", "/pw-find", "/oauth2/**", "/members/**", "/images/**", "/videos/**", "/course/**", "/api/**" ,"/admin/login",  "/subcategories").permitAll() //해당 경로의 요청은 누구나 허용한다.
                            .requestMatchers("/admin/**").hasRole("ADMIN") //해당 경로의 요청은 ADMIN 만 가능
                            .anyRequest().authenticated();
                        });
        http
                .formLogin(config -> {
                    config
                            .loginPage("/members/login") //커스텀 로그인
                            .loginProcessingUrl("/login")
                            .defaultSuccessUrl("/success", true) //로그인 성공시
                            .usernameParameter("loginId") //로그인화면에서 name=username이면 생략가능 / name=eamil이면 필수기입
                            .failureUrl("/members/login/error");
                        })
                .logout(logout ->
                        logout.logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 처리
                                .logoutSuccessUrl("/") //로그아웃 성공시
                );
        http
                .oauth2Login(form -> {
                    form
                            .loginPage("/members/login")
                            .userInfoEndpoint(userInfo -> {
                       userInfo.userService(principalOauth2UserService);
                   });
        });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }
}
