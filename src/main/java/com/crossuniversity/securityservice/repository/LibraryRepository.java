package com.crossuniversity.securityservice.repository;

import com.crossuniversity.securityservice.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    List<Library> findLibrariesByUniversityIdAndLibraryAccess(Long universityId, boolean libraryAccess);
    List<Library> findLibrariesByTitle(String title);
    List<Library> findLibrariesByTopic(String topic);
    List<Library> findLibrariesByTitleAndTopic(String title, String topic);

    @Query("select l from Library l join l.owners lo join lo.userCredentials uc " +
            "where uc.email=:email")
    List<Library> findLibrariesByOwner(String email);

    @Query("select l from Library l join l.owners lo join lo.userCredentials uc " +
            "where uc.email=:email and l.topic=:topic")
    List<Library> findLibrariesByTopicAndOwner(String topic, String email);

    @Query("select l from Library l join l.owners lo join lo.userCredentials uc " +
            "where uc.email=:email and l.title=:title")
    List<Library> findLibrariesByTitleAndOwner(String title, String email);


    @Query("select l from Library l join l.owners lo join lo.userCredentials uc " +
            "where uc.email=:email and l.title=:title and l.topic=:topic")
    List<Library> findLibrariesByTitleAndTopicAndOwner(String title, String topic, String email);

    @Modifying
    @Transactional
    @Query(value = "delete from library where library.id =:libraryId", nativeQuery = true)
    int deleteLibrary(Long libraryId);
}
