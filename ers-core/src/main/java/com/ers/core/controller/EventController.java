/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.controller;

import com.ers.core.constants.FileConstants;
import com.ers.core.dto.EventDto;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.User;
import com.ers.core.service.EventService;
import com.ers.core.service.picture.GetPictureService;
import com.ers.core.util.ErsFileUtils;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    
    @DeleteMapping("/events/{eventId}")
    public boolean deleteEvent(@PathVariable("eventId") String eventId) throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("User is deleting an event [user={}, eventId={}]", loggedUser.getEmail(), eventId);

        eventService.deleteEvent(loggedUser, eventId);

        return true;
    }
    
    @PostMapping("/events/{eventId}/picture")
    public boolean uploadUserProfilePicture(
            HttpServletRequest httpRequest,
            @PathVariable("eventId") String eventId,
            @RequestParam("file") MultipartFile multipartFile
    ) throws ErsException, IOException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("{} is uploading event picture", loggedUser.getEmail());

        String origFileName = multipartFile.getOriginalFilename();

        // Transfer the multipart file to a temporary file.
        // Use a random name because other files with the same name could be uploaded into different folders at the same time.
        File dataFile = ersFileUtils.createTempFile();
        multipartFile.transferTo(dataFile);

        try {
            eventService.updateEventPicture(loggedUser, eventId, origFileName, dataFile, httpRequest);
        } finally {
            ErsFileUtils.deleteQuietly(dataFile);
        }

        return true;
    }
    
    @DeleteMapping("/events/{eventId}/picture")
    public boolean deleteUserProfilePicture(@PathVariable("eventId") String eventId) throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("{} is deleting event picture", loggedUser.getEmail());

        eventService.deleteEventPicture(loggedUser, eventId);

        return true;
    }
    
    @GetMapping("/events/{eventId}/picture")
    public ResponseEntity<byte[]> downloadUserProfilePicture(
            @PathVariable("eventId") String eventId,
            @RequestParam(value = "width", required = false, defaultValue = "0") int width,
            @RequestParam(value = "height", required = false, defaultValue = "0") int height
    ) throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("{} is getting {} event picture, width={}, height={}", loggedUser.getEmail(), eventId, width, height);

        byte[] picture = getPictureService.getPicture(loggedUser, eventId, width, height, GetPictureService.PictureServiceType.EVENT);

        HttpHeaders responseHeaders = new HttpHeaders();

        // The profile picture is kept in PNG format.
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, FileConstants.PNG_MIME_TYPE);

        return new ResponseEntity<>(picture, responseHeaders, HttpStatus.OK);
    }
    
    @GetMapping(value = "/events/{eventId}/picture/base64")
    public String downloadUserProfilePictureBase64(
            @PathVariable("eventId") String eventId,
            @RequestParam(value = "width", required = false, defaultValue = "0") int width,
            @RequestParam(value = "height", required = false, defaultValue = "0") int height
    ) throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("{} is getting {} event picture base64, width={}, height={}", loggedUser.getEmail(), eventId, width, height);

        byte[] picture = getPictureService.getPicture(loggedUser, eventId, width, height, GetPictureService.PictureServiceType.EVENT);

        if (picture == null) {
            return null;
        }

        return Base64.getEncoder().encodeToString(picture);
    }
    
    @GetMapping(value = "/events/{eventId}/has_picture")
    public boolean hasUserProfilePicture(@PathVariable("eventId") String eventId) throws ErsException {

        User loggedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        LOGGER.info("{} is verifying if {} event has a picture", loggedUser.getEmail(), eventId);

        boolean result = eventService.hasUserProfilePicture(eventId);

        return result;
    }

}
