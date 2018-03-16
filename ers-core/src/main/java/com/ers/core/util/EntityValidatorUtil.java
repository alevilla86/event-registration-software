package com.ers.core.util;

import com.ers.core.constants.EventConstants;
import com.ers.core.constants.UserConstants;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.Event;
import com.ers.core.orm.User;
import com.ers.core.orm.UserProfile;
import com.ers.core.security.PasswordValidator;
import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Contains methods to validate entities before saving them to the database.
 * 
 * @author avillalobos
 */
@Component
public class EntityValidatorUtil {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityValidatorUtil.class);
    
    @Autowired
    private PasswordValidator passwordValidator;
    
    @Autowired
    private ErsFileUtils ersFileUtils;
    
    /**
     * Validates user's fields.
     * 
     * @param user
     * @throws ErsException 
     */
    public void validateUser(User user) throws ErsException {
        
        if (user == null) {
            throw new ErsException("User is null", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(user.getEmail())) {
            throw new ErsException("User email is blank", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.length(user.getEmail()) > UserConstants.MAX_USER_EMAIL_LENGTH) {
            throw new ErsException("User email too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(user.getEmail())) {
            throw new ErsException("User email is invalid", ErsErrorCode.INVALID_EMAIL);
        }
        
        if (StringUtils.isBlank(user.getPassword())) {
            throw new ErsException("User password is blank", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        passwordValidator.validate(user.getPassword(), user.getEmail());
        
        if (StringUtils.isBlank(user.getFirstName())) {
            throw new ErsException("User first name is blank", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.length(user.getFirstName()) > UserConstants.MAX_USER_NAME_LENGTH) {
            throw new ErsException("User name too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.isBlank(user.getLastName())) {
            throw new ErsException("User last name is blank", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.length(user.getLastName()) > UserConstants.MAX_USER_LAST_NAME_LENGTH) {
            throw new ErsException("User last name too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (user.getType() == null) {
            throw new ErsException("User password is blank", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (user.getStatus() == null) {
            user.setStatus(User.Status.ENABLED);
        }
        
        if (user.getDateCreated() == null) {
            throw new ErsException("User created date is null", ErsErrorCode.GENERAL);
        }
        
        if (user.getDateModified() == null) {
            throw new ErsException("User modified date is null", ErsErrorCode.GENERAL);
        }
        
    }
    
    /**
     * Validates the user profile required fields.
     * Fields not required in the database, but required per business logic.
     * 
     * @param userProfile 
     * @param type 
     * @throws com.ers.core.exception.ErsException 
     */
    public void validateUserProfileFields(UserProfile userProfile, User.Type type) throws ErsException {
        
        if (userProfile == null) {
            throw new ErsException("UserProfile is null", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(userProfile.getGovernmentId())) {
            throw new ErsException("User government id is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getHand() == null) {
            throw new ErsException("User government hand is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getDateBirth() == null) {
            throw new ErsException("User date birth is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getAge() < 0) {
            throw new ErsException("User age is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(userProfile.getPhone())) {
            throw new ErsException("User phone is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getGenre() == null) {
            throw new ErsException("User government idgenre is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(userProfile.getCountry())) {
            throw new ErsException("User country is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(userProfile.getEmergencyContactName())) {
            throw new ErsException("User emergency contact is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(userProfile.getEmergencyContactPhone())) {
            throw new ErsException("User emergency contact phone is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (userProfile.getShirtSize() == null) {
            throw new ErsException("User shirt size is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        /*
        Only USER type users requires to have a role and a category
        */
        if (type != User.Type.ADMIN && userProfile.getRole() == null) {
            throw new ErsException("User role is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
        
        if (type == User.Type.USER && userProfile.getCategory() == null) {
            throw new ErsException("User category is missing", ErsErrorCode.USER_INFORMATION_MISSING);
        }
    }
    
    /**
     * Validates an Event required field, formats and length of attributes.
     * 
     * @param event
     * @throws ErsException 
     */
    public void validateEvent(Event event) throws ErsException {
        
        if (event == null) {
            throw new ErsException("Event is null", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(event.getName())) {
            throw new ErsException("Event name is empty", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (StringUtils.length(event.getDescription()) > EventConstants.MAX_EVENT_DESCRIPTION_LENGTH) {
            throw new ErsException("Event name too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.isBlank(event.getCreatedByUserId())) {
            throw new ErsException("Event created by user id is empty", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (StringUtils.isBlank(event.getCreatedByUserEmail())) {
            throw new ErsException("Event created by user email is empty", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (StringUtils.length(event.getCreatedByUserEmail()) > EventConstants.MAX_EVENT_CREATED_BY_USER_EMAIL_LENGTH) {
            throw new ErsException("Event created by user email too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (event.getDateCreated() == null) {
            throw new ErsException("Event created date is null", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (event.getDateModified() == null) {
            throw new ErsException("Event modified date is null", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (event.getDateStart() == null) {
            throw new ErsException("Event start date is null", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (event.getDateEnd() == null) {
            throw new ErsException("Event end date is null", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (event.getDateStart().after(event.getDateEnd())) {
            throw new ErsException("Event start date is after event end date", ErsErrorCode.INVALID_DATE);
        }
        
        if (event.getDateLimitRegister() == null) {
            throw new ErsException("Event limit register date is null", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (event.getDateLimitRegister().after(event.getDateEnd())) {
            throw new ErsException("Event limit register date is after event end date", ErsErrorCode.INVALID_DATE);
        }
        
        if (StringUtils.isBlank(event.getCountry())) {
            throw new ErsException("Event country is empty", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (StringUtils.length(event.getCountry()) > EventConstants.MAX_EVENT_COUNTRY_LENGTH) {
            throw new ErsException("Event country name too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.isBlank(event.getState())) {
            throw new ErsException("Event state is empty", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (StringUtils.length(event.getState()) > EventConstants.MAX_EVENT_STATE_LENGTH) {
            throw new ErsException("Event state name too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.isBlank(event.getCity())) {
            throw new ErsException("Event city is empty", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (StringUtils.length(event.getCity()) > EventConstants.MAX_EVENT_CITY_LENGTH) {
            throw new ErsException("Event city name too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.length(event.getStreet1()) > EventConstants.MAX_EVENT_STREET_LENGTH) {
            throw new ErsException("Event street1 too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.length(event.getStreet2()) > EventConstants.MAX_EVENT_STREET_LENGTH) {
            throw new ErsException("Event street2 too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.length(event.getWebsite()) > EventConstants.MAX_EVENT_WEBSITE_LENGTH) {
            throw new ErsException("Event website too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.length(event.getSocialNetworkFb()) > EventConstants.MAX_EVENT_SOCIAL_NETWORK_LENGTH) {
            throw new ErsException("Event FB too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.length(event.getSocialNetworkIg()) > EventConstants.MAX_EVENT_SOCIAL_NETWORK_LENGTH) {
            throw new ErsException("Event IG too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.length(event.getSocialNetworkTw()) > EventConstants.MAX_EVENT_SOCIAL_NETWORK_LENGTH) {
            throw new ErsException("Event TW too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        if (StringUtils.isBlank(event.getEmail())) {
            throw new ErsException("Event email is empty", ErsErrorCode.EVENT_INFORMATION_MISSING);
        }
        
        if (StringUtils.length(event.getEmail()) > EventConstants.MAX_EVENT_EMAIL_LENGTH) {
            throw new ErsException("Event email too big", ErsErrorCode.INVALID_ATTRIBUTE_LENGTH);
        }
        
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(event.getEmail())) {
            throw new ErsException("Event email is invalid", ErsErrorCode.INVALID_EMAIL);
        }
        
    }
    
    /**
     * Validates a picture.
     * 
     * @param pictureFileName
     * @param dataFile
     * @throws ErsException 
     */
    public void validatePicture(String pictureFileName, File dataFile) throws ErsException {

        // A weak check: just look at the file extension.
        if (!ersFileUtils.hasValidPictureFileExtension(pictureFileName)) {
            LOGGER.warn("{} doesn't have an allowed extension for pictures", pictureFileName);
            throw new ErsException("Invalid extension for a picture file", ErsErrorCode.INVALID_PICTURE_EXTENSION);
        }

        // A stronger check
        if (!ImageUtils.isImage(dataFile)) {
            LOGGER.warn("{} is not a valid picture file", pictureFileName);
            throw new ErsException("Invalid picture format", ErsErrorCode.INVALID_PICTURE_FORMAT);
        }
    }

}
