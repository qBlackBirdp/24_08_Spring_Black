package com.example.blackbirdlofi.repository;

import com.example.blackbirdlofi.JPAentity.InstrumentItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentItemRepository {
    List<InstrumentItem> findByInstrumentId(int instrumentId);
}
