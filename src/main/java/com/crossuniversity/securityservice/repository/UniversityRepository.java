package com.crossuniversity.securityservice.repository;

import com.crossuniversity.securityservice.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
    University findUniversityByDomain(String domain);
}
