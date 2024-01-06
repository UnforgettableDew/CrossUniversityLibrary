package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.dto.UniversityDTO;
import com.crossuniversity.securityservice.entity.University;
import com.crossuniversity.securityservice.auth.UniversityRegisterRequest;
import com.crossuniversity.securityservice.mapper.UniversityMapper;
import com.crossuniversity.securityservice.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversityService {
    private final UniversityRepository universityRepository;
    private final UniversityMapper universityMapper;

    @Autowired
    public UniversityService(UniversityRepository universityRepository,
                             UniversityMapper universityMapper) {
        this.universityRepository = universityRepository;
        this.universityMapper = universityMapper;
    }

    public UniversityDTO registerUniversity(UniversityRegisterRequest request) {
        University university = University.builder()
                .title(request.getTitle())
                .domain(request.getDomain())
                .build();
        universityRepository.save(university);

        return universityMapper.mapToDTO(university);
    }
}
