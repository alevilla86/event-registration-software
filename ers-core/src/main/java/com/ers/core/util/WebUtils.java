/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.util;

import com.ers.core.dao.constants.SecurityConstants;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author avillalobos
 */
public class WebUtils {
    
    /**
     * Gets the client ip address from the request.
     * 
     * @param request
     * @return 
     */
    public static String getClientIpAddress(HttpServletRequest request) {
        // Since we have a load balancer (barracuda) in front of the services we need to get the client IP address from this header.
        String ipAddress = request.getHeader(SecurityConstants.X_FORWARDED_FOR_HEADER_NAME);
        if (StringUtils.isBlank(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }

}
