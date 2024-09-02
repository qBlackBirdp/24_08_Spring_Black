package com.example.blackbirdlofi.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    private int id;
    private String regDate;
    private String updateDate;
    private String loginId;
    private String loginPw;
    private String uName;
    private String nickname;
    private String cellphoneNum;
    private String email;
    private boolean delStatus;
    private String delDate;
    private int sellLevel;
    private int authLevel;
    private String spotifyLoginId;
    private String googleLoginId;
}