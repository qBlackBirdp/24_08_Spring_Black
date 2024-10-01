package com.example.blackbirdlofi.repository;

import com.example.blackbirdlofi.JPAentity.Instrument;
import com.example.blackbirdlofi.JPAentity.InstrumentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentItemRepository extends JpaRepository<InstrumentItem, Integer> {
    // Instrument 리스트를 기반으로 InstrumentItem 리스트를 조회
    List<InstrumentItem> findByInstrumentIn(List<Instrument> instruments);
}
