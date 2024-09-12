package com.example.blackbirdlofi.vo;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.util.Ut;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {

	private final HttpServletRequest req;
	private final HttpServletResponse resp;

	// 생성자에 @Autowired 적용하여 Request와 Response를 주입받음
	@Autowired
	public Rq(HttpServletRequest req, HttpServletResponse resp) {
		this.req = req;
		this.resp = resp;
	}
	// 로그인 여부 확인: SecurityContextHolder 사용
	public boolean isLogined() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
	}

	// 로그인 처리 시 호출 (세션에 사용자 정보 저장할 때 사용)
	public void login(Member member) {
		// 필요시 세션에 사용자 정보 추가
		req.getSession().setAttribute("loginedMember", member);
	}

	// 로그아웃 처리
	public void logout() {
		// Spring Security 로그아웃 처리
		SecurityContextHolder.clearContext();  // SecurityContext 초기화
		req.getSession().invalidate();  // 세션 무효화

		// JSESSIONID 쿠키 삭제
		Cookie cookie = new Cookie("JSESSIONID", null);
		cookie.setPath("/");  // 쿠키 경로 설정 (전역 경로로 설정)
		cookie.setMaxAge(0);  // 쿠키 즉시 삭제
		resp.addCookie(cookie);  // 응답에 쿠키 추가
	}

	public void printHistoryBack(String msg) throws IOException {
		resp.setContentType("text/html; charset=UTF-8");
		println("<script>");
		if (!Ut.isEmpty(msg)) {
			println("alert('" + msg + "');");
		}
		println("history.back();");
		println("</script>");
	}

	private void println(String str) {
		print(str + "\n");
	}

	private void print(String str) {
		try {
			resp.getWriter().append(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getCurrentUri() {
		String currentUri = req.getRequestURI();
		String queryString = req.getQueryString();

		if (queryString != null && !queryString.isEmpty()) {
			currentUri += "?" + queryString;
		}

		return currentUri;
	}
}