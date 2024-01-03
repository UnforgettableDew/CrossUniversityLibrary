package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.dto.UserProfileDTO;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.repository.UniversityUserRepository;
import com.crossuniversity.securityservice.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final SecurityUtils securityUtils;
    private final UniversityUserRepository universityUserRepository;

    public UserService(SecurityUtils securityUtils,
                       UniversityUserRepository universityUserRepository) {
        this.securityUtils = securityUtils;
        this.universityUserRepository = universityUserRepository;
    }

    public UserProfileDTO updateProfile(String userName) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        universityUser.setUserName(userName);
        UserProfileDTO userProfileDTO = UserProfileDTO.parseEntityToDto(universityUser);

        universityUserRepository.save(universityUser);
        return userProfileDTO;
    }

    public UserProfileDTO getUserProfileByEmail(String email) {
        UniversityUser universityUser = universityUserRepository.findUniversityUserByEmail(email).orElseThrow();
        return UserProfileDTO.parseEntityToDto(universityUser);
    }

    public UserProfileDTO myProfile(){
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        return getUserProfileByEmail(universityUser.getUserCredentials().getEmail());
    }
}
