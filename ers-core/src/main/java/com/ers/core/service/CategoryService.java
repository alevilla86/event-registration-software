/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service;

import com.ers.core.dao.CategoryDao;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Category;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author avillalobos
 */
@Service
public class CategoryService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);
    
    @Autowired
    private CategoryDao categoryDao;
    
    /**
     * Gets a Category from the database.
     * If not exists throws an exception.
     * 
     * @param name
     * @return
     * @throws ErsException 
     */
    protected Category getByName(String name) throws ErsException {
        
        if (StringUtils.isBlank(name)) {
            throw new ErsException("Category name is missing to retrieve from db", ErsErrorCode.PARAMETER_MISSING);
        }
        
        Category category = categoryDao.getByName(name);
        
        if (category == null) {
            throw new ErsException("The category not exists", ErsErrorCode.TARGET_NOT_EXISTS);
        }
        
        LOGGER.debug("Found cateogry [role={}]", name);
        
        return category;
    }
}
