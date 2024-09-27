package com.example.blackbirdlofi.repository;

import com.example.blackbirdlofi.JPAentity.Instrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Integer> {
}
