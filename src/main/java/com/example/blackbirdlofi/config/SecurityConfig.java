package com.example.blackbirdlofi.config;

import com.example.blackbirdlofi.security.CustomOAuth2UserService;
import com.example.blackbirdlofi.security.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = true)  // 개발 단계이므로 디버깅 모드 활성화
public class SecurityConfig {

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;  // @Lazy 제거

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 비밀번호 암호화를 위한 Encoder
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                .cors(withDefaults())  // CORS 설정 추가
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화

                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()  // FORWARD 요청은 인증 없이 허용
                                .requestMatchers("/usr/member/login", "/usr/member/doGLogin", "/firebaseUser", "/usr/member/doLogout").permitAll()  // 소셜 로그인 관련 경로 허용
                                .requestMatchers("/", "/usr/home/**").permitAll()  // 홈 페이지 및 특정 리소스 접근 허용
                                .requestMatchers("/js/**", "/css/**", "/img/**", "/fontawesome-free-6.5.1-web/**").permitAll()  // 정적 리소스 허용
                                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )

                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/usr/member/login")
                        .successHandler(oAuth2AuthenticationSuccessHandler)  // 로그인 성공 후 핸들러
                        .defaultSuccessUrl("/usr/home/main", true)
                        .failureUrl("/usr/member/login?error=true")
                        .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(customOAuth2UserService))
                )

                // 세션 관리 설정: OAuth2 로그인 시 세션을 사용하므로 세션 정책을 기본으로 두거나 필요 시 생성
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // 필요 시 세션 생성
                )

                // 로그아웃 설정
                .logout(logout ->
                        logout
                                .logoutUrl("/usr/member/doLogout")  // 로그아웃 URL 설정
                                .logoutSuccessUrl("/usr/member/login?logout")  // 로그아웃 성공 시 이동할 경로
                                .invalidateHttpSession(true)  // 로그아웃 시 세션 무효화
                                .deleteCookies("JSESSIONID")  // 쿠키 삭제
                );

        return http.build();  // SecurityFilterChain 생성
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()  // 정적 리소스에 대한 보안 검사를 무시
                );
    }

    // AuthenticationManager 빈 설정
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}