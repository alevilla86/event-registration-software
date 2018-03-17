/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Event;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author avillalobos
 */
@Repository
public class EventDao extends BaseDao {
    
    /**
     * Saves an event to the database.
     * 
     * @param event
     * @return
     * @throws ErsException 
     */
    public Event save(Event event) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.save(event);
            
            return event;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Updates an event to the database.
     * 
     * @param event
     * @return
     * @throws ErsException 
     */
    public Event update(Event event) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.update(event);
            
            return event;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Gets an event by its id.
     * 
     * @param eventId
     * @return
     * @throws ErsException 
     */
    public Event getById(String eventId) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<Event> query = builder.createQuery(Event.class);
            
            Root<Event> event = 
                    query.from(Event.class);
                    query.where(builder.equal(event.get("id"), eventId));
                    
            Event result = (Event) session.createQuery(query).uniqueResult();
            
            return result;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }

    /**
     * Deletes an event from the database.
     * 
     * @param event
     * @throws ErsException 
     */
    public void delete(Event event) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.delete(event);
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
}
