package com.crossuniversity.securityservice.repository;

import com.crossuniversity.securityservice.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    UserRole findUserRoleByRoleName(String userRole);
}
