package com.ers.core.dao;

import javax.persistence.EntityManagerFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * SessionFactory transaction support is configured in com.ers.core.HibernateConfiguration.
 * This class provides hibernate Sessions for all Dao classes.
 * 
 * @author avillalobos
 */
@Component
public abstract class BaseDao {
    
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    protected Session getSession() {
        
        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
        
        return sessionFactory.getCurrentSession();
    }
    
}
