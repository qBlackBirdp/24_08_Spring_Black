package com.example.blackbirdlofi.JPAentity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instruments_items")  // DB의 테이블 이름과 매핑
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InstrumentItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Primary Key 자동 생성
    private int id;

    // ManyToOne 관계 설정: Instrument와 외래키 관계
    @ManyToOne
    @JoinColumn(name = "items_id")  // 외래 키 컬럼 이름이 'items_id'
    private Instrument itemsId; // 악기 ID

    @Column(name = "items_name")
    private String itemsName; // 악기 이름
}
