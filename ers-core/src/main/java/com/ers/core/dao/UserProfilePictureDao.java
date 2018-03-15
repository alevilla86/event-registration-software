package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.orm.UserProfilePicture;
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
public class UserProfilePictureDao extends BaseDao {
    
    /**
     * Gets the original picture from the database.
     * 
     * @param userId
     * @return
     * @throws DatabaseException 
     */
    public UserProfilePicture getOriginalPicture(String userId) throws DatabaseException {
        // The original picture of a user profile is identified by width == height == 0.
        return get(userId, 0, 0);
    }

    /**
     * Gets a resized picture.
     * 
     * @param userId
     * @param width
     * @param height
     * @return
     * @throws DatabaseException 
     */
    public UserProfilePicture get(String userId, int width, int height) throws DatabaseException {
        try {
            
            Criteria criteria = getSession().createCriteria(UserProfilePicture.class) //
                    .add(Restrictions.eq("userId", userId)) //
                    .add(Restrictions.eq("width", width)) //
                    .add(Restrictions.eq("height", height));

            UserProfilePicture result = (UserProfilePicture) criteria.uniqueResult();

            return result;
        } catch (HibernateException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Deletes all user profile pictures.
     * 
     * @param userId
     * @return
     * @throws DatabaseException 
     */
    public boolean deleteUserProfilePictures(String userId) throws DatabaseException {
        String hql = "DELETE " + UserProfilePicture.class.getName() + " WHERE userId = :userId";

        try {
            Query query = getSession().createQuery(hql).setParameter("userId", userId);
            int count = query.executeUpdate();

            return count != 0;

        } catch (HibernateException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Saves an user profile picture to the database.
     * 
     * @param userProfilePicture
     * @return
     * @throws DatabaseException 
     */
    public UserProfilePicture save(UserProfilePicture userProfilePicture) throws DatabaseException {
        
        try {
            
            Session session = getSession();
            UserProfilePicture result = (UserProfilePicture) session.merge(userProfilePicture);

            return result;

        } catch (HibernateException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Returns true if the user has the original uploaded picture.
     * 
     * @param userId
     * @return
     * @throws DatabaseException 
     */
    public boolean hasOriginalPicture(String userId) throws DatabaseException {

        // The original picture of a user profile is identified by width == height == 0.
        try {
            
            Criteria criteria = getSession().createCriteria(UserProfilePicture.class, "picture") //
                    .add(Restrictions.eq("picture.userId", userId)) //
                    .add(Restrictions.eq("picture.width", 0)) //
                    .add(Restrictions.eq("picture.height", 0)) // 
                    .setProjection(Projections.rowCount());

            Number count = (Number) criteria.uniqueResult();

            return count.intValue() != 0;

        } catch (HibernateException e) {
            throw new DatabaseException(e);
        }
    }

}
