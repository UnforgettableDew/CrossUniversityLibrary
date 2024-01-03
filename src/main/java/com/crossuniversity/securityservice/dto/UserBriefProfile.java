package com.crossuniversity.securityservice.dto;

import com.crossuniversity.securityservice.entity.UniversityUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserBriefProfile {
    private Long id;
    private String userName;
    private String email;

    public static UserBriefProfile parseEntityToDto(UniversityUser universityUser){
        return UserBriefProfile.builder()
                .id(universityUser.getId())
                .userName(universityUser.getUserName())
                .email(universityUser.getUserCredentials().getEmail())
                .build();
    }
}
