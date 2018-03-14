package com.ers.core.service;

import com.ers.core.dao.TestDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author avillalobos
 */
@Service
public class TestService {
    
    @Autowired
    private TestDao testDao;
    
    public String test() {
        return testDao.test();
    }

}
