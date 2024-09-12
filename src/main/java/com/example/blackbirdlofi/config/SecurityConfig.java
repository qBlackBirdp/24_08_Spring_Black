package com.example.blackbirdlofi.config;

import com.example.blackbirdlofi.filter.CustomLoginFilter;
import com.example.blackbirdlofi.filter.CustomLogoutFilter;
import com.example.blackbirdlofi.security.CustomUserDetailsService;
import com.example.blackbirdlofi.filter.SocialLoginFilter;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextHolderFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity(debug = true) // Spring Security를 활성화하고 디버그 모드를 설정
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private SocialLoginFilter socialLoginFilter;

    @Autowired
    private CustomLoginFilter customLoginFilter;

    @Autowired
    private CustomLogoutFilter customLogoutFilter;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 비밀번호 암호화를 위한 Encoder
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);  // UserDetailsService 등록
        provider.setPasswordEncoder(passwordEncoder());  // 암호화 방식 등록
        return provider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())  // CORS 설정 추가
                // CSRF 비활성화: CSRF 공격 방어를 비활성화
                .csrf(AbstractHttpConfigurer::disable)

                // 포워딩 요청 허용
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll() // FORWARD 요청은 인증 없이 허용
                                // 로그인/회원가입 및 로그인 처리 관련 페이지를 인증 없이 접근 허용
                                .requestMatchers("/usr/member/login", "/usr/member/join", "/usr/member/doLocalLogin", "/usr/member/doGLogin", "/usr/member/doJoin", "/firebaseUser").permitAll()
                                // 홈 페이지 및 특정 리소스 접근 허용
                                .requestMatchers("/", "/usr/home/**").permitAll()
                                .requestMatchers("/js/**", "/css/**", "/img/**", "/fontawesome-free-6.5.1-web/**").permitAll()
                                // 나머지 요청은 인증 필요
                                .anyRequest().authenticated()
                )

                // 익명 사용자 처리 비활성화
                .anonymous(AbstractHttpConfigurer::disable)

                // SecurityContextPersistenceFilter 추가
                .addFilterBefore(new SecurityContextPersistenceFilter(), SecurityContextHolderFilter.class)  // SecurityContext를 세션과 동기화

                // 커스텀 필터 적용 (필터 중복 제거)
                .addFilterBefore(socialLoginFilter, UsernamePasswordAuthenticationFilter.class)  // 소셜 로그인 필터 추가
                .addFilterBefore(customLoginFilter, UsernamePasswordAuthenticationFilter.class)  // 커스텀 로그인 필터 추가
                .addFilterBefore(customLogoutFilter, UsernamePasswordAuthenticationFilter.class)  // 커스텀 로그아웃 필터 추가

                // 로컬 로그인 직접 처리
                .formLogin(AbstractHttpConfigurer::disable)

                // OAuth2 로그인 설정 (구글 로그인)
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/usr/member/login") // OAuth2 로그인 페이지 경로
                        .defaultSuccessUrl("/usr/home/main", true)  // OAuth2 로그인 성공 시 이동할 경로
                        .failureUrl("/usr/member/login?error=true")  // OAuth2 로그인 실패 시 이동할 경로
                )

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