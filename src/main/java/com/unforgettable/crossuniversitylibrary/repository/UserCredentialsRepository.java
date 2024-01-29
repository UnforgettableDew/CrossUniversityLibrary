package com.unforgettable.crossuniversitylibrary.repository;

import com.unforgettable.crossuniversitylibrary.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialsRepository extends JpaRepository<UserCredentials, Integer> {
    Optional<UserCredentials> findByEmail(String email);

}
