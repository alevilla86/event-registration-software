/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service;

import com.ers.core.dao.EventDao;
import com.ers.core.dao.EventPictureDao;
import com.ers.core.dao.UserJoinEventDao;
import com.ers.core.dto.EventDto;
import com.ers.core.dto.EventRegistrationOptionDto;
import com.ers.core.exception.ErsAccessDeniedException;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Currency;
import com.ers.core.orm.Event;
import com.ers.core.orm.EventPicture;
import com.ers.core.orm.EventRegistrationOption;
import com.ers.core.orm.User;
import com.ers.core.orm.UserJoinEvent;
import com.ers.core.orm.UserJoinEventId;
import com.ers.core.orm.UserProfile;
import com.ers.core.util.EntityValidatorUtil;
import com.ers.core.util.ImageUtils;
import com.mchange.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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
    private UserJoinEventDao userJoinEventDao;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private EventRegistrationOptionService optionService;
    
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
            throw new ErsException("Event id missing to update", ErsErrorCode.PARAMETER_MISSING);
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
        
        if (!StringUtils.equals(loggedUser.getId(), existingEvent.getCreatedByUserId()) && !loggedUser.isAdmin()) {
            LOGGER.warn("Trying to delete an event with a non-authorized user [user={}, eventId={}, eventName={}]", loggedUser.getEmail(), existingEvent.getId(), existingEvent.getName());
            throw new ErsAccessDeniedException("Only the creator or an ADMIN can delete events");
        }
        
        //If the event has USERS joined to it, it can be deleted.
        if (!existingEvent.getUserJoinEvents().isEmpty()) {
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
        
        if (!StringUtils.equals(loggedUser.getId(), existingEvent.getCreatedByUserId()) && !loggedUser.isAdmin()) {
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

        if (!StringUtils.equals(existingEvent.getCreatedByUserId(), loggedUser.getId()) && !loggedUser.isAdmin()) {
            throw new ErsAccessDeniedException("You are not authorized to delete this event picture");
        }

        eventPictureDao.deletePictures(eventId);
    }
    
    @Transactional(readOnly = true)
    public boolean hasUserProfilePicture(String eventId) throws ErsException {
        boolean result = eventPictureDao.hasOriginalPicture(eventId);
        return result;
    }

    /**
     * Gets the events created by the logged user.
     * 
     * @param loggedUser
     * @return
     * @throws ErsException 
     */
    @Transactional(readOnly = true)
    public List<EventDto> getMyCreatedEvents(User loggedUser) throws ErsException {
        
        List<Event> myEvents = eventDao.getMyCreatedEvents(loggedUser.getId());
        
        if (myEvents == null || myEvents.isEmpty()) {
            LOGGER.info("User {} has no created events", loggedUser.getEmail());
            return Collections.EMPTY_LIST;
        }
        
        List<EventDto> result = new ArrayList<>(myEvents.size());
        
        for (Event event : myEvents) {
            EventDto dto = converToEventDto(event);
            result.add(dto);
        }
        
        LOGGER.info("User {} has {} events created by him", loggedUser.getEmail(), result.size());
        
        return result;
    }
    
    /**
     * Saves a new event registration option for an existing event.
     * 
     * @param loggedUser
     * @param eventId
     * @param dto
     * @return
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public EventRegistrationOptionDto saveEventRegistrationOption(User loggedUser, String eventId, EventRegistrationOptionDto dto) throws ErsException {
        
        if (!loggedUser.isAdminOrOrganizer()) {
            throw new ErsAccessDeniedException("Only ADMIN and ORGANIZER can add event options");
        }
        
        if (StringUtils.isBlank(eventId)) {
            throw new ErsException("Event id is missing to save a registration option", ErsErrorCode.PARAMETER_MISSING);
        }
        
        Event event = eventDao.getById(eventId);
        
        if (event == null) {
            throw new ErsException("EventRegistration option can not be saved because the event not exists", ErsErrorCode.NOT_FOUND_EVENT);
        }
        
        //Validate the user adding the option to the event is the one who created the event.
        if (!StringUtils.equals(loggedUser.getId(), event.getCreatedByUserId()) && !loggedUser.isAdmin()) {
            throw new ErsAccessDeniedException("Only the user who created the event or an ADMIN can add registration options");
        }
        
        EventRegistrationOption option = new EventRegistrationOption();
        option.setEvent(event);
        option.setName(dto.getName());
        option.setDescription(dto.getDescription());
        option.setRegistrationCost(dto.getRegistrationCost());
        option.setRegistrationCostCurrency(Currency.getByName(dto.getRegistrationCostCurrency()));
        
        //This methods makes all validations of fields.
        option = optionService.saveEventRegistrationOption(option);
        
        EventRegistrationOptionDto result = optionService.convertEventToEventRegistrationOptionDto(option);
        
        LOGGER.info("User {} saved a new event registration option [eventid={}, eventName={}, optionId={}, optionName={}]", loggedUser.getEmail(), event.getId(), event.getName(), option.getId(), option.getName());
        
        return result;
    }
    
    /**
     * Updates an existing event registration option.
     * 
     * @param loggedUser
     * @param eventId
     * @param optionId
     * @param updatedDto
     * @return
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public EventRegistrationOptionDto updateEventRegistrationOption(User loggedUser, String eventId, String optionId, EventRegistrationOptionDto updatedDto) throws ErsException {
        
        if (!loggedUser.isAdminOrOrganizer()) {
            throw new ErsAccessDeniedException("Only ADMIN and ORGANIZER can update event options");
        }
        
        if (StringUtils.isBlank(eventId)) {
            throw new ErsException("Event id is missing to update a registration option", ErsErrorCode.PARAMETER_MISSING);
        }
        
        Event event = eventDao.getById(eventId);
        
        if (event == null) {
            throw new ErsException("EventRegistration option can not be updated because the event not exists", ErsErrorCode.NOT_FOUND_EVENT);
        }
        
        //Validate the user adding the option to the event is the one who created the event.
        if (!StringUtils.equals(loggedUser.getId(), event.getCreatedByUserId()) && !loggedUser.isAdmin()) {
            throw new ErsAccessDeniedException("Only the user who created the event or an ADMIN can update registration options");
        }
        
        EventRegistrationOption existingOption = optionService.getEventRegistrationOptionById(optionId);
        
        if (existingOption == null) {
            throw new ErsException("EventRegistration option not exists", ErsErrorCode.NOT_FOUND_EVENT_OPTION);
        }
        
        //Validate the option is from the specified event.
        validateIfRegistrationOptionIsFromEvent(existingOption, event);
        
        existingOption.setName(updatedDto.getName());
        existingOption.setDescription(updatedDto.getDescription());
        existingOption.setRegistrationCost(updatedDto.getRegistrationCost());
        existingOption.setRegistrationCostCurrency(Currency.getByName(updatedDto.getRegistrationCostCurrency()));
        
        //This method validates all the fields.
        optionService.updateEventRegistrationOption(existingOption);
        
        EventRegistrationOptionDto result = optionService.convertEventToEventRegistrationOptionDto(existingOption);
        
        LOGGER.info("User {} updated an existing event registration option [eventid={}, eventName={}, optionId={}, optionName={}]", loggedUser.getEmail(), event.getId(), event.getName(), existingOption.getId(), existingOption.getName());
        
        return result;
    }
    
    /**
     * Gets the list of event registration options for an event.
     * 
     * @param loggedUser
     * @param eventId
     * @return
     * @throws ErsException 
     */
    @Transactional(readOnly = true)
    public List<EventRegistrationOptionDto> getRegistrationOptionsByEventId(User loggedUser, String eventId) throws ErsException {
        
        if (StringUtils.isBlank(eventId)) {
            throw new ErsException("Event id is missing to get the registration options", ErsErrorCode.PARAMETER_MISSING);
        }
        
        Event event = eventDao.getById(eventId);
        
        if (event == null) {
            throw new ErsException("EventRegistration options can not be retrieved because the event not exists", ErsErrorCode.NOT_FOUND_EVENT);
        }
        
        List<EventRegistrationOption> options = optionService.getEventRegistrationOptionsByEventId(eventId);
        
        if (options.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        
        List<EventRegistrationOptionDto> result = new ArrayList<>(options.size());
        
        for (EventRegistrationOption option : options) {
            EventRegistrationOptionDto dto = optionService.convertEventToEventRegistrationOptionDto(option);
            result.add(dto);
        }
        
        return result;
    }
    
    /**
     * Deletes an event registration option.
     * 
     * @param loggedUser
     * @param eventId
     * @param optionId
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteEventRegistrationOption(User loggedUser, String eventId, String optionId) throws ErsException {
        
        if (!loggedUser.isAdminOrOrganizer()) {
            throw new ErsAccessDeniedException("Only ADMIN and ORGANIZER can delete event options");
        }
        
        if (StringUtils.isBlank(optionId) || StringUtils.isBlank(eventId)) {
            throw new ErsException("Option id is required to delete the option", ErsErrorCode.PARAMETER_MISSING);
        }
        
        Event event = eventDao.getById(eventId);
        
        if (event == null) {
            LOGGER.warn("Trying to delete a registration option for a non-existing event");
            return;
        }
        
        EventRegistrationOption option;
        
        try {
            
            option = optionService.getEventRegistrationOptionById(optionId);
            
        } catch (ErsException ex) {
            LOGGER.warn("Trying to delete a non-existing EventRegistration option {}", optionId);
            return;
        }
        
        //Validate the option is from the specified event.
        validateIfRegistrationOptionIsFromEvent(option, event);
        
        //Validate the user adding the option to the event is the one who created the event.
        if (!StringUtils.equals(loggedUser.getId(), event.getCreatedByUserId()) && !loggedUser.isAdmin()) {
            throw new ErsAccessDeniedException("Only the user who created the event or an ADMIN can delete registration options");
        }
        
        optionService.deleteEventRegistrationOption(option);
        
        LOGGER.info("User {} has deleted the EventRegistrationOption {} with name {}", loggedUser.getEmail(), option.getName(), option.getName());
    }
    
    /**
     * Register an user to an event.
     * Validates the event, the user and option exists and the dates are correct.
     * 
     * @param loggedUser
     * @param userId
     * @param eventId
     * @param registrationOptionId
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public void registerUserToEvent(User loggedUser, String userId, String eventId, String registrationOptionId) throws ErsException {
        
        //First validate the params.
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(eventId) || StringUtils.isBlank(registrationOptionId)) {
            throw new ErsException("To register an user to an event userId, eventId and registrationOptionId are required", ErsErrorCode.PARAMETER_MISSING);
        }
        
        //Get the event.
        Event event = eventDao.getById(eventId);
        
        if (event == null) {
            throw new ErsException("Trying to register an user to a non-existing event", ErsErrorCode.NOT_FOUND_EVENT);
        }
        
        //Get the user.
        User user = userService.getUserById(userId);
        
        //Load the user profile.
        UserProfile userProfile = user.getUserProfile();
        
        //Only users with the profile completed can be registered.
        if (!validator.isUserProfileComplete(userProfile, User.Type.USER)) {
            throw new ErsException("In order to complete the registration process we need the user profile information to be complete", ErsErrorCode.USER_PROFILE_NOT_COMPLETED);
        }
        
        //Get the registration option.
        EventRegistrationOption option = optionService.getEventRegistrationOptionById(registrationOptionId);
        
        //The event exists, the user exists, the registration option exists.
        //Verify the option corresponds to the event.
        validateIfRegistrationOptionIsFromEvent(option, event);
        
        Date now = new Date();
        
        //Verify the event is still accepting registrations.
        if (now.after(event.getDateLimitRegister())) {
            throw new ErsException("Revent registration date has expired", ErsErrorCode.EVENT_REGISTRATION_DATE_EXPIRED);
        }
        
        //Build the user join event.
        UserJoinEventId joinEventId = new UserJoinEventId(userId, eventId);
        UserJoinEvent joinEvent = new UserJoinEvent();
        joinEvent.setId(joinEventId);
        joinEvent.setEvent(event);
        joinEvent.setUser(user);
        joinEvent.setCurrency(option.getRegistrationCostCurrency());
        joinEvent.setAmountPaid(option.getRegistrationCost());
        joinEvent.setRegisteredByUserId(loggedUser.getId());
        joinEvent.setRegisteredByUserEmail(loggedUser.getEmail());
        joinEvent.setDateRegistered(now);
        
        //Category and shirt size are not required.
        joinEvent.setCategory(userProfile.getCategory().getName());
        joinEvent.setShirtSize(userProfile.getShirtSize());
        
        validator.validateUserJoinEvent(joinEvent);
        
        userJoinEventDao.save(joinEvent);
        
        //TODO: payment goes here. if an exception is thrown during payment the
        //tx will be rolled back and not saved the user in the user_join_event table.
        //Include in the user_join_event table a confirmation_id for payment.
        
        LOGGER.info("User {} registered user {} to the event [eventId={}, eventName={}] with the event option [optionId={}, optionName={}]", 
                loggedUser.getEmail(), user.getEmail(), event.getId(), event.getName(), option.getId(), option.getName());
    }
    
    /**
     * Validates if an option for registration is saved for an specific event.
     * 
     * @param option
     * @param event
     * @throws ErsException 
     */
    private void validateIfRegistrationOptionIsFromEvent(EventRegistrationOption option, Event event) throws ErsException {
        if (!StringUtils.equals(option.getEvent().getId(), event.getId())) {
            throw new ErsException("Event and option exists but the option is for another event", ErsErrorCode.EVENT_REGISTRATION_OPTION_CORRESPONDS_TO_OTHER_EVENT);
        }
    }

}
