package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.UserProfile;
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
public class UserProfileDao extends BaseDao {
    
    /**
     * Gets an user profile from the database by its id.
     * 
     * @param id
     * @return
     * @throws ErsException 
     */
    public UserProfile getById(String id) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<UserProfile> criteriaQuery = builder.createQuery(UserProfile.class);
            
            Root<UserProfile> userProfile = 
                    criteriaQuery.from(UserProfile.class);
                    criteriaQuery.where(builder.equal(userProfile.get("userId"), id));
            
            UserProfile result = (UserProfile) session.createQuery(criteriaQuery).uniqueResult();
            
            return result;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Saves an userProfile to the database.
     * 
     * @param userProfile
     * @return
     * @throws ErsException 
     */
    public UserProfile save(UserProfile userProfile) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.save(userProfile);
            
            return userProfile;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Updates an user to the database.
     * 
     * @param userProfile
     * @return
     * @throws ErsException 
     */
    public UserProfile update(UserProfile userProfile) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            session.update(userProfile);
            
            return userProfile;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }

}
