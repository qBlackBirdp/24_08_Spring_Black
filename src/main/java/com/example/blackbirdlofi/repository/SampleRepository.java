package com.example.blackbirdlofi.repository;

import com.example.blackbirdlofi.JPAentity.Sample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleRepository extends JpaRepository<Sample, Long> {
}
