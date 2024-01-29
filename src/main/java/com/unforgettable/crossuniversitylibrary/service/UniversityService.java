package com.unforgettable.crossuniversitylibrary.service;

import com.unforgettable.crossuniversitylibrary.dto.UniversityDTO;
import com.unforgettable.crossuniversitylibrary.entity.University;
import com.unforgettable.crossuniversitylibrary.exception.bad_request.UniversityAlreadyExistsException;
import com.unforgettable.crossuniversitylibrary.helper.UniversityHelper;
import com.unforgettable.crossuniversitylibrary.mapper.UniversityMapper;
import com.unforgettable.crossuniversitylibrary.repository.UniversityRepository;
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
