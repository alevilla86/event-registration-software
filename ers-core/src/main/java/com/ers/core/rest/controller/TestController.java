/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author avillalobos
 */
@RestController
public class TestController {
    
    @GetMapping(value = "/tests")
    public boolean test() {
        
        return true;
    }

}
