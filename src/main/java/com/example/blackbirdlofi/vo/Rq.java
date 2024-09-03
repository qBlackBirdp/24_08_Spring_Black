package com.example.blackbirdlofi.vo;

import com.example.blackbirdlofi.service.MemberService;
import com.example.blackbirdlofi.util.Ut;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Rq {

	@Getter
	private boolean isLogined = false;  // 초기화를 명시적으로 수행
	@Getter
	private int loginedMemberId = 0;    // 초기화를 명시적으로 수행
	@Getter
	private Member loginedMember;

	private HttpServletRequest req;
	private HttpServletResponse resp;

	private HttpSession session;

	public Rq(HttpServletRequest req, HttpServletResponse resp, MemberService memberService) {
		this.req = req;
		this.resp = resp;
		this.session = req.getSession();

		System.out.println("Rq 객체 생성됨");

		if (session.getAttribute("loginedMemberId") != null) {
			isLogined = true;
			loginedMemberId = (int) session.getAttribute("loginedMemberId");
			loginedMember = memberService.getMemberById(loginedMemberId);
		}

		this.req.setAttribute("rq", this);
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

	public void logout() {
		session.removeAttribute("loginedMemberId");
		session.removeAttribute("loginedMember");
		isLogined = false;
		loginedMemberId = 0;
		loginedMember = null;
	}

	public void login(Member member) {
		session.setAttribute("loginedMemberId", member.getId());
		session.setAttribute("loginedMember", member);
		isLogined = true;
		loginedMemberId = member.getId();
		loginedMember = member;
	}

	public void initBeforeActionInterceptor() {
		System.err.println("initBeforeActionInterceptor 실행");
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
