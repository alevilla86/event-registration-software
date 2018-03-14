/*
 * Copyright (C) 2017-2018 GCF Network Accelerator, LLC. All rights reserved.
 */
package com.ers.core.util;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

@Component
public class RandomUUIDGenerator implements IdentifierGenerator {

    public RandomUUIDGenerator() {
    }
    
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        
        return UUID.randomUUID().toString();
    }
}
