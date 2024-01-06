package com.crossuniversity.securityservice.service;

import com.crossuniversity.securityservice.dto.UserProfileDTO;
import com.crossuniversity.securityservice.entity.UniversityUser;
import com.crossuniversity.securityservice.mapper.UserProfileMapper;
import com.crossuniversity.securityservice.repository.UniversityUserRepository;
import com.crossuniversity.securityservice.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final SecurityUtils securityUtils;
    private final UniversityUserRepository universityUserRepository;
    private final UserProfileMapper userProfileMapper;

    public UserService(SecurityUtils securityUtils,
                       UniversityUserRepository universityUserRepository,
                       UserProfileMapper userProfileMapper) {
        this.securityUtils = securityUtils;
        this.universityUserRepository = universityUserRepository;
        this.userProfileMapper = userProfileMapper;
    }

    public UserProfileDTO updateProfile(String userName) {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        universityUser.setUserName(userName);
        UserProfileDTO userProfileDTO = userProfileMapper.mapToDTO(universityUser);

        universityUserRepository.save(universityUser);
        return userProfileDTO;
    }

    public UserProfileDTO getUserProfileByEmail(String email) {
        UniversityUser universityUser = universityUserRepository.findUniversityUserByEmail(email).orElseThrow();
        return userProfileMapper.mapToDTO(universityUser);
    }

    public UserProfileDTO myProfile(){
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        return getUserProfileByEmail(universityUser.getUserCredentials().getEmail());
    }
}
