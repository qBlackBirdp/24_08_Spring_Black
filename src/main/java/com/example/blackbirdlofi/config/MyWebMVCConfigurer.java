package com.example.blackbirdlofi.config;


import com.example.blackbirdlofi.interceptor.BeforeActionInterceptor;
import com.example.blackbirdlofi.interceptor.NeedLoginInterceptor;
import com.example.blackbirdlofi.interceptor.NeedLogoutInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class MyWebMVCConfigurer implements WebMvcConfigurer {

	// BeforeActionInterceptor 불러오기(연결)
	@Autowired
	BeforeActionInterceptor beforeActionInterceptor;
	// NeedLoginInterceptor 불러오기(연결)
	@Autowired
	NeedLoginInterceptor needLoginInterceptor;
	// NeedLogoutInterceptor 불러오기(연결)
	@Autowired
	NeedLogoutInterceptor needLogoutInterceptor;

	// CORS 설정 추가
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**") // 모든 경로에 대해 CORS 허용
				.allowedOrigins("http://localhost:8081") // 허용할 도메인
				.allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
				.allowedHeaders("*") // 허용할 헤더
				.allowCredentials(true); // 자격증명(쿠키, 인증 정보) 허용
	}

	// 인터셉터 등록(적용)
	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**")
//				.excludePathPatterns("/error");
//
//		registry.addInterceptor(needLoginInterceptor).addPathPatterns("/usr/article/write")
//				.addPathPatterns("/usr/article/doWrite").addPathPatterns("/usr/article/modify")
//				.addPathPatterns("/usr/article/doModify").addPathPatterns("/usr/article/doDelete")
//				.addPathPatterns("/usr/member/doLogout");
////				.addPathPatterns("/usr/article/doReaction");
//
//		registry.addInterceptor(needLogoutInterceptor).addPathPatterns("/usr/member/login")
//				.addPathPatterns("/usr/member/doLogin").addPathPatterns("/usr/member/join")
//				.addPathPatterns("/usr/member/doJoin");
		
		InterceptorRegistration ir;
		ir = registry.addInterceptor(beforeActionInterceptor);
		ir.addPathPatterns("/**");
		ir.addPathPatterns("/favicon.ico");
		ir.excludePathPatterns("/resource/**");
		ir.excludePathPatterns("/error");

//		로그인 필요
		ir = registry.addInterceptor(needLoginInterceptor);
//		글 관련
		ir.addPathPatterns("/usr/article/write");
		ir.addPathPatterns("/usr/article/doWrite");
		ir.addPathPatterns("/usr/article/modify");
		ir.addPathPatterns("/usr/article/doModify");
		ir.addPathPatterns("/usr/article/doDelete");

//		회원관련
		ir.addPathPatterns("/usr/member/myPage");
		ir.addPathPatterns("/usr/member/checkPw");
		ir.addPathPatterns("/usr/member/doCheckPw");
		ir.addPathPatterns("/usr/member/doLogout");
		ir.addPathPatterns("/usr/member/modify");
		ir.addPathPatterns("/usr/member/doModify");

//		댓글 관련
		ir.addPathPatterns("/usr/reply/doWrite");

//		좋아요 싫어요
		ir.addPathPatterns("/usr/reactionPoint/doGoodReaction");
		ir.addPathPatterns("/usr/reactionPoint/doBadReaction");

//		로그아웃 필요
		ir = registry.addInterceptor(needLogoutInterceptor);
		ir.addPathPatterns("/usr/member/login");
		ir.addPathPatterns("/usr/member/doLogin");
		ir.addPathPatterns("/usr/member/join");
		ir.addPathPatterns("/usr/member/doJoin");
		ir.addPathPatterns("/usr/member/findLoginId");
		ir.addPathPatterns("/usr/member/doFindLoginId");
		ir.addPathPatterns("/usr/member/findLoginPw");
		ir.addPathPatterns("/usr/member/doFindLoginPw");
	}

}