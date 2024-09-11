package com.example.blackbirdlofi.controller;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.service.MemberService;
import com.example.blackbirdlofi.util.Ut;
import com.example.blackbirdlofi.vo.ResultData;
import com.example.blackbirdlofi.vo.Rq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

@Controller
@RequestMapping("/usr/member/")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private Rq rq;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("join")
    public String showJoin() {
        return "usr/member/join";
    }

    @RequestMapping("doJoin")
    @ResponseBody
    public String doJoin(HttpServletRequest req, String loginPw,
                         String name, String nickname, String email) {

        if (Ut.isEmptyOrNull(email)) {
            return Ut.jsHistoryBack("F-6", Ut.f("이메일을 입력해주세요."));
        }

        if (Ut.isEmptyOrNull(loginPw)) {
            return Ut.jsHistoryBack("F-2", Ut.f("비밀번호를 입력해주세요."));
        }

        if (Ut.isEmptyOrNull(name)) {
            return Ut.jsHistoryBack("F-3", Ut.f("이름을 입력해주세요."));
        }

        if (Ut.isEmptyOrNull(nickname)) {
            return Ut.jsHistoryBack("F-4", Ut.f("닉네임을 입력해주세요."));
        }

        // 이메일 중복 체크
        Member existingMember = memberService.getMemberByEmail(email);
        if (existingMember != null) {
            return Ut.jsHistoryBack("F-7", Ut.f("해당 이메일은 이미 사용 중입니다."));
        }

        // 자동 생성된 loginId (user-랜덤UUID 형식)
        String loginId = "user-" + UUID.randomUUID().toString().substring(0, 8);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(loginPw);

        ResultData<Member> doJoinRd = memberService.doJoin(loginId, encodedPassword, name, nickname, email);

        if (doJoinRd.isFail()) {
            return Ut.jsHistoryBack(doJoinRd.getResultCode(), doJoinRd.getMsg());
        }

        return Ut.jsReplace(doJoinRd.getResultCode(), doJoinRd.getMsg(), "usr/member/login");
    }

    // 로컬 로그인 처리
    @PostMapping("/doLocalLogin")
    @ResponseBody
    public ResultData<Member> doLocalLogin(@RequestParam String email, @RequestParam String loginPw) {
        // 로그인 시도
        ResultData<Member> loginResult = memberService.doLogin(email, loginPw);

        // 로그인 성공 시 처리
        if (loginResult.isSuccess()) {
            // 세션에 로그인 정보를 저장
            rq.login(loginResult.getData1());

            // 로그인된 사용자 정보를 함께 반환 (JSON)
            return ResultData.from("S-1", "로그인 성공", "member", loginResult.getData1());
        }

        // 로그인 실패 시 실패 결과 반환
        return loginResult;
    }

    // 로그인 페이지 렌더링
    @GetMapping("/login")
    public ModelAndView loginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return new ModelAndView("redirect:/usr/home/main");  // 이미 로그인된 사용자는 메인 페이지로 리다이렉트
        }

        return new ModelAndView("usr/member/login");  // 로그인 페이지 렌더링
    }

    // 로그아웃 처리
    @RequestMapping("/doLogout")
    @ResponseBody
    public String doLogout(HttpServletRequest req, SessionStatus sessionStatus) {
        rq.logout();  // Rq에서 세션과 Spring Security 로그아웃 처리
        sessionStatus.setComplete();

        return Ut.jsReplace("S-1", Ut.f("로그아웃 성공"), "/");
    }
}
