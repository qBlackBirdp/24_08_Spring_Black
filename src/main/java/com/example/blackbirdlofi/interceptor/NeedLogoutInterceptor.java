package com.example.blackbirdlofi.interceptor;

import com.example.blackbirdlofi.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class NeedLogoutInterceptor implements HandlerInterceptor {
	
	@Autowired
	private Rq rq;

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {

		Rq rq = (Rq) req.getAttribute("rq");

		if (rq.isLogined()) {
			System.err.println("==================로그아웃 하고 써====================");

			rq.printHistoryBack("로그아웃부터 해주세요.");

			return false;

		}

		return HandlerInterceptor.super.preHandle(req, resp, handler);
	}

}