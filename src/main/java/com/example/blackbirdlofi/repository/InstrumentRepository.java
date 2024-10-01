package com.example.blackbirdlofi.repository;

import com.example.blackbirdlofi.JPAentity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Integer> {

    // 대분류 악기 타입만 가져오는 쿼리
    @Query("""
        SELECT DISTINCT i.istmType
        FROM Instrument i
        WHERE i.id NOT IN (SELECT ii.instrument.id FROM InstrumentItem ii)
        """)
    List<String> findDistinctInstrumentTypes();


    List<Instrument> findByIstmType(String instrumentType);
}
