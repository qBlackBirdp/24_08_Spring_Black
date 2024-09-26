package com.example.blackbirdlofi.service;

import com.example.blackbirdlofi.JPAentity.InstrumentItem;
import com.example.blackbirdlofi.repository.InstrumentItemRepository;
import com.example.blackbirdlofi.repository.InstrumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sound.midi.Instrument;
import java.util.List;

@Service
public class InstrumentService {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private InstrumentItemRepository instrumentItemRepository;

    public List<Instrument> getAllInstruments() {
        return instrumentRepository.findAll();
    }

    public List<InstrumentItem> getInstrumentItemsByInstrumentId(int instrumentId) {
        return instrumentItemRepository.findByInstrumentId(instrumentId);
    }
}