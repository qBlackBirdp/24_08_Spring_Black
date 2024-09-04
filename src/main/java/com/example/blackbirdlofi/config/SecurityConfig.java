package com.example.blackbirdlofi.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                // 로그인 폼 설정
                .formLogin(Customizer.withDefaults())
                // 권한 설정
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // /usr/member/login 및 /usr/member/join는 모두 접근 가능하게 설정
                                .requestMatchers("/usr/member/login", "/usr/member/join").permitAll()
                                // /home 및 루트 페이지는 인증 필요 없음
                                .requestMatchers("/", "/usr/home/").permitAll()
                                // 나머지 요청은 인증 필요
                                .anyRequest().authenticated()
                )
                // H2 콘솔을 위한 헤더 설정 (프레임을 허용)
                .headers(headersConfigurer ->
                        headersConfigurer.frameOptions(frameOptionsConfig -> frameOptionsConfig.sameOrigin())
                )
                // 로그아웃 설정
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout")
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
