package com.example.blackbirdlofi.JPAentity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")  // DB의 테이블 이름과 매핑
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Primary Key 자동 생성
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