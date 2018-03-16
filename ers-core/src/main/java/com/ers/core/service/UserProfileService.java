/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service;

import com.ers.core.dao.UserProfileDao;
import com.ers.core.dao.UserProfilePictureDao;
import com.ers.core.dto.UserProfileDto;
import com.ers.core.exception.ErsAccessDeniedException;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Category;
import com.ers.core.orm.Role;
import com.ers.core.orm.User;
import com.ers.core.orm.UserProfile;
import com.ers.core.orm.UserProfilePicture;
import com.ers.core.util.EntityValidatorUtil;
import com.ers.core.util.ImageUtils;
import com.mchange.io.FileUtils;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
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
    
    @Autowired
    private UserProfilePictureDao userProfilePictureDao;
    
    @Autowired
    private EntityValidatorUtil validator;
    
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
        
        /*
        Not all user types requires a ROLE and a CATEGORY.
        */
        if (!StringUtils.isBlank(updatedProfile.getRole())) {
            Role role = roleService.getByName(updatedProfile.getRole());
            existingProfile.setRole(role);
        } else {
            existingProfile.setRole(null);
        }
        
        if (!StringUtils.isBlank(updatedProfile.getCategory())) {
            Category category = categoryService.getByName(updatedProfile.getCategory());
            existingProfile.setCategory(category);
        } else {
            existingProfile.setCategory(null);
        }
        
        validator.validateUserProfileFields(existingProfile, loggedUser.getType());
        
        userProfileDao.update(existingProfile);
        
        LOGGER.info("User profile updated [userProfileId={}, profile={}]", userProfileId, existingProfile);
        
        return convertToUserProfileDto(existingProfile);
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
        dto.setHand(userProfile.getHand() != null ? userProfile.getHand().name() : null);
        dto.setTeam(userProfile.getTeam());
        dto.setDateBirth(userProfile.getDateBirth());
        dto.setAge(userProfile.getAge());
        dto.setPhone(userProfile.getPhone());
        dto.setGenre(userProfile.getGenre() != null ? userProfile.getGenre().name() : null);
        dto.setWeight(userProfile.getWeight());
        dto.setHeight(userProfile.getHeight());
        dto.setCountry(userProfile.getCountry());
        dto.setState(userProfile.getState());
        dto.setCity(userProfile.getCity());
        dto.setStreet1(userProfile.getStreet1());
        dto.setStreet2(userProfile.getStreet2());
        dto.setEmergencyContactName(userProfile.getEmergencyContactName());
        dto.setEmergencyContactPhone(userProfile.getEmergencyContactPhone());
        dto.setShirtSize(userProfile.getShirtSize() != null ? userProfile.getShirtSize().name() : null);
        dto.setCategory(userProfile.getCategory() != null ? userProfile.getCategory().getName() : null);
        dto.setRole(userProfile.getRole() != null ? userProfile.getRole().getName() : null);
        
        return dto;
    }
    
    /**
     * Uploads an user profile picture.
     * 
     * @param loggedUser
     * @param pictureFileName
     * @param dataFile
     * @param httpRequest
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserProfilePicture(User loggedUser, String pictureFileName, File dataFile, HttpServletRequest httpRequest) throws ErsException {

        String userId = loggedUser.getId();
        
        UserProfile existingProfile = userProfileDao.getById(userId);

        if (existingProfile == null) {
            throw new ErsException("User profile not found", ErsErrorCode.USER_PROFILE_NOT_FOUND);
        }

        validator.validatePicture(pictureFileName, dataFile);

        // Delete all the other old pictures
        userProfilePictureDao.deletePictures(userId);

        byte[] picture;
        try {
            picture = FileUtils.getBytes(dataFile);
        } catch (IOException ex) {

            throw new ErsException("Failed to process the picture", ErsErrorCode.GENERAL, ex);
        }

        // Convert to PNG keeping the original size.
        // This may be a "waste" if the picture is already in PNG format.
        byte[] pngPicture = ImageUtils.resizeImageToPng(picture, -1, -1);

        // Save the new picture: this is the original picture because is saved with width == height == 0
        UserProfilePicture userProfilePicture = UserProfilePicture.newOriginalPicture(userId, pngPicture);
        userProfilePictureDao.save(userProfilePicture);
    }
    
    /**
     * Deletes all user profile pictures.
     * 
     * @param userId
     * @param loggedUser
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserProfilePicture(User loggedUser, String userId) throws ErsException {
        
        UserProfile existingProfile = userProfileDao.getById(userId);

        if (existingProfile == null) {
            throw new ErsException("User profile not found", ErsErrorCode.USER_PROFILE_NOT_FOUND);
        }

        if (!StringUtils.equals(existingProfile.getUser().getId(), loggedUser.getId())) {
            throw new ErsAccessDeniedException("You are not authorized to delete this profile picture");
        }

        userProfilePictureDao.deletePictures(userId);
    }
    
    @Transactional(readOnly = true)
    public boolean hasUserProfilePicture(String profileId) throws ErsException {
        boolean result = userProfilePictureDao.hasOriginalPicture(profileId);
        return result;
    }

}
