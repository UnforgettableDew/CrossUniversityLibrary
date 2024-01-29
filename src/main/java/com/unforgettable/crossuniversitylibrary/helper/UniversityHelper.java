package com.unforgettable.crossuniversitylibrary.helper;

import com.unforgettable.crossuniversitylibrary.entity.University;
import com.unforgettable.crossuniversitylibrary.exception.not_found.UniversityNotFoundException;
import com.unforgettable.crossuniversitylibrary.repository.UniversityRepository;
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
