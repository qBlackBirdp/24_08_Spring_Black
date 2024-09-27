package com.example.blackbirdlofi.repository;

import com.example.blackbirdlofi.JPAentity.InstrumentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstrumentItemRepository extends JpaRepository<InstrumentItem, Integer> {
    List<InstrumentItem> findByItemsId(int itemsId);  // itemsId에 맞춰 메서드 수정
}