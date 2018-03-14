/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.rest.controller;

import com.ers.core.orm.User;
import com.ers.core.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author avillalobos
 */
@RestController
public class TestController {
    
    private final static Logger log = LoggerFactory.getLogger(TestController.class);
    
    @Autowired
    private TestService testService;
    
    @GetMapping(value = "/tests")
    public String test() {
        
        return testService.test();
    }
    
    @GetMapping(value = "tests/hello")
    public String testHello() {
        return "hello with path";
    }
    
    @GetMapping(value = "secured")
    public String testSecure() {
        
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("Logged user is {}", loggedUser.getId());
        
        return "this shuold be secured.";
    }

}
