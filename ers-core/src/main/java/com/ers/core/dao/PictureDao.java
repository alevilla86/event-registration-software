package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.orm.Picture;

/**
 * Dao to deal with pictures.
 * 
 * @author avillalobos
 */
public interface PictureDao {
    
    /**
     * Gets the original picture from the database.
     * 
     * @param entityId
     * @return
     * @throws DatabaseException 
     */
    public Picture getOriginalPicture(String entityId) throws DatabaseException;

    /**
     * Gets a resized picture.
     * 
     * @param entityId
     * @param width
     * @param height
     * @return
     * @throws DatabaseException 
     */
    public Picture get(String entityId, int width, int height) throws DatabaseException;

    /**
     * Deletes all entity pictures.
     * 
     * @param entityId
     * @return
     * @throws DatabaseException 
     */
    public boolean deletePictures(String entityId) throws DatabaseException;

    /**
     * Saves an entity picture to the database.
     * 
     * @param entityPicture
     * @return
     * @throws DatabaseException 
     */
    //public Picture save(Picture entityPicture) throws DatabaseException;
    
    /**
     * Saves a picture to the database.
     * 
     * @param entityId
     * @param resizedImage
     * @param newWidth
     * @param newHeight
     * @return
     * @throws DatabaseException 
     */
    public Picture save(String entityId, byte[] resizedImage, int newWidth, int newHeight) throws DatabaseException;

    /**
     * Returns true if the entity has the original uploaded picture.
     * 
     * @param entityId
     * @return
     * @throws DatabaseException 
     */
    public boolean hasOriginalPicture(String entityId) throws DatabaseException;
    
}
