package com.unforgettable.crossuniversitylibrary.repository;

import com.unforgettable.crossuniversitylibrary.entity.UniversityUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UniversityUserRepository extends JpaRepository<UniversityUser, Long> {
    @Query("select uu from UniversityUser uu join uu.userCredentials uc where uc.email=:email")
    Optional<UniversityUser> findUniversityUserByEmail(String email);
}
