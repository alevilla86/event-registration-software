/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service.picture;

import com.ers.core.dao.UserProfilePictureDao;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Picture;
import com.ers.core.orm.UserProfilePicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class to retrieve user profile pictures.
 * 
 * @author avillalobos
 */
@Component
class InternalGetUserProfilePictureService extends InternalGetPictureService {
    
    @Autowired
    private UserProfilePictureDao userProfilePictureDao;

    @Override
    public Picture getPicture(String userId, int desiredWidth, int desiredHeight) throws ErsException {
        
        return userProfilePictureDao.get(userId, desiredWidth, desiredHeight);
    }

    @Override
    protected boolean hasOriginalPicture(String userId) throws ErsException {
        
        return userProfilePictureDao.hasOriginalPicture(userId);
    }

    @Override
    protected Picture getOriginalPicture(String userId) throws ErsException {
        
        return userProfilePictureDao.getOriginalPicture(userId);
    }

    @Override
    protected void saveResizedImage(String userId, byte[] resizedImage, int newWidth, int newHeight) throws ErsException {
        
        UserProfilePicture userProfilePicture = new UserProfilePicture(userId, resizedImage, newWidth, newHeight);
        userProfilePictureDao.save(userProfilePicture);
    }

}
