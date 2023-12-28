package com.crossuniversity.securityservice.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private UniversityUser owner;
}
