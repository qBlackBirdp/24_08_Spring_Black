package com.example.blackbirdlofi.vo;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.service.MemberService;
import com.example.blackbirdlofi.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {

	@Getter
	private boolean isLogined;
	@Getter
	private int loginedMemberId;
	@Getter
	private Member loginedMember;

	private HttpServletRequest req;
	private HttpServletResponse resp;

	public Rq(HttpServletRequest req, HttpServletResponse resp, MemberService memberService) {
		this.req = req;
		this.resp = resp;

		// 로그인 상태 확인
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated() && !authentication.getPrincipal().equals("anonymousUser")) {
			isLogined = true;
			loginedMember = memberService.getMemberByEmail(((UserDetails) authentication.getPrincipal()).getUsername());
			loginedMemberId = loginedMember.getId();
		} else {
			isLogined = false;
			loginedMemberId = 0;
			loginedMember = null;
		}

		this.req.setAttribute("rq", this);
	}

	// 세션에 로그인 정보 저장
	public void login(Member member) {
		HttpSession session = req.getSession();
		session.setAttribute("loginedMemberId", member.getId());
		session.setAttribute("loginedMemberEmail", member.getEmail());
		isLogined = true;
		loginedMemberId = member.getId();
		loginedMember = member;
	}

	// 로그아웃 처리
	public void logout() {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();  // 세션 무효화
		}
		isLogined = false;
		loginedMemberId = 0;
		loginedMember = null;
		SecurityContextHolder.clearContext();  // Spring Security의 SecurityContext 초기화
	}

	public void initBeforeActionInterceptor() {
		System.err.println("initBeforeActionInterceptor 실행");

		// 현재 요청 경로를 세션에 저장할 수 있도록 설정
		String currentUri = getCurrentUri();
		req.setAttribute("currentUri", currentUri);
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

	public String historyBackOnView(String msg) {
		req.setAttribute("msg", msg);
		req.setAttribute("historyBack", true);
		return "usr/common/js";
	}

	public String getCurrentUri() {
		String currentUri = req.getRequestURI();
		String queryString = req.getQueryString();

		if (currentUri != null && queryString != null) {
			currentUri += "?" + queryString;
		}

		return currentUri;
	}
}
