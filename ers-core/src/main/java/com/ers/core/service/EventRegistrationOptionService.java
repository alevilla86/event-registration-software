/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service;

import com.ers.core.dao.EventRegistrationOptionDao;
import com.ers.core.dto.EventRegistrationOptionDto;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.EventRegistrationOption;
import com.ers.core.util.EntityValidatorUtil;
import java.util.Collections;
import java.util.List;
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
public class EventRegistrationOptionService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EventRegistrationOptionService.class);

    @Autowired
    private EventRegistrationOptionDao optionDao;
    
    @Autowired
    private EntityValidatorUtil validator;
    
    /**
     * Gets an EventRegistrationOption by its id.
     * 
     * @param optionId
     * @return
     * @throws ErsException 
     */
    protected EventRegistrationOption getEventRegistrationOptionById(String optionId) throws ErsException {
        
        if (StringUtils.isBlank(optionId)) {
            throw new ErsException("Option id is required to retrieve the registration option", ErsErrorCode.PARAMETER_MISSING);
        }
        
        EventRegistrationOption option = optionDao.getById(optionId);
        
        if (option == null) {
            throw new ErsException("Registration option not exists", ErsErrorCode.NOT_FOUND_EVENT_OPTION);
        }
        
        LOGGER.debug("Found EventRegistrationOption {}", option.getId());
        
        return option;
    }
    
    /**
     * Saves an event registration option to the database.
     * Validates the option before saving it.
     * 
     * @param option
     * @return
     * @throws ErsException 
     */
    protected EventRegistrationOption saveEventRegistrationOption(EventRegistrationOption option) throws ErsException {
        
        validator.validateEventRegistrationOption(option);
        
        option = optionDao.save(option);
        
        LOGGER.debug("EventRegistrationOption was saved to the database [optionId={}]", option.getId());
        
        return option;
    }
    
    /**
     * Updates an event registration option to the database.
     * Validates the option before saving it.
     * 
     * @param option
     * @return
     * @throws ErsException 
     */
    protected EventRegistrationOption updateEventRegistrationOption(EventRegistrationOption option) throws ErsException {
        
        validator.validateEventRegistrationOption(option);
        
        option = optionDao.update(option);
        
        LOGGER.debug("EventRegistrationOption was updated to the database [optionId={}]", option.getId());
        
        return option;
    }
    
    /**
     * Returns the list of event registration options for the event id.
     * 
     * @param eventId
     * @return
     * @throws ErsException 
     */
    protected List<EventRegistrationOption> getEventRegistrationOptionsByEventId(String eventId) throws ErsException {
        
        if (StringUtils.isBlank(eventId)) {
            throw new ErsException("The eventId is required to get the list of registration options", ErsErrorCode.PARAMETER_MISSING);
        }
        
        List<EventRegistrationOption> options = optionDao.getByEventId(eventId);
        
        if (options == null || options.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        
        LOGGER.debug("A total of {} registration options were found for the event {}", options.size(), eventId);
        
        return options;
        
    }
    
    /**
     * Deletes an event registration option.
     * 
     * @param option
     * @throws ErsException 
     */
    protected void deleteEventRegistrationOption(EventRegistrationOption option) throws ErsException {
        
        if (option == null) {
            LOGGER.warn("Tryng to delete a registration option passing a null argument");
            return;
        }
        
        optionDao.delete(option);
        
        LOGGER.debug("EventRegistrationOption {} was deleted", option.getId());
    }
    
    /**
     * Converts a the EventRegistrationOption to its DTO.
     * 
     * @param option
     * @return 
     */
    protected EventRegistrationOptionDto convertEventToEventRegistrationOptionDto(EventRegistrationOption option) {
        
        EventRegistrationOptionDto dto = new EventRegistrationOptionDto();
        dto.setId(option.getId());
        dto.setName(option.getName());
        dto.setDescription(option.getDescription());
        dto.setRegistrationCost(option.getRegistrationCost());
        dto.setRegistrationCostCurrency(option.getRegistrationCostCurrency().name());
        
        return dto;
    }
    
}
