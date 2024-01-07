package com.crossuniversity.securityservice.helper;

import com.crossuniversity.securityservice.entity.University;
import com.crossuniversity.securityservice.exception.not_found.UniversityNotFoundException;
import com.crossuniversity.securityservice.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniversityHelper {
    private final UniversityRepository universityRepository;

    @Autowired
    public UniversityHelper(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public University getUniversityByDomain(String domain) {
        return universityRepository.findUniversityByDomain(domain)
                .orElseThrow(() -> new UniversityNotFoundException("University domain = " + domain + " does not exist"));
    }
}
