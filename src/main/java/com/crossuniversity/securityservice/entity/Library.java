package com.crossuniversity.securityservice.entity;

import com.crossuniversity.securityservice.dto.LibraryDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "library")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "topic")
    private String topic;

    @Column(name = "library_access")
    private boolean libraryAccess;

    @JsonIgnore
    @ManyToMany(cascade = {MERGE, PERSIST, REFRESH}, mappedBy = "ownLibraries", fetch = FetchType.LAZY)
    private List<UniversityUser> owners;

    @JsonIgnore
    @ManyToMany(cascade = {MERGE, PERSIST, REFRESH}, mappedBy = "subscribedLibraries", fetch = FetchType.LAZY)
    private List<UniversityUser> subscribers;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "library_id")
    private List<Document> documents;

    @JsonIgnore
    @ManyToOne(cascade = ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    public static Library parseDtoToEntity(LibraryDTO libraryDTO){
        return Library.builder()
                .title(libraryDTO.getTitle())
                .topic(libraryDTO.getTopic())
                .libraryAccess(libraryDTO.isLibraryAccess())
                .build();
    }
}
