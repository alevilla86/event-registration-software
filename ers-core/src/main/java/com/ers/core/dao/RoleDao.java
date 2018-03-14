package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Role;
import java.util.List;
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
public class RoleDao extends BaseDao {
    
    /**
     * Gets a role from the database by its name.
     * 
     * @param name
     * @return
     * @throws ErsException 
     */
    public Role getByName(String name) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<Role> criteriaQuery = builder.createQuery(Role.class);
            
            Root<Role> role = 
                    criteriaQuery.from(Role.class);
                    criteriaQuery.where(builder.equal(role.get("name"), name));
            
            Role result = (Role) session.createQuery(criteriaQuery).uniqueResult();
            
            return result;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Gets all roles from the database.
     * 
     * @return
     * @throws ErsException 
     */
    public List<Role> getByName() throws ErsException {
        
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<Role> criteriaQuery = builder.createQuery(Role.class);
            
            Root<Role> role = 
                    criteriaQuery.from(Role.class);
                    criteriaQuery.distinct(true);
                    criteriaQuery.orderBy(builder.asc(role.get("id")));
            
            List<Role> result = (List<Role>) session.createQuery(criteriaQuery).list();
            
            return result;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }

}
