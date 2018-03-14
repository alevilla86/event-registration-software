/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.dao;

import com.ers.core.exception.DatabaseException;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Category;
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
public class CategoryDao extends BaseDao {
    
    /**
     * Gets a category from the database by its name.
     * 
     * @param name
     * @return
     * @throws ErsException 
     */
    public Category getByName(String name) throws ErsException {
        
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<Category> criteriaQuery = builder.createQuery(Category.class);
            
            Root<Category> category = 
                    criteriaQuery.from(Category.class);
                    criteriaQuery.where(builder.equal(category.get("name"), name));
            
            Category result = (Category) session.createQuery(criteriaQuery).uniqueResult();
            
            return result;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }
    
    /**
     * Gets all categories from the database.
     * 
     * @return
     * @throws ErsException 
     */
    public List<Category> getByName() throws ErsException {
        
        try {
            
            Session session = getSession();
            
            CriteriaBuilder builder = session.getCriteriaBuilder();
            
            CriteriaQuery<Category> criteriaQuery = builder.createQuery(Category.class);
            
            Root<Category> category = 
                    criteriaQuery.from(Category.class);
                    criteriaQuery.distinct(true);
                    criteriaQuery.orderBy(builder.asc(category.get("id")));
            
            List<Category> result = (List<Category>) session.createQuery(criteriaQuery).list();
            
            return result;
            
        } catch (HibernateException ex) {
            throw new DatabaseException(ex);
        }
    }

}
