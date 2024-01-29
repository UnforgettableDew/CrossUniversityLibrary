package com.unforgettable.crossuniversitylibrary.repository;

import com.unforgettable.crossuniversitylibrary.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    Optional<University> findUniversityByDomain(String domain);
}
