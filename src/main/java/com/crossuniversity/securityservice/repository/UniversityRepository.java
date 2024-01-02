package com.crossuniversity.securityservice.repository;

import com.crossuniversity.securityservice.entity.Library;
import com.crossuniversity.securityservice.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {
}
