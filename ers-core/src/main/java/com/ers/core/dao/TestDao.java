/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.dao;

import org.springframework.stereotype.Repository;

/**
 *
 * @author avillalobos
 */
@Repository
public class TestDao {
    
    public String test() {
        return "Hello Spring boot!";
    }

}
