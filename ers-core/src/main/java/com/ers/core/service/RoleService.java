/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service;

import com.ers.core.dao.RoleDao;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Role;
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
public class RoleService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleService.class);
    
    @Autowired
    private RoleDao roleDao;
    
    /**
     * Gets a Role from the database.
     * If not exists throws an exception.
     * 
     * @param name
     * @return
     * @throws ErsException 
     */
    protected Role getByName(String name) throws ErsException {
        
        if (StringUtils.isBlank(name)) {
            throw new ErsException("Role name is missing to retrieve from db", ErsErrorCode.PARAMETER_MISSING);
        }
        
        Role role = roleDao.getByName(name);
        
        if (role == null) {
            throw new ErsException("The role not exists", ErsErrorCode.TARGET_NOT_EXISTS);
        }
        
        LOGGER.debug("Found role [role={}]", name);
        
        return role;
    }

}
