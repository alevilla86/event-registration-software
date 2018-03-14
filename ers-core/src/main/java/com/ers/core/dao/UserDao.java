package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.User;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 * Executes querys against ers_user table.
 * 
 * @author avillalobos
 */
@Repository
public class UserDao extends BaseDao {
    
    /**
     * Gets an user from the database by its id.
     * 
     * @param id
     * @return
     * @throws ErsException 
     */
    public User getById(String id) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            
            Root<User> user = 
                    criteriaQuery.from(User.class);
                    criteriaQuery.where(builder.equal(user.get("id"), id));
            
            User result = (User) session.createQuery(criteriaQuery).uniqueResult();
            
            return result;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Gets an user from the database by its email.
     * 
     * @param email
     * @return
     * @throws ErsException 
     */
    public User getByEmail(String email) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            
            Root<User> user = 
                    criteriaQuery.from(User.class);
                    criteriaQuery.where(builder.equal(user.get("email"), email));
            
            User result = (User) session.createQuery(criteriaQuery).uniqueResult();
            
            return result;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Saves an user to the database.
     * 
     * @param user
     * @return
     * @throws ErsException 
     */
    public User save(User user) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.save(user);
            
            return user;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Updates an user to the database.
     * 
     * @param user
     * @return
     * @throws ErsException 
     */
    public User update(User user) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.update(user);
            
            return user;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Deletes an user from the database.
     * 
     * @param user
     * @throws ErsException 
     */
    public void delete(User user) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.delete(user);
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }

}
