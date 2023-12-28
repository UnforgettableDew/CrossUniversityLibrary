package com.crossuniversity.securityservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "university")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "domain", nullable = false)
    private String domain;
}
