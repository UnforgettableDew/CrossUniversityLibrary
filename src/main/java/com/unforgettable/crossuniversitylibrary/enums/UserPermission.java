package com.unforgettable.crossuniversitylibrary.enums;

public enum UserPermission {
    USER_READ("user:read"),

    LIBRARY_CREATE_COMMON("library:create"),
    LIBRARY_CREATE_WITH_ACCESS("library:create-with-access"),
    LIBRARY_READ("library:read"),
    LIBRARY_SUBSCRIBE("library:subscribe"),
    LIBRARY_UNSUBSCRIBE("library:unsubscribe"),
    LIBRARY_SUBSCRIBE_USER("library:unsubscribe-user"),
    LIBRARY_UPDATE("library:update"),
    LIBRARY_DELETE("library:delete"),

    DOCUMENT_UPLOAD("document:upload"),
    DOCUMENT_DOWNLOAD("document:download"),
    DOCUMENT_ADD("document:add"),
    DOCUMENT_REMOVE("document:remove"),
    DOCUMENT_READ("document:read"),
    DOCUMENT_UPDATE("document:update"),
    DOCUMENT_DELETE("document:delete"),

    TEACHER_REGISTER("teacher:register"),
    TEACHER_UPDATE("teacher:update"),
    TEACHER_DELETE("teacher:delete"),

    UNIVERSITY_ADMIN_REGISTER("university-admin:register"),
    UNIVERSITY_ADMIN_UPDATE("university-admin:update"),
    UNIVERSITY_ADMIN_DELETE("university-admin:delete"),

    UNIVERSITY_CREATE("university:create"),
    UNIVERSITY_READ("university:read"),
    UNIVERSITY_UPDATE("university:update"),
    UNIVERSITY_DELETE("university:delete");


    private final String permissionName;

    UserPermission(String permissionName) {
        this.permissionName = permissionName;
    }

    public String getPermissionName() {
        return permissionName;
    }
}
