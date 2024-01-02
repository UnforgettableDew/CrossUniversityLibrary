package com.crossuniversity.securityservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Table(name = "university_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UniversityUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "space")
    private Double space;

    @ManyToOne(cascade = ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    @JsonIgnore
    private University university;

    @OneToOne(cascade = ALL)
    @JoinColumn(name = "user_credentials_id")
    @JsonIgnore
    private UserCredentials userCredentials;

    @OneToMany(cascade = ALL, fetch = FetchType.LAZY, mappedBy = "owner")
    private List<Document> documents;

    @ManyToMany(cascade = ALL)
    @JoinTable(
            name = "user_library_owners",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "library_id")
    )
    @JsonIgnore
    private List<Library> ownLibraries;

    @ManyToMany(cascade = ALL)
    @JoinTable(
            name = "user_library_subscribers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "library_id")
    )
    @JsonIgnore
    private List<Library> subscribedLibraries;

    public void addSubscribedLibrary(Library library){
        this.subscribedLibraries.add(library);
    }

    public void addOwnLibrary(Library library){
        this.ownLibraries.add(library);
    }

    public void removeSubscribedLibrary(Library library){
        this.subscribedLibraries.remove(library);
    }

    public void removeOwnLibrary(Library library){
        this.ownLibraries.remove(library);
    }
}
