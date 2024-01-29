package com.unforgettable.crossuniversitylibrary.repository;

import com.unforgettable.crossuniversitylibrary.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findUserRoleByRoleName(String userRole);
}
