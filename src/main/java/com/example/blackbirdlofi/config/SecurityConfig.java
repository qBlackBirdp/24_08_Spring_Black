package com.example.blackbirdlofi.config;

import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 포워딩 허용
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // 포워딩에 대해 인증을 허용
                                // 로그인 페이지와 회원가입 페이지에 대한 접근을 허용
                                .requestMatchers("/usr/member/login", "/usr/member/join", "/usr/member/doLogin", "/usr/member/doJoin").permitAll()
                                // 루트 페이지 및 /usr/home은 인증 필요 없음
                                .requestMatchers("/", "/usr/home/**").permitAll()
                                // 정적 리소스에 대한 접근 허용
                                .requestMatchers("/js/**", "/css/**", "/img/**", "/fontawesome-free-6.5.1-web/**").permitAll()
                                // 나머지 요청은 인증 필요
                                .anyRequest().authenticated()
                )

                // 로컬 로그인 폼 설정
                .formLogin(form -> form
                        .loginPage("/usr/member/login")
                        .loginProcessingUrl("/usr/member/doLogin")
                        .usernameParameter("email")  // 사용자명을 email로 설정
                        .passwordParameter("loginPw") // 비밀번호를 loginPw로 설정
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/usr/member/login?error=true")
                        .permitAll()
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/usr/member/login") // 같은 로그인 페이지 사용
                        .defaultSuccessUrl("/", true)  // 로그인 성공 후 리디렉션될 기본 URL
                        .failureUrl("/usr/member/login?error=true")  // 로그인 실패 시 이동할 URL
                )

                // H2 콘솔을 위한 헤더 설정 (프레임을 허용)
                .headers(headersConfigurer ->
                        headersConfigurer.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin())
                )

                // 로그아웃 설정
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/usr/member/login?logout")
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID")
                );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }
}
