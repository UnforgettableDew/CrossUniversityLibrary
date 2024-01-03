package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.dto.UniversityDTO;
import com.crossuniversity.securityservice.entity.University;
import com.crossuniversity.securityservice.model.UniversityRegisterRequest;
import com.crossuniversity.securityservice.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversityService {
    private final UniversityRepository universityRepository;

    @Autowired
    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public UniversityDTO registerUniversity(UniversityRegisterRequest request) {
        University university = University.builder()
                .title(request.getTitle())
                .domain(request.getDomain())
                .build();
        universityRepository.save(university);

        return UniversityDTO.parseEntityToDto(university);
    }
}
