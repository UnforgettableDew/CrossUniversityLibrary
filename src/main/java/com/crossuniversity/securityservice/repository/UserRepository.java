package com.crossuniversity.securityservice.repository;

import com.crossuniversity.securityservice.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    AppUser findByEmail(String email);
    @Query("select u.id from AppUser u where u.email=:email")
    Long findUserIdByEmail(@Param("email") String email);

}
