package com.unforgettable.crossuniversitylibrary.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.unforgettable.crossuniversitylibrary.enums.UserPermission.*;

public enum UserRoles {
    STUDENT(Set.of(
            USER_READ,
            LIBRARY_CREATE_COMMON,
            LIBRARY_READ,
            LIBRARY_SUBSCRIBE,
            LIBRARY_UNSUBSCRIBE,
            LIBRARY_SUBSCRIBE_USER,
            LIBRARY_UPDATE,
            LIBRARY_DELETE,

            DOCUMENT_UPLOAD,
            DOCUMENT_DOWNLOAD,
            DOCUMENT_ADD,
            DOCUMENT_REMOVE,
            DOCUMENT_READ,
            DOCUMENT_UPDATE,
            DOCUMENT_DELETE)),

    TEACHER(STUDENT.getPermissions(), LIBRARY_CREATE_WITH_ACCESS),

    UNIVERSITY_ADMIN(
            TEACHER.getPermissions(),
            TEACHER_REGISTER,
            TEACHER_UPDATE,
            TEACHER_DELETE
    ),

    GLOBAL_ADMIN(
            UNIVERSITY_ADMIN.getPermissions(),

            UNIVERSITY_ADMIN_REGISTER,
            UNIVERSITY_ADMIN_UPDATE,
            UNIVERSITY_ADMIN_DELETE,

            UNIVERSITY_CREATE,
            UNIVERSITY_READ,
            UNIVERSITY_UPDATE,
            UNIVERSITY_DELETE
    );

    private final Set<UserPermission> permissions;

    UserRoles(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    UserRoles(Set<UserPermission> permissions, UserPermission... additionalPermissions) {
        this.permissions = new HashSet<>(permissions);
        this.permissions.addAll(Set.of(additionalPermissions));
    }

    public Set<UserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getSimpleGrantedAuthority() {
        Set<SimpleGrantedAuthority> permissions = this.getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermissionName()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
