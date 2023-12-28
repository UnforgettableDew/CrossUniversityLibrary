package com.crossuniversity.securityservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "space")
    private Double space;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_credentials_id")
    private UserCredentials userCredentials;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_library_owners",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "library_id")
    )
    private List<Library> ownLibraries;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_library_subscribers",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "library_id")
    )
    private List<Library> subscribedLibraries;
}
