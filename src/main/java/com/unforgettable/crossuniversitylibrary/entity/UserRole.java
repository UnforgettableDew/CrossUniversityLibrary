package com.unforgettable.crossuniversitylibrary.entity;

import com.unforgettable.crossuniversitylibrary.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_role")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoles roleName;
}
