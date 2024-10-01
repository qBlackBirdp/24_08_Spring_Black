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

    // 대분류 악기 타입만 반환
    public List<String> getAllInstrumentTypes() {
        return instrumentRepository.findDistinctInstrumentTypes();
    }

    // 선택된 악기 타입으로 세부 항목 리스트 반환 (대분류 악기는 제외)
    public List<InstrumentItem> getInstrumentItemsByInstrumentType(String instrumentType) {
        // instrumentType으로 Instrument 리스트를 가져옴
        List<Instrument> instruments = instrumentRepository.findByIstmType(instrumentType);

        if (instruments.isEmpty()) {
            throw new IllegalArgumentException("Invalid instrument type: " + instrumentType);
        }

        // Instrument 리스트로 InstrumentItem 리스트를 조회
        return instrumentItemRepository.findByInstrumentIn(instruments);
    }

    // 모든 악기 타입(대분류)을 중복 없이 반환
    public List<String> getUniqueInstrumentTypes() {
        List<Instrument> instruments = instrumentRepository.findAll();
        return instruments.stream()
                .map(Instrument::getIstmType)  // 악기 이름만 추출
                .distinct()  // 중복 제거
                .collect(Collectors.toList());
    }
}