/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service.picture;

import com.ers.core.service.picture.GetPictureService.PictureServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Factory class for InternalGetPictureService.
 * 
 * @author avillalobos
 */
@Component
class InternalGetPictureServiceFactory {
    
    @Autowired
    private InternalGetUserProfilePictureService userProfilePictureService;
    
    @Autowired
    private InternalGetEventPictureService eventPictureService;
    
    /**
     * Factory method to return the correct instance.
     * 
     * @param type
     * @return 
     */
    InternalGetPictureService getPictureService(PictureServiceType type) {
        
        switch (type) {
            
            case USER_PROFILE:
                return userProfilePictureService;
                
            case EVENT:
                return eventPictureService;
        }
        
        return null;
    }

}
