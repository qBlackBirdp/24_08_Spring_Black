package com.example.blackbirdlofi.service;

import com.example.blackbirdlofi.repository.MemberRepository;
import com.example.blackbirdlofi.vo.Member;
import com.example.blackbirdlofi.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    // 이메일 중복 체크.
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }
    // 회원가입.
    public ResultData doJoin(String loginId, String loginPw, String uName, String nickname, String email) {

        // 새로운 회원 생성
        Member newMember = new Member();
        newMember.setLoginId(loginId);
        newMember.setLoginPw(loginPw);
        newMember.setUName(uName);
        newMember.setNickname(nickname);
        newMember.setEmail(email);


        // 회원 저장
        memberRepository.doJoin(newMember);

        return ResultData.from("S-1", "회원가입이 완료되었습니다.", "생성된 회원 id", newMember.getId());
    }


//    public Member getMemberById(int data1) {
//    }
}
