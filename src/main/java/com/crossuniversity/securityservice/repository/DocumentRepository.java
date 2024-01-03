package com.crossuniversity.securityservice.repository;

import com.crossuniversity.securityservice.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Modifying
    @Transactional
    @Query(value = "delete from documents where documents.id=:documentId", nativeQuery = true)
    int deleteDocumentById(Long documentId);
}
