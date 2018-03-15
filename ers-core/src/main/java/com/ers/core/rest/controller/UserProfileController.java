package com.ers.core.rest.controller;

import com.ers.core.dto.UserProfileDto;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.User;
import com.ers.core.service.UserProfileService;
import com.ers.core.util.ErsFileUtils;
import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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

}
