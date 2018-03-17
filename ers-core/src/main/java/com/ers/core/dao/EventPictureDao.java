/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.orm.EventPicture;
import com.ers.core.orm.Picture;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author avillalobos
 */
@Repository
public class EventPictureDao extends BaseDao implements PictureDao {
    
    /**
     * Gets the original picture from the database.
     * 
     * @param eventId
     * @return
     * @throws DatabaseException 
     */
    @Override
    public EventPicture getOriginalPicture(String eventId) throws DatabaseException {
        // The original picture of an event is identified by width == height == 0.
        return get(eventId, 0, 0);
    }

    /**
     * Gets a resized picture.
     * 
     * @param eventId
     * @param width
     * @param height
     * @return
     * @throws DatabaseException 
     */
    @Override
    public EventPicture get(String eventId, int width, int height) throws DatabaseException {
        try {
            
            Criteria criteria = getSession().createCriteria(EventPicture.class) //
                    .add(Restrictions.eq("eventId", eventId)) //
                    .add(Restrictions.eq("width", width)) //
                    .add(Restrictions.eq("height", height));

            EventPicture result = (EventPicture) criteria.uniqueResult();

            return result;
        } catch (HibernateException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Deletes all user event pictures.
     * 
     * @param eventId
     * @return
     * @throws DatabaseException 
     */
    @Override
    public boolean deletePictures(String eventId) throws DatabaseException {
        String hql = "DELETE " + EventPicture.class.getName() + " WHERE eventId = :eventId";

        try {
            Query query = getSession().createQuery(hql).setParameter("eventId", eventId);
            int count = query.executeUpdate();

            return count != 0;

        } catch (HibernateException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Saves an event picture to the database.
     * 
     * @param eventPicture
     * @return
     * @throws DatabaseException 
     */
    public EventPicture save(EventPicture eventPicture) throws DatabaseException {
        
        try {
            
            Session session = getSession();
            EventPicture result = (EventPicture) session.merge(eventPicture);

            return result;

        } catch (HibernateException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Returns true if the event has the original uploaded picture.
     * 
     * @param eventId
     * @return
     * @throws DatabaseException 
     */
    @Override
    public boolean hasOriginalPicture(String eventId) throws DatabaseException {

        // The original picture of a user profile is identified by width == height == 0.
        try {
            
            Criteria criteria = getSession().createCriteria(EventPicture.class, "picture") //
                    .add(Restrictions.eq("picture.eventId", eventId)) //
                    .add(Restrictions.eq("picture.width", 0)) //
                    .add(Restrictions.eq("picture.height", 0)) // 
                    .setProjection(Projections.rowCount());

            Number count = (Number) criteria.uniqueResult();

            return count.intValue() != 0;

        } catch (HibernateException e) {
            throw new DatabaseException(e);
        }
    }

    @Override
    public Picture save(String eventId, byte[] resizedImage, int newWidth, int newHeight) throws DatabaseException {
        EventPicture picture = new EventPicture(eventId, resizedImage, newWidth, newHeight);
        return save(picture);
    }

}
