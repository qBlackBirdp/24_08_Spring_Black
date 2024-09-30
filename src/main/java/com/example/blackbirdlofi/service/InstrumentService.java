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

    // 선택된 악기의 항목 리스트 반환
    public List<String> getUniqueInstrumentNames() {
        List<Instrument> instruments = instrumentRepository.findAll();
        return instruments.stream()
                .map(Instrument::getIstmName)
                .distinct()
                .collect(Collectors.toList());
    }

    // 선택된 악기 대분류에 맞는 세부 항목 가져오기
    public List<InstrumentItem> getInstrumentItemsByInstrumentId(int instrumentId) {
        return instrumentItemRepository.findByInstrumentId(instrumentId);
    }
}