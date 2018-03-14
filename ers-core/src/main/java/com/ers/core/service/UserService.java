package com.ers.core.service;

import com.ers.core.dao.UserDao;
import com.ers.core.dao.common.UserConstants;
import com.ers.core.dto.UserCreateDto;
import com.ers.core.dto.UserDto;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import com.ers.core.orm.User;
import com.ers.core.orm.UserProfile;
import com.ers.core.security.PasswordValidator;
import com.ers.core.util.SecurityUtils;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.validator.routines.EmailValidator;

/**
 *
 * @author avillalobos
 */
@Service
public class UserService {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Autowired
    private PasswordValidator passwordValidator;
    
    /**
     * Retrieves an User by its id.
     * 
     * @param id
     * @return
     * @throws ErsException 
     */
    @Transactional(readOnly = true)
    public User getUserById(String id) throws ErsException {
        
        if (StringUtils.isBlank(id)) {
            throw new ErsException("User id is missing", ErsErrorCode.PARAMETER_MISSING);
        }
        
        User user = userDao.getById(id);
        
        if (user == null) {
            throw new ErsException("User not exists", ErsErrorCode.TARGET_NOT_EXISTS);
        }
        
        return user;
    }
    
    /**
     * Retrieves an user by its email.
     * 
     * @param email
     * @return
     * @throws ErsException 
     */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) throws ErsException {
        
        if (StringUtils.isBlank(email)) {
            throw new ErsException("User email is missing", ErsErrorCode.PARAMETER_MISSING);
        }
        
        User user = userDao.getByEmail(email);
        
        if (user == null) {
            throw new ErsException("User not exists", ErsErrorCode.TARGET_NOT_EXISTS);
        }
        
        return user;
    }
    
    /**
     * Used to register users to the platform. 
     * No logged user is necessary. 
     * Returns a UserDto to avoid system-only information like passwords.
     * 
     * @param userDto
     * @return
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    public UserDto createUser(UserCreateDto userDto) throws ErsException {
        
        //Verify if the email is already registered.
        User existingUser = userDao.getByEmail(userDto.getEmail());
        
        if (existingUser != null) {
            throw new ErsException("This user email is already registered", ErsErrorCode.USER_EMAIL_ALREADY_REGISTERED);
        }
        
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());
        user.setType(User.Type.getByName(userDto.getType()));
        
        Date date = new Date();
        
        //Set default values.
        user.setDateCreated(date);
        user.setDateModified(date);
        user.setFailedLoginAttempts(0);
        
        //Validate all user fields.
        validateUser(user);
        
        //Encrypt the password after validation and before saving.
        user.setPassword(SecurityUtils.getBcryptHash(user.getPassword()));
        
        //Save the user.
        user = userDao.save(user);
        
        //Create the user profile.
        UserProfile userProfile = new UserProfile();
        userProfile.setUserId(user.getId());
        userProfile.setUser(user);
        
        //Save the user profile.
        userProfileService.saveUserProfile(userProfile);
        
        user.setUserProfile(userProfile);
        
        UserDto result = converUserToUserDto(user);
        
        LOGGER.info("Account was created for a new user [user={}]", user.toString());
        
        return result;
    }
    
    /**
     * Other services needs to update certain user attributes at some point and
     * no validation is required.
     * 
     * @param existingUser
     * @throws ErsException 
     */
    @Transactional(rollbackFor = Exception.class)
    void updateUser(User existingUser) throws ErsException {
        userDao.update(existingUser);
    }
    
    /**
     * Validates user's fields.
     * 
     * @param user
     * @throws ErsException 
     */
    private void validateUser(User user) throws ErsException {
        
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
            throw new ErsException("Email is invalid", ErsErrorCode.INVALID_EMAIL);
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
     * Converts a User to its UserDto representation.
     * 
     * @param user
     * @return 
     */
    private UserDto converUserToUserDto(User user) {
        
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setStatus(user.getStatus().name());
        dto.setType(user.getType().name());
        
        return dto;
    }
}
