package com.example.blackbirdlofi.repository;

import com.example.blackbirdlofi.JPAentity.Instrument;
import com.example.blackbirdlofi.JPAentity.InstrumentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentItemRepository extends JpaRepository<InstrumentItem, Integer> {
    // Instrument 객체를 기준으로 세부 항목 리스트를 가져옴
    List<InstrumentItem> findByItemsId(Instrument instrument);
}
