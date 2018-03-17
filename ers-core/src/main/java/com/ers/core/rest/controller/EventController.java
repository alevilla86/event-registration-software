/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.rest.controller;

import com.ers.core.dto.EventDto;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.User;
import com.ers.core.service.EventService;
import com.ers.core.service.picture.GetPictureService;
import com.ers.core.util.ErsFileUtils;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author avillalobos
 */
@RestController
public class EventController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private GetPictureService getPictureService;
    
    @Autowired
    private ErsFileUtils ersFileUtils;
    
    @PostMapping("/events")
    public EventDto saveEvent(@RequestBody EventDto eventDto) throws ErsException {
        
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("User is saving an event [user={} eventName={}]", loggedUser.getEmail(), eventDto != null ? eventDto.getName() : null);
        
        EventDto savedEvent = eventService.saveEvent(loggedUser, eventDto);
        
        return savedEvent;
        
    }
    
    @PutMapping("/events/{eventId}")
    public EventDto updateEvent(@PathVariable("eventId") String eventId, @RequestBody EventDto eventDto) throws ErsException {
        
        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("User is updating an event [user={} eventId={}]", loggedUser.getEmail(), eventId);
        
        EventDto updatedEvent = eventService.updateEvent(loggedUser, eventId, eventDto);
        
        return updatedEvent;
        
    }
    
    @GetMapping("/events/{eventId}")
    public EventDto getEvent(@PathVariable("eventId") String eventId) throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("User is retrieving an event [user={}, eventId={}]", loggedUser.getEmail(), eventId);

        EventDto event = eventService.getEvent(loggedUser, eventId);

        return event;
    }
    
    @GetMapping("/events")
    public List<EventDto> getMyCreatedEvents() throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("User is retrieving his created events [user={}]", loggedUser.getEmail());

        List<EventDto> events = eventService.getMyCreatedEvents(loggedUser);

        return events;
    }

}
