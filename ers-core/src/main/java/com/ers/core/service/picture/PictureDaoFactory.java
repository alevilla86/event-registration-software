/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service.picture;

import com.ers.core.dao.EventPictureDao;
import com.ers.core.dao.PictureDao;
import com.ers.core.dao.UserProfilePictureDao;
import com.ers.core.service.picture.GetPictureService.PictureServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory class for InternalGetPictureService.
 * 
 * @author avillalobos
 */
@Component
class PictureDaoFactory {
    
    @Autowired
    private EventPictureDao eventPictureDao;
    
    @Autowired
    private UserProfilePictureDao userProfilePictureDao;
    
    /**
     * Factory method to return the correct instance.
     * 
     * @param type
     * @return 
     */
    PictureDao getPictureService(PictureServiceType type) {
        
        switch (type) {
            
            case USER_PROFILE:
                return userProfilePictureDao;
                
            case EVENT:
                return eventPictureDao;
        }
        
        return null;
    }

}
