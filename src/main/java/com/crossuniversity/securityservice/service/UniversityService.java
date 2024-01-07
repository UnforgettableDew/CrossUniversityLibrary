package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.dto.UniversityDTO;
import com.crossuniversity.securityservice.entity.University;
import com.crossuniversity.securityservice.exception.bad_request.UniversityAlreadyExistsException;
import com.crossuniversity.securityservice.helper.UniversityHelper;
import com.crossuniversity.securityservice.mapper.UniversityMapper;
import com.crossuniversity.securityservice.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversityService {
    private final UniversityRepository universityRepository;
    private final UniversityMapper universityMapper;
    private final UniversityHelper universityHelper;

    @Autowired
    public UniversityService(UniversityRepository universityRepository,
                             UniversityMapper universityMapper,
                             UniversityHelper universityHelper) {
        this.universityRepository = universityRepository;
        this.universityMapper = universityMapper;
        this.universityHelper = universityHelper;
    }

    public UniversityDTO registerUniversity(String title, String domain) {
        if (universityHelper.getUniversityByDomain(domain) != null)
            throw new UniversityAlreadyExistsException("University domain = '" + domain + "' already exists");

        University university = University.builder()
                .title(title)
                .domain(domain)
                .build();
        universityRepository.save(university);

        return universityMapper.mapToDTO(university);
    }
}
