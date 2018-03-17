/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service;

import com.ers.core.dao.EventDao;
import com.ers.core.dao.EventPictureDao;
import com.ers.core.dto.EventDto;
import com.ers.core.exception.ErsAccessDeniedException;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Event;
import com.ers.core.orm.EventPicture;
import com.ers.core.orm.User;
import com.ers.core.util.EntityValidatorUtil;
import com.ers.core.util.ImageUtils;
import com.mchange.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author avillalobos
 */
@Service
public class EventService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EventService.class);
    
    @Autowired
    private EventDao eventDao;
    
    @Autowired
    private EntityValidatorUtil validator;
    
    @Autowired
    private EventPictureDao eventPictureDao;
    
    /**
     * Gets an Event by its id.
     * 
     * @param loggedUser
     * @param eventId
     * @return
     * @throws ErsException 
     */
    @Transactional(readOnly = true)
    public EventDto getEvent(User loggedUser, String eventId) throws ErsException {
        
        if (StringUtils.isBlank(eventId)) {
            throw new ErsException("The id is missing to get the event", ErsErrorCode.PARAMETER_MISSING);
        }
        
        Event event = eventDao.getById(eventId);
        
        if (event == null) {
            throw new ErsException("Event not exists", ErsErrorCode.NOT_FOUND_EVENT);
        }
        
        EventDto result = converToEventDto(event);
        
        LOGGER.info("User {} retrieved the event {} [event={}]", loggedUser.getEmail(), eventId, result.toString());
        
        return result;
    }
    
    /**
     * Saves an event.
     * Only ADMIN or ORGANIZER can save events.
     * 
     * @param loggedUser
     * @param dto
     * @return
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public EventDto saveEvent(User loggedUser, EventDto dto) throws ErsException {
        
        //Only allowed to admins and organizers.
        if (!loggedUser.isAdminOrOrganizer()) {
            throw new ErsAccessDeniedException("Only ADMIN and ORGANIZER can create events");
        }
        
        Date now = new Date();
        
        Event event = new Event();
        event.setName(dto.getName());
        event.setDescription(dto.getDescription());
        event.setDateStart(dto.getDateStart());
        event.setDateEnd(dto.getDateEnd());
        event.setDateLimitRegister(dto.getDateLimitRegister());
        event.setCountry(dto.getCountry());
        event.setState(dto.getState());
        event.setCity(dto.getCity());
        event.setStreet1(dto.getStreet1());
        event.setStreet2(dto.getStreet2());
        event.setWebsite(dto.getWebsite());
        event.setSocialNetworkFb(dto.getSocialNetworkFb());
        event.setSocialNetworkIg(dto.getSocialNetworkIg());
        event.setSocialNetworkTw(dto.getSocialNetworkTw());
        event.setEmail(dto.getEmail());
        
        //Default values.
        event.setCreatedByUserId(loggedUser.getId());
        event.setCreatedByUserEmail(loggedUser.getEmail());
        event.setDateCreated(now);
        event.setDateModified(now);
        
        //Validate all event fields.
        validator.validateEvent(event);
        
        event = eventDao.save(event);
        
        EventDto result = converToEventDto(event);
        
        LOGGER.info("User {} created an event [event={}]", loggedUser.getEmail(), result.toString());
        
        return result;
    }
    
    /**
     * Saves an event.
     * Only ADMIN or ORGANIZER can save events.
     * 
     * @param loggedUser
     * @param eventId
     * @param updatedEvent
     * @return
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public EventDto updateEvent(User loggedUser, String eventId, EventDto updatedEvent) throws ErsException {
        
        if (StringUtils.isBlank(eventId)) {
            throw new ErsException("Event missing to update", ErsErrorCode.PARAMETER_MISSING);
        }
        
        Event existingEvent = eventDao.getById(eventId);
        
        if (existingEvent == null) {
            throw new ErsException("Trying to update a non-existing event", ErsErrorCode.NOT_FOUND_EVENT);
        }
        
        //Only ADMINS and the creator can update an event.
        if (!StringUtils.equals(loggedUser.getId(), existingEvent.getCreatedByUserId()) && !loggedUser.isAdmin()) {
            throw new ErsAccessDeniedException("Only the creator or an ADMIN can update the event");
        }
        
        Date now = new Date();
        
        existingEvent.setName(updatedEvent.getName());
        existingEvent.setDescription(updatedEvent.getDescription());
        existingEvent.setDateStart(updatedEvent.getDateStart());
        existingEvent.setDateEnd(updatedEvent.getDateEnd());
        existingEvent.setDateLimitRegister(updatedEvent.getDateLimitRegister());
        existingEvent.setCountry(updatedEvent.getCountry());
        existingEvent.setState(updatedEvent.getState());
        existingEvent.setCity(updatedEvent.getCity());
        existingEvent.setStreet1(updatedEvent.getStreet1());
        existingEvent.setStreet2(updatedEvent.getStreet2());
        existingEvent.setWebsite(updatedEvent.getWebsite());
        existingEvent.setSocialNetworkFb(updatedEvent.getSocialNetworkFb());
        existingEvent.setSocialNetworkIg(updatedEvent.getSocialNetworkIg());
        existingEvent.setSocialNetworkTw(updatedEvent.getSocialNetworkTw());
        existingEvent.setEmail(updatedEvent.getEmail());
        
        existingEvent.setDateModified(now);
        
        //Validate all event fields.
        validator.validateEvent(existingEvent);
        
        //Persist the event.
        existingEvent = eventDao.update(existingEvent);
        
        EventDto result = converToEventDto(existingEvent);
        
        LOGGER.info("User {} updated an event [eventId={}, event={}]", loggedUser.getEmail(), eventId, result.toString());
        
        return result;
    }
    
    /**
     * Deletes an event if the user is authorized.
     * 
     * @param loggedUser
     * @param eventId
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteEvent(User loggedUser, String eventId) throws ErsException {
        
        if (StringUtils.isBlank(eventId)) {
            LOGGER.warn("Trying to delete an event without id");
            return;
        }
        
        Event existingEvent = eventDao.getById(eventId);
        
        if (existingEvent == null) {
            LOGGER.warn("Trying to delete a non-existing event {}", eventId);
            return;
        }
        
        if (!StringUtils.equals(loggedUser.getId(), eventId) && !loggedUser.isAdmin()) {
            LOGGER.warn("Trying to delete an event with a non-authorized user [user={}, eventId={}, eventName={}]", loggedUser.getEmail(), existingEvent.getId(), existingEvent.getName());
            throw new ErsAccessDeniedException("Only the creator or an ADMIN can delete events");
        }
        
        //If the event has USERS joined to it, it can be deleted.
        if (!existingEvent.getUsers().isEmpty()) {
            throw new ErsException("Event has people registered", ErsErrorCode.EVENT_HAS_USERS_REGISTERED);
        }
        
        eventDao.delete(existingEvent);
    }
    
    /**
     * Converts an Event to EventDto object.
     * 
     * @param event
     * @return 
     */
    private EventDto converToEventDto(Event event) {
        
        EventDto dto = new EventDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        dto.setDescription(event.getDescription());
        dto.setDateStart(event.getDateStart());
        dto.setDateEnd(event.getDateEnd());
        dto.setDateLimitRegister(event.getDateLimitRegister());
        dto.setCountry(event.getCountry());
        dto.setState(event.getState());
        dto.setCity(event.getCity());
        dto.setStreet1(event.getStreet1());
        dto.setStreet2(event.getStreet2());
        dto.setWebsite(event.getWebsite());
        dto.setSocialNetworkFb(event.getSocialNetworkFb());
        dto.setSocialNetworkIg(event.getSocialNetworkIg());
        dto.setSocialNetworkTw(event.getSocialNetworkTw());
        dto.setEmail(event.getEmail());
        
        return dto;
    }
    
    /**
     * Uploads an event picture.
     * 
     * @param loggedUser
     * @param eventId
     * @param pictureFileName
     * @param dataFile
     * @param httpRequest
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateEventPicture(User loggedUser, String eventId, String pictureFileName, File dataFile, HttpServletRequest httpRequest) throws ErsException {

        Event existingEvent = eventDao.getById(eventId);

        if (existingEvent == null) {
            throw new ErsException("Event not found", ErsErrorCode.NOT_FOUND_EVENT);
        }
        
        if (!StringUtils.equals(loggedUser.getId(), eventId) && !loggedUser.isAdmin()) {
            LOGGER.warn("Trying to upload an event picture with a non-authorized user [user={}, eventId={}, eventName={}]", loggedUser.getEmail(), existingEvent.getId(), existingEvent.getName());
            throw new ErsAccessDeniedException("Only the creator or an ADMIN can upload event pictures");
        }

        validator.validatePicture(pictureFileName, dataFile);

        // Delete all the other old pictures
        eventPictureDao.deletePictures(eventId);

        byte[] picture;
        try {
            picture = FileUtils.getBytes(dataFile);
        } catch (IOException ex) {

            throw new ErsException("Failed to process the picture", ErsErrorCode.IO_EXCEPTION, ex);
        }

        // Convert to PNG keeping the original size.
        // This may be a "waste" if the picture is already in PNG format.
        byte[] pngPicture = ImageUtils.resizeImageToPng(picture, -1, -1);

        // Save the new picture: this is the original picture because is saved with width == height == 0
        EventPicture userProfilePicture = EventPicture.newOriginalPicture(eventId, pngPicture);
        eventPictureDao.save(userProfilePicture);
    }
    
    /**
     * Deletes all user profile pictures.
     * 
     * @param eventId
     * @param loggedUser
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteEventPicture(User loggedUser, String eventId) throws ErsException {
        
        Event existingEvent = eventDao.getById(eventId);

        if (existingEvent == null) {
            LOGGER.error("Trying to delete the picture of a non-existing event [eventId={}]", eventId);
            return;
        }

        if (!StringUtils.equals(existingEvent.getCreatedByUserId(), loggedUser.getId())) {
            throw new ErsAccessDeniedException("You are not authorized to delete this event picture");
        }

        eventPictureDao.deletePictures(eventId);
    }
    
    @Transactional(readOnly = true)
    public boolean hasUserProfilePicture(String eventId) throws ErsException {
        boolean result = eventPictureDao.hasOriginalPicture(eventId);
        return result;
    }

}
