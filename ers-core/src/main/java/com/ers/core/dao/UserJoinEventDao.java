/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.UserJoinEvent;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author avillalobos
 */
@Repository
public class UserJoinEventDao extends BaseDao {
    
    /**
     * Saves an UserJoinEvent to the database.
     * 
     * @param userRegistration
     * @return
     * @throws ErsException 
     */
    public UserJoinEvent save(UserJoinEvent userRegistration) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.save(userRegistration);
            
            return userRegistration;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Updates an UserJoinEvent to the database.
     * 
     * @param userRegistration
     * @return
     * @throws ErsException 
     */
    public UserJoinEvent update(UserJoinEvent userRegistration) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.update(userRegistration);
            
            return userRegistration;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }

}
