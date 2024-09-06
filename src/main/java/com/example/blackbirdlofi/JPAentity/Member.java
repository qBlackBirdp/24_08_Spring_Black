package com.example.blackbirdlofi.JPAentity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;


@Entity
@Table(name = "users")  // DB의 테이블 이름과 매핑
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Primary Key 자동 생성
    private int id;

    @Column(name = "reg_date", nullable = false, columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime regDate;

    @Column(name = "update_date", nullable = false, columnDefinition = "DATETIME(6) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime updateDate;

    @PrePersist
    protected void onCreate() {
        this.regDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }

    @Column(name = "login_id", nullable = false, length = 50)
    private String loginId;

    @Column(name = "login_pw", nullable = false, length = 100)
    private String loginPw;
    // 비밀번호 암호화 저장
    public void setLoginPw(String loginPw) {
        this.loginPw = new BCryptPasswordEncoder().encode(loginPw);
    }

    @Column(name = "u_name", nullable = false, length = 20)
    private String uName;

    @Column(name = "nickname", nullable = false, length = 20)
    private String nickname;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "del_status", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean delStatus;

    @Column(name = "del_date", columnDefinition = "DATETIME(6)")
    private LocalDateTime delDate;

    @Column(name = "sell_level", nullable = false, columnDefinition = "SMALLINT(2) DEFAULT 3")
    private int sellLevel;

    @Column(name = "auth_level", nullable = false, columnDefinition = "SMALLINT(2) DEFAULT 3")
    private int authLevel;

    @Column(name = "spotify_login_id", length = 100)
    private String spotifyLoginId;

    @Column(name = "google_login_id", length = 100)
    private String googleLoginId;
}
