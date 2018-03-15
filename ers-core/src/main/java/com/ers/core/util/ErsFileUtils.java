/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.util;

import com.ers.core.constants.ApplicationPropertiesConstants;
import com.ers.core.exception.ErsErrorCode;
import com.ers.core.exception.ErsException;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author avillalobos
 */
@Component
public class ErsFileUtils {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ErsFileUtils.class);
    
    private static final String DEFAULT_TEMP_FILE_PREFIX = "tmp";

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ApplicationPropertiesUtil appProperties;
    
    /**
     * Gets the extension of a file.
     *
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {

        int lastDotIndex = StringUtils.lastIndexOf(fileName, '.');

        if (lastDotIndex == -1) {
            return StringUtils.EMPTY;
        }

        String extension = fileName.substring(lastDotIndex + 1);

        return extension;
    }

    /**
     * Returns the temporary directory.
     *  
     * @return the temporary directory.
     */
    public File getTempDirectory() {
        // This directory is provided by the servlet container
        File tempDir = (File) servletContext.getAttribute(ServletContext.TEMPDIR);
        return tempDir;
    }

    /**
     * Creates an empty file in the default temporary-file directory. 
     * 
     * @param prefix The prefix string to be used in generating the file's name; must be at least 3 characters long. 
     * ".tmp" suffix is added automatically to the file name.
     * 
     * @return the empty file
     * 
     * @throws ErsException
     */
    public File createTempFile(String prefix) throws ErsException {
        return createTempFile(prefix, null);
    }

    /**
     * Creates a temporary file.
     * 
     * @return temporary file
     * 
     * @throws ErsException
     */
    public File createTempFile() throws ErsException {
        return createTempFile(DEFAULT_TEMP_FILE_PREFIX, null);
    }

    /**
     * Creates an empty file in the default temporary-file directory. 
     * 
     * @param prefix The prefix string to be used in generating the file's name; must be at least 3 characters long
     * @param suffix The suffix string to be used in generating the file's name; may be null, in which case the suffix ".tmp" will be used
     * 
     * @return the empty file
     * 
     * @throws ErsException
     */
    public File createTempFile(String prefix, String suffix) throws ErsException {

        File tempDir = getTempDirectory();

        try {
            return File.createTempFile(prefix, suffix, tempDir);
        } catch (IOException e) {
            throw new ErsException(e.getMessage(), ErsErrorCode.IO_EXCEPTION, e);
        }
    }

    /**
     * Deletes a file, never throwing an exception, but logging the result. If file is a directory, delete it and all sub-directories.
     * 
     * @param file file or directory to delete, can be null
     * @return
     */
    public static boolean deleteQuietly(File file) {
        boolean deleted = FileUtils.deleteQuietly(file);

        String filePath;
        if (file != null) {
            filePath = file.getAbsolutePath();
        } else {
            filePath = "null";
        }

        if (deleted) {
            LOGGER.info("Deleted " + filePath);
        } else {
            LOGGER.warn("Failed to delete " + filePath);
        }

        return deleted;
    }

    /**
     * Verifies if the file extension corresponds to a valid image file.
     *
     * @param fileName
     * @return result
     */
    public boolean hasValidPictureFileExtension(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return false;
        }

        // Getting the configured valid extensions.
        String value = appProperties.getProperty(ApplicationPropertiesConstants.ALLOWED_PICTURE_FILE_EXTENSIONS);
        String[] validExtensions = StringUtils.split(value, ";");

        // Checking if the file extension is included in the list.
        for (String extension : validExtensions) {
            if (StringUtils.endsWithIgnoreCase(fileName, extension)) {
                return true;
            }
        }

        return false;
    }

}
