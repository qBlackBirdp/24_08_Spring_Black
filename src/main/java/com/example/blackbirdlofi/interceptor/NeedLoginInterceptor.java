package com.example.blackbirdlofi.interceptor;

import com.example.blackbirdlofi.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class NeedLoginInterceptor implements HandlerInterceptor {
	
	@Autowired
	private Rq rq;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

		Rq rq = (Rq) req.getAttribute("rq");

		if (!rq.isLogined()) {
			System.err.println("==================로그인 하고 써====================");
//			resp.getWriter().append("<script>~~~~");

			rq.printHistoryBack("로그인 후 이용해주세요.");

			return false;

		}

		return HandlerInterceptor.super.preHandle(req, resp, handler);
	}
}