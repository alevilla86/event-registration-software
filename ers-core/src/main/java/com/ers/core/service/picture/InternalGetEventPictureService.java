/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service.picture;

import com.ers.core.exception.ErsException;
import com.ers.core.orm.Picture;
import org.springframework.stereotype.Service;

/**
 *
 * @author avillalobos
 */
@Service
class InternalGetEventPictureService extends InternalGetPictureService {

    @Override
    protected boolean hasOriginalPicture(String eventId) throws ErsException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Picture getPicture(String eventId, int desiredWidth, int desiredHeight) throws ErsException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected Picture getOriginalPicture(String eventId) throws ErsException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void saveResizedImage(String eventId, byte[] resizedImage, int newWidth, int newHeight) throws ErsException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
