/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Event;
import com.ers.core.orm.EventRegistrationOption;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author avillalobos
 */
@Repository
public class EventRegistrationOptionDao extends BaseDao {
    
    /**
     * Saves an EventRegistrationOption to the database.
     * 
     * @param option
     * @return
     * @throws ErsException 
     */
    public EventRegistrationOption save(EventRegistrationOption option) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.save(option);
            
            return option;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Updates an EventRegistrationOption to the database.
     * 
     * @param option
     * @return
     * @throws ErsException 
     */
    public EventRegistrationOption update(EventRegistrationOption option) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.update(option);
            
            return option;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Gets an EventRegistrationOption by its id.
     * 
     * @param optionId
     * @return
     * @throws ErsException 
     */
    public EventRegistrationOption getById(String optionId) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<EventRegistrationOption> query = builder.createQuery(EventRegistrationOption.class);
            
            Root<EventRegistrationOption> option = 
                    query.from(EventRegistrationOption.class);
                    query.where(builder.equal(option.get("id"), optionId));
                    
            EventRegistrationOption result = (EventRegistrationOption) session.createQuery(query).uniqueResult();
            
            return result;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Gets a list of EventRegistrationOption by event.
     * 
     * @param eventId
     * @return
     * @throws ErsException 
     */
    public List<EventRegistrationOption> getByEventId(String eventId) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<EventRegistrationOption> query = builder.createQuery(EventRegistrationOption.class);
            
            Root<EventRegistrationOption> option =  query.from(EventRegistrationOption.class);
            Join<EventRegistrationOption, Event> event = option.join("event", JoinType.LEFT);
            
            query.where(builder.equal(event.get("id"), eventId));
            query.orderBy(builder.asc(option.get("name")));
            query.distinct(true);

            List<EventRegistrationOption> result = (List<EventRegistrationOption>) session.createQuery(query).list();
            
            return result;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Deletes an EventRegistrationOption from the database.
     * 
     * @param option
     * @throws ErsException 
     */
    public void delete(EventRegistrationOption option) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.delete(option);
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }

}
