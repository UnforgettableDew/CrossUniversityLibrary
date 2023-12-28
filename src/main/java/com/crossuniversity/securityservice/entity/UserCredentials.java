package com.crossuniversity.securityservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_credentials")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserCredentials {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private UserRole role;

}