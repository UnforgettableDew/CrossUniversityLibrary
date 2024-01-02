package com.crossuniversity.securityservice.repository;

import com.crossuniversity.securityservice.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibraryRepository extends JpaRepository<Library, Long> {
    List<Library> findLibrariesByUniversityIdAndLibraryAccess(Long universityId, boolean libraryAccess);
    List<Library> findLibrariesByTitle(String title);
    List<Library> findLibrariesByTopic(String topic);
    List<Library> findLibrariesByTitleAndTopic(String title, String topic);

    @Query("select l from Library l join l.owners lo where lo.userName=:userName")
    List<Library> findLibrariesByOwner(String userName);

    @Query("select l from Library l join l.owners lo " +
            "where lo.userName=:userName and l.topic=:topic")
    List<Library> findLibrariesByTopicAndOwner(String topic, String userName);

    @Query("select l from Library l join l.owners lo " +
            "where lo.userName=:userName and l.title=:title")
    List<Library> findLibrariesByTitleAndOwner(String title, String userName);


    @Query("select l from Library l join l.owners lo " +
            "where lo.userName=:userName and l.title=:title and l.topic=:topic")
    List<Library> findLibrariesByTitleAndTopicAndOwner(String title, String topic, String userName);
}
