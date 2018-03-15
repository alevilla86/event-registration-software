/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.rest.controller;

import com.ers.core.constants.FileConstants;
import com.ers.core.dto.UserProfileDto;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.User;
import com.ers.core.service.UserProfileService;
import com.ers.core.service.picture.GetPictureService;
import com.ers.core.service.picture.GetPictureService.PictureServiceType;
import com.ers.core.util.ErsFileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author avillalobos
 */
@RestController
public class UserProfileController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserProfileController.class);
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Autowired
    private GetPictureService getPictureService;
    
    @Autowired
    private ErsFileUtils ersFileUtils;
    
    @GetMapping("/user-profile")
    public UserProfileDto getUserProfile() throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("User is retrieving his user profile [user={}]", loggedUser.getEmail());

        UserProfileDto profile = userProfileService.getUserProfile(loggedUser);

        return profile;
    }
    
    @PutMapping("/user-profile")
    public UserProfileDto updateUserProfile(@RequestBody UserProfileDto profileDto) throws ErsException {
        
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("User is updating his user profile [user={}]", loggedUser.getEmail());

        UserProfileDto profile = userProfileService.updateUserProfile(loggedUser, loggedUser.getId(), profileDto);

        return profile;
        
    }
    
    @PostMapping("/user-profile/picture")
    public boolean uploadUserProfilePicture( //
            HttpServletRequest httpRequest, //
            @RequestParam("file") MultipartFile multipartFile //
    ) throws ErsException, IOException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("{} is uploading user profile picture", loggedUser.getEmail());

        String origFileName = multipartFile.getOriginalFilename();

        // Transfer the multipart file to a temporary file.
        // Use a random name because other files with the same name could be uploaded into different folders at the same time.
        File dataFile = ersFileUtils.createTempFile();
        multipartFile.transferTo(dataFile);

        try {
            userProfileService.updateUserProfilePicture(loggedUser, origFileName, dataFile, httpRequest);
        } finally {
            ErsFileUtils.deleteQuietly(dataFile);
        }

        return true;
    }
    
    @DeleteMapping("/user-profile/picture")
    public boolean deleteUserProfilePicture() throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("{} is deleting user profile picture", loggedUser.getEmail());

        userProfileService.deleteUserProfilePicture(loggedUser, loggedUser.getId());

        return true;
    }
    
    @GetMapping("/user-profile/{userId}/picture")
    public ResponseEntity<byte[]> downloadUserProfilePicture(
            @PathVariable("userId") String userId,
            @RequestParam(value = "width", required = false, defaultValue = "0") int width,
            @RequestParam(value = "height", required = false, defaultValue = "0") int height
    ) throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("{} is getting {} user profile picture, width={}, height={}", loggedUser.getEmail(), userId, width, height);

        byte[] picture = getPictureService.getPicture(loggedUser, userId, width, height, PictureServiceType.USER_PROFILE);

        HttpHeaders responseHeaders = new HttpHeaders();

        // The profile picture is kept in PNG format.
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, FileConstants.PNG_MIME_TYPE);

        return new ResponseEntity<>(picture, responseHeaders, HttpStatus.OK);
    }

    @GetMapping(value = "/user-profile/{userId}/picture/base64")
    public String downloadUserProfilePictureBase64(
            @PathVariable("userId") String userId,
            @RequestParam(value = "width", required = false, defaultValue = "0") int width,
            @RequestParam(value = "height", required = false, defaultValue = "0") int height
    ) throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("{} is getting {} user profile picture base64, width={}, height={}", loggedUser.getEmail(), userId, width, height);

        byte[] picture = getPictureService.getPicture(loggedUser, userId, width, height, PictureServiceType.USER_PROFILE);

        if (picture == null) {
            return null;
        }

        return Base64.getEncoder().encodeToString(picture);
    }

    @GetMapping(value = "/user-profile/{userId}/has_picture")
    public boolean hasUserProfilePicture(@PathVariable("userId") String userId) throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("{} is verifying if {} user profile has a picture", loggedUser.getEmail(), userId);

        boolean result = userProfileService.hasUserProfilePicture(userId);

        return result;
    }

}
