package com.example.blackbirdlofi.service;

import com.example.blackbirdlofi.JPAentity.Instrument;
import com.example.blackbirdlofi.JPAentity.InstrumentItem;
import com.example.blackbirdlofi.repository.InstrumentItemRepository;
import com.example.blackbirdlofi.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstrumentService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private InstrumentItemRepository instrumentItemRepository;

    // 모든 악기 리스트 반환
    public List<Instrument> getAllInstruments() {
        return instrumentRepository.findAll();
    }

    // 선택된 악기 ID로 세부 항목 리스트 반환
    public List<InstrumentItem> getInstrumentItemsByInstrumentId(int instrumentId) {
        // instrumentId로 Instrument 객체를 찾음
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid instrument ID"));

        // Instrument 객체로 세부 항목 리스트를 조회
        return instrumentItemRepository.findByItemsId(instrument);
    }

    // 모든 악기 이름(대분류)을 중복 없이 반환
    public List<String> getUniqueInstrumentNames() {
        List<Instrument> instruments = instrumentRepository.findAll();
        return instruments.stream()
                .map(Instrument::getIstmName)  // 악기 이름만 추출
                .distinct()  // 중복 제거
                .collect(Collectors.toList());
    }
}