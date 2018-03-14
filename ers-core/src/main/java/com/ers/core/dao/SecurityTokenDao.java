package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.orm.SecurityToken;
import com.ers.core.orm.User;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

/**
 *
 * @author avillalobos
 */
@Repository
public class SecurityTokenDao extends BaseDao {
    
    public SecurityToken save(SecurityToken token) throws DatabaseException {
        
        try {
            
            Session session = getSession();
            
            session.save(token);
            
            return token;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    public SecurityToken getById(String tokenId) throws DatabaseException {
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<SecurityToken> criteriaQuery = builder.createQuery(SecurityToken.class);
            
            Root<SecurityToken> token = 
                    criteriaQuery.from(SecurityToken.class);
                    token.fetch("user", JoinType.LEFT);
                    criteriaQuery.where(builder.equal(token.get("id"), tokenId));
            
            SecurityToken result = (SecurityToken) session.createQuery(criteriaQuery).uniqueResult();
            
            return result;
            
        } catch (HibernateException e) {
            throw new DatabaseException(e);
        }
    }

    /**
     * Deletes a token identified by its ID.
     * @param id
     * @return
     * @throws DatabaseException
     */
    public boolean delete(String id) throws DatabaseException {
        
        Session session = getSession();
        Query query = session.createQuery("DELETE" + SecurityToken.class.getName() + " WHERE id = :id");
        query.setParameter("id", id);

        try {
            int deletedCount = query.executeUpdate();
            return deletedCount != 0;
        } catch (HibernateException e) {
            throw new DatabaseException(e);
        }
    }

    public SecurityToken update(SecurityToken securityToken) throws DatabaseException {
        
        try {
            
            Session session = getSession();
            
            session.update(securityToken);
            
            return securityToken;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }

}
