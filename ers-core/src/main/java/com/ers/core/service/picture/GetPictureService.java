/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service.picture;

import com.ers.core.dao.PictureDao;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service has a suggestive name: deals with one method.
 * It's purpose is to deal with a race condition: multiple threads retrieving the same picture at the same time that is not yet saved in DB with the indicated dimensions.
 * Only one of the treads will be able to insert in DB the newly scaled picture, the others will fail generating a DataIntegrityViolationException.
 * This service catches the specific exception then making the call again.
 * 
 * @author avillalobos
 */
@Service
public class GetPictureService {
    
    public enum PictureServiceType {
        USER_PROFILE,
        EVENT
    }
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GetPictureService.class);
    
    @Autowired
    private PictureDaoFactory factory;
    
    @Autowired
    private InternalGetPictureService internalGetPictureService;
    
    /**
     * 
     * @param entityId the entity to which the picture belongs. (profilePicture, eventPicture...)
     * @param width desired width.
     * @param height desired height.
     * @param loggedUser
     * @param pictureServiceType
     * @return
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public byte[] getPicture(User loggedUser, String entityId, int width, int height, PictureServiceType pictureServiceType) throws ErsException {
        
        //Decide what DAO to use.
        PictureDao dao = factory.getPictureService(pictureServiceType);
        internalGetPictureService.setPictureDao(dao);
        
        byte[] result; 
        
        try {
            
            result = internalGetPictureService.retrievePicture(loggedUser, entityId, width, height);
            
        } catch (DataIntegrityViolationException ex) {
            
            LOGGER.info("Got DataIntegrityViolationException. Calling again.", ex);

            // Another thread was faster and inserted the scaled picture.
            // Call again: this call shouldn't get the exception.

            result = internalGetPictureService.retrievePicture(loggedUser, entityId, width, height);
        }
        
        return result;
    }

}
