package com.example.blackbirdlofi.controller;

import com.example.blackbirdlofi.JPAentity.Member;
import com.example.blackbirdlofi.service.MemberService;
import com.example.blackbirdlofi.util.Ut;
import com.example.blackbirdlofi.vo.ResultData;
import com.example.blackbirdlofi.vo.Rq;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.UUID;

@Controller
@SessionAttributes("member")
@RequestMapping("/usr/member/")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private Rq rq;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;  // BCryptPasswordEncoder 주입

    @RequestMapping("join")
    public String showJoin() {
        return "usr/member/join";
    }

    @RequestMapping("doJoin")
    @ResponseBody
    public String doJoin(HttpServletRequest req, String loginPw,
                         String name, String nickname, String email) {
        rq = (Rq) req.getAttribute("rq");

        if (Ut.isEmptyOrNull(email)) {
            System.out.println("이메일이 비었습니다.");
            return Ut.jsHistoryBack("F-6", Ut.f("이메일을 입력해주세요."));
        }

        if (Ut.isEmptyOrNull(loginPw)) {
            System.out.println("비번이 비었습니다.");
            return Ut.jsHistoryBack("F-2", Ut.f("비밀번호를 입력해주세요."));
        }
        if (Ut.isEmptyOrNull(name))
            return Ut.jsHistoryBack("F-3", Ut.f("이름을 입력해주세요."));

        if (Ut.isEmptyOrNull(nickname))
            return Ut.jsHistoryBack("F-4", Ut.f("닉네임을 입력해주세요."));

        // 이메일 중복 체크
        Member existingMember = memberService.getMemberByEmail(email);
        if (existingMember != null) {
            return Ut.jsHistoryBack("F-7", Ut.f("해당 이메일은 이미 사용 중입니다."));
        }

        // 자동 생성된 loginId (user-랜덤UUID 형식)
        String loginId = "user-" + UUID.randomUUID().toString().substring(0, 8);

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(loginPw);

        ResultData doJoinRd = memberService.doJoin(loginId, loginPw, name, nickname, email);

        if (doJoinRd.isFail()) {
            return Ut.jsHistoryBack(doJoinRd.getResultCode(), doJoinRd.getMsg());
        }

//        Member member = memberService.getMemberById((int) doJoinRd.getData1());

        return Ut.jsReplace(doJoinRd.getResultCode(), doJoinRd.getMsg(), "usr/member/login");
    }

    @RequestMapping("login")
    public String showLogin(HttpServletRequest req) {
        System.err.println("+++++++++++++로그인 실행.+++++++++");
        return "usr/member/login";
    }

//  로컬 로그인 메서드
//    @RequestMapping("doLocalLogin")
//    @ResponseBody
//    public String doLocalLogin(HttpServletRequest req, @RequestParam("username") String email, @RequestParam("password") String loginPw, String afterLoginUri) {
//        System.err.println("+++++++++++++DO 로컬 로그인 실행.+++++++++");
//        rq = (Rq) req.getAttribute("rq");
//
//        Member member = memberService.getMemberByEmail(email);
//        System.out.println("Fetched Member: " + member);
//        System.out.println("Fetched Password: " + member.getLoginPw());
//
//        if (member == null) {
//            return Ut.jsHistoryBack("F-3", Ut.f("%s는(은) 존재하지 않습니다.", email));
//        }
//
//        if (!member.getLoginPw().equals(loginPw)) {
//            return Ut.jsHistoryBack("F-4", Ut.f("비밀번호가 틀렸습니다."));
//        }
//
//        rq.login(member);
//
//        if (afterLoginUri != null && !afterLoginUri.isEmpty()) {
//            return Ut.jsReplace("S-1", Ut.f("%s님 환영합니다", member.getNickname()), afterLoginUri);
//        }
//
//        return Ut.jsReplace("S-1", Ut.f("%s님 환영합니다", member.getNickname()), "/");
//    }

    //구글 로그인 메서드
    public String doGLogin(HttpServletRequest req, @RequestParam("username") String email, @RequestParam("password") String password) {
        return "google";
    }

    @RequestMapping("doLogout")
    @ResponseBody
    public String doLogout(HttpServletRequest req, SessionStatus sessionStatus) {

        // 로그아웃 처리1
        rq.logout();
        sessionStatus.setComplete();

        return Ut.jsReplace("S-1", Ut.f("로그아웃 성공"), "/");
    }

}
