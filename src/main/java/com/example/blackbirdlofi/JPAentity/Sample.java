package com.example.blackbirdlofi.JPAentity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "samples")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sample {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "u_id")
    private long userId; // 유저 ID (FK)

    @Column(name = "istm_id")
    private long instrumentId; // 악기 ID (FK)

    @Column(name = "s_name")
    private String sName; // 샘플 이름

    @Column(name = "bpm")
    private Integer bpm; // BPM 값

    @Column(name = "genres")
    private String genres; // 장르

//    @Column(name = "price", nullable = true)
//    private Integer price; // 가격 (nullable)

    @Column(name = "is_one_shot")
    private Boolean oneShot; // One-shot 여부

    @Column(name = "url")
    private String url; // Firebase Storage URL

    @Column(name = "reg_date")
    private LocalDateTime regDate = LocalDateTime.now(); // 등록 날짜

}