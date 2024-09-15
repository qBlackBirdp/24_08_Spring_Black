package com.example.blackbirdlofi.config;

import com.example.blackbirdlofi.filter.JwtAuthenticationFilter;
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
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = true) // 개발 단계이므로 디버깅 모드 활성화
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화를 위한 Encoder
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())  // CORS 설정 추가
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 비활성화

                // 포워딩 요청 허용
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // FORWARD 요청은 인증 없이 허용
                                .requestMatchers("/usr/member/login", "/usr/member/doGLogin", "/firebaseUser").permitAll()  // 소셜 로그인 관련 경로 허용
                                .requestMatchers("/", "/usr/home/**").permitAll()  // 홈 페이지 및 특정 리소스 접근 허용
                                .requestMatchers("/js/**", "/css/**", "/img/**", "/fontawesome-free-6.5.1-web/**").permitAll()  // 정적 리소스 허용
                                .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )

                // 익명 사용자 처리 비활성화
                .anonymous(AbstractHttpConfigurer::disable)

                // 세션 관리 설정
                // JWT는 세션을 사용하지 않고 stateless로 설정
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // JWT에서는 세션 사용 안 함
                )

                // OAuth2 로그인 설정 - OAuth2는 세션 유지
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/usr/member/login")  // 로그인 페이지 설정
                        .successHandler(oAuth2AuthenticationSuccessHandler)  // 로그인 성공 후 핸들러
                        .defaultSuccessUrl("/usr/home/main", true)  // 성공 후 리다이렉트 경로
                        .failureUrl("/usr/member/login?error=true")  // 실패 시 경로
                )

                // JWT 필터를 추가하여 토큰 인증만 처리
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

                // H2 콘솔의 프레임 옵션 설정
                .headers(headersConfigurer ->
                        headersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)  // 동일 출처의 프레임 허용 (H2 콘솔 접근)
                )

                // 로그아웃 설정
                .logout(logout ->
                        logout
                                .logoutUrl("/logout")  // 로그아웃 URL 설정
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
