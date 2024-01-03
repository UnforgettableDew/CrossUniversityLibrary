package com.crossuniversity.securityservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "topic")
    private String topic;

    @Column(name = "description", length = 1024)
    private String description;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "owner_id")
    @JsonIgnore
    private UniversityUser owner;

    @ManyToMany(cascade = ALL, fetch = FetchType.LAZY, mappedBy = "documents")
    @JsonIgnore
    private List<Library> libraries;

    public void addLibrary(Library library){
        this.libraries.add(library);
    }
}
