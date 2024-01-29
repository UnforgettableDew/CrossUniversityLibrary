package com.unforgettable.crossuniversitylibrary.service;

import com.unforgettable.crossuniversitylibrary.dto.UserProfileDTO;
import com.unforgettable.crossuniversitylibrary.entity.UniversityUser;
import com.unforgettable.crossuniversitylibrary.exception.not_found.UserNotFoundException;
import com.unforgettable.crossuniversitylibrary.mapper.UserProfileMapper;
import com.unforgettable.crossuniversitylibrary.repository.UniversityUserRepository;
import com.unforgettable.crossuniversitylibrary.utils.SecurityUtils;
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
        UniversityUser universityUser = universityUserRepository.findUniversityUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email = '" + email + "' not found"));
        return userProfileMapper.mapToDTO(universityUser);
    }

    public UserProfileDTO profile() {
        UniversityUser universityUser = securityUtils.getUserFromSecurityContextHolder();
        return getUserProfileByEmail(universityUser.getUserCredentials().getEmail());
    }
}
