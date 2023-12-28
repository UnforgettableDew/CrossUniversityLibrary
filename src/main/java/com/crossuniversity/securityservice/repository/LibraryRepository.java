package com.crossuniversity.securityservice.repository;

import com.crossuniversity.securityservice.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {

}
