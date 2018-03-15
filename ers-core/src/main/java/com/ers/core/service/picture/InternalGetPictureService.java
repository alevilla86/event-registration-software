/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service.picture;

import com.ers.core.exception.ErsException;
import com.ers.core.orm.Picture;
import com.ers.core.orm.User;
import com.ers.core.util.ImageUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Attempts to get a Picture of the desired dimensions and if not exists with the
 * specified dimensions it will create one in the database.
 * 
 * @see com.ers.core.orm.Picture
 * @author avillalobos
 */
@Component
abstract class InternalGetPictureService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(InternalGetPictureService.class);
    
    /**
     * Makes some validations and at runtime one of its implementations knows how
     * to retrieve the Picture from the database.
     * 
     * @param loggedUser
     * @param entityId
     * @param width
     * @param height
     * @return
     * @throws ErsException 
     */
    byte[] retrievePicture(User loggedUser, String entityId, int width, int height) throws ErsException {
        
        //Validate the dimensions are within the configured limits.
        ImageUtils.validateDimentions(width, height);
        
        //Clean picture sizes.
        PictureSizeCleaner cleaner = new PictureSizeCleaner(width, height);
        int cleanWidth = cleaner.getCleanWidth();
        int cleanHeight = cleaner.getCleanHeight();
        
        if (!hasOriginalPicture(entityId)) {
            LOGGER.info("Entity with {} ID has no picture", entityId);
            return null;
        }
        
        // Look for the picture with the size wanted by the client.
        Picture picture = getPicture(entityId, width, height);
        
        if (picture != null) {
            //Found!.
            return picture.getPicture();
        }
        
        // Picture with the exact size wasn't found.
        // We need to resize the original picture.
        Picture original = getOriginalPicture(entityId);
        if (original == null) {
            // Something is wrong
            LOGGER.error("The original picture with {} ID is not found", entityId);
            return null;
        }
        
        byte[] resizedImage = ImageUtils.resizeImageToPng(original.getPicture(), cleanWidth, cleanHeight);
        
        saveResizedImage(entityId, resizedImage, width, cleanHeight);
        
        return resizedImage;
    }
    
    protected abstract boolean hasOriginalPicture(String entityId) throws ErsException;
    
    protected abstract Picture getPicture(String entityId, int desiredWidth, int desiredHeight) throws ErsException;
    
    protected abstract Picture getOriginalPicture(String entityId) throws ErsException;
    
    protected abstract void saveResizedImage(String entityId, byte[] resizedImage, int newWidth, int newHeight) throws ErsException;

}
