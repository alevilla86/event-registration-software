/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service;

import com.ers.core.dao.UserProfileDao;
import com.ers.core.dto.UserProfileDto;
import com.ers.core.exception.ErsAccessDeniedException;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Category;
import com.ers.core.orm.Role;
import com.ers.core.orm.User;
import com.ers.core.orm.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author avillalobos
 */
@Service
public class UserProfileService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserProfileDao userProfileDao;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private CategoryService categoryService;
    
    /**
     * Saves an user profile. Restricted access to package classes only.
     * 
     * @param userProfile
     * @return
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    UserProfile saveUserProfile(UserProfile userProfile) throws ErsException {
        
        if (userProfile == null || StringUtils.isBlank(userProfile.getUserId())) {
            throw new ErsException("User information is missing to create user profile", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        userProfileDao.save(userProfile);
        
        LOGGER.info("UserProfile was created for a user [user={}]", userProfile.getUserId());
        
        return userProfile;
    }
    
    /**
     * Gets the user profile of the logged user.
     * 
     * @param loggedUser
     * @return
     * @throws ErsException 
     */
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfile(User loggedUser) throws ErsException {
        
        //Get the existing user profile.
        UserProfile existingProfile = userProfileDao.getById(loggedUser.getId());
        
        if (existingProfile == null) {
            throw new ErsException("User profile not exists", ErsErrorCode.TARGET_NOT_EXISTS);
        }
        
        return convertToUserProfileDto(existingProfile);
    }
    
    /**
     * Gets an user profile by its id.
     * 
     * @param userProfileId
     * @return
     * @throws ErsException 
     */
    protected UserProfile getUserProfile(String userProfileId) throws ErsException {
        
        //Get the existing user profile.
        UserProfile existingProfile = userProfileDao.getById(userProfileId);
        
        if (existingProfile == null) {
            throw new ErsException("User profile not exists", ErsErrorCode.TARGET_NOT_EXISTS);
        }
        
        return existingProfile;
    }
    
    /**
     * Updates an user profile of the logged user.
     * 
     * @param loggedUser
     * @param userProfileId
     * @param updatedProfile
     * @return
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public UserProfileDto updateUserProfile(User loggedUser, String userProfileId, UserProfileDto updatedProfile) throws ErsException {
        
        //Validate params.
        if (StringUtils.isBlank(userProfileId) || updatedProfile == null) {
            throw new ErsException("Parameters to update user profile are missing", ErsErrorCode.PARAMETER_MISSING);
        }
        
        //Get the existing user profile.
        UserProfile existingProfile = userProfileDao.getById(userProfileId);
        
        if (existingProfile == null) {
            throw new ErsException("User profile not exists", ErsErrorCode.TARGET_NOT_EXISTS);
        }
        
        if (!StringUtils.equals(userProfileId, loggedUser.getId())) {
            throw new ErsAccessDeniedException("You are not authorized to update the user profile");
        }
        
        //Update editable fields.
        existingProfile.setGovernmentId(updatedProfile.getGovernmentId());
        existingProfile.setExternalId(updatedProfile.getExternalId());
        existingProfile.setHand(UserProfile.Hand.getByName(updatedProfile.getHand()));
        existingProfile.setTeam(updatedProfile.getTeam());
        existingProfile.setDateBirth(updatedProfile.getDateBirth());
        existingProfile.setAge(updatedProfile.getAge());
        existingProfile.setPhone(updatedProfile.getPhone());
        existingProfile.setGenre(UserProfile.Genre.getByName(updatedProfile.getGenre()));
        existingProfile.setWeight(updatedProfile.getWeight());
        existingProfile.setHeight(updatedProfile.getHeight());
        existingProfile.setCountry(updatedProfile.getCountry());
        existingProfile.setState(updatedProfile.getState());
        existingProfile.setCity(updatedProfile.getCity());
        existingProfile.setStreet1(updatedProfile.getStreet1());
        existingProfile.setStreet2(updatedProfile.getStreet2());
        existingProfile.setEmergencyContactName(updatedProfile.getEmergencyContactName());
        existingProfile.setEmergencyContactPhone(updatedProfile.getEmergencyContactPhone());
        existingProfile.setShirtSize(UserProfile.ShirtSize.getByName(updatedProfile.getShirtSize()));
        
        Role role = roleService.getByName(updatedProfile.getRole());
        
        Category category = categoryService.getByName(updatedProfile.getCategory());
        
        existingProfile.setRole(role);
        existingProfile.setCategory(category);
        
        validateUserProfileFields(existingProfile);
        
        userProfileDao.update(existingProfile);
        
        LOGGER.info("User profile updated [userProfileId={}, profile={}]", userProfileId, existingProfile);
        
        return convertToUserProfileDto(existingProfile);
    }
    
    /**
     * Validates the user profile required fields.
     * Fields not required in the database, but required per business logic.
     * 
     * @param userProfile 
     */
    private void validateUserProfileFields(UserProfile userProfile) throws ErsException {
        
        if (userProfile == null) {
            throw new ErsException("UserProfile is null", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(userProfile.getGovernmentId())) {
            throw new ErsException("User government id is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getHand() == null) {
            throw new ErsException("User government hand is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getDateBirth() == null) {
            throw new ErsException("User date birth is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getAge() < 0) {
            throw new ErsException("User age is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(userProfile.getPhone())) {
            throw new ErsException("User phone is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getGenre() == null) {
            throw new ErsException("User government idgenre is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(userProfile.getCountry())) {
            throw new ErsException("User country is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(userProfile.getEmergencyContactName())) {
            throw new ErsException("User emergency contact is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(userProfile.getEmergencyContactPhone())) {
            throw new ErsException("User emergency contact phone is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getShirtSize() == null) {
            throw new ErsException("User shirt size is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getRole() == null) {
            throw new ErsException("User role is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getCategory() == null) {
            throw new ErsException("User category is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
    }
    
    /**
     * Converts a user profile to a user profile dto.
     * 
     * @param userProfile
     * @return 
     */
    private UserProfileDto convertToUserProfileDto(UserProfile userProfile) {
        
        UserProfileDto dto = new UserProfileDto();
        dto.setUserId(userProfile.getUserId());
        dto.setGovernmentId(userProfile.getGovernmentId());
        dto.setExternalId(userProfile.getExternalId());
        dto.setHand(userProfile.getHand().name());
        dto.setTeam(userProfile.getTeam());
        dto.setDateBirth(userProfile.getDateBirth());
        dto.setAge(userProfile.getAge());
        dto.setPhone(userProfile.getPhone());
        dto.setGenre(userProfile.getGenre().name());
        dto.setWeight(userProfile.getWeight());
        dto.setHeight(userProfile.getHeight());
        dto.setCountry(userProfile.getCountry());
        dto.setState(userProfile.getState());
        dto.setCity(userProfile.getCity());
        dto.setStreet1(userProfile.getStreet1());
        dto.setStreet2(userProfile.getStreet2());
        dto.setEmergencyContactName(userProfile.getEmergencyContactName());
        dto.setEmergencyContactPhone(userProfile.getEmergencyContactPhone());
        dto.setShirtSize(userProfile.getShirtSize().name());
        dto.setCategory(userProfile.getCategory().getName());
        dto.setRole(userProfile.getRole().getName());
        
        return dto;
    }

}
