package com.example.blackbirdlofi.repository;

import org.springframework.stereotype.Repository;

import javax.sound.midi.Instrument;
import java.util.List;

@Repository
public interface InstrumentRepository {
    List<Instrument> findAll();
}
