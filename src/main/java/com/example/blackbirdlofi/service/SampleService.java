package com.example.blackbirdlofi.service;

import com.example.blackbirdlofi.JPAentity.Sample;
import com.example.blackbirdlofi.repository.SampleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SampleService {
    private final SampleRepository sampleRepository;

    @Autowired
    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;
    }

    public Sample saveSample(Sample sample) {
        return sampleRepository.save(sample);
    }
}
