/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.service.picture;

/**
 * The rules and meaning of width and height for a user picture are a bit convoluted.
 * The original picture uploaded by the user is identified by width == height == 0.
 * If either width or height is a negative number then the other value is substituted automatically by the resizing library to maintain the aspect ratio of the original image dimensions.
 * Since we save the resized pictures in DB, we identify them using the width and height.
 * If both are negative, then the clean values are 0: they identify the original picture.
 * If either one is 0, then the clean values are 0: they identify the original picture.
 * If only one value is negative, its replaced with -1.
 * 
 * @author avillalobos
 */
class PictureSizeCleaner {
    
    private final int cleanWidth;
    private final int cleanHeight;

    public PictureSizeCleaner(int width, int height) {

        if (width < 0 && height < 0) {
            // Identifies the original picture
            cleanWidth = 0;
            cleanHeight = 0;
        } else if (width == 0) {
            // Identifies the original picture
            cleanWidth = 0;
            cleanHeight = 0;
        } else if (height == 0) {
            // Identifies the original picture
            cleanWidth = 0;
            cleanHeight = 0;
        } else if (width < 0) {
            // It's negative, we don't care about the exact value.
            // Replace it with -1.
            cleanWidth = -1;
            cleanHeight = height;
        } else if (height < 0) {
            cleanWidth = width;
            // It's negative, we don't care about the exact value.
            // Replace it with -1.
            cleanHeight = -1;
        } else {
            // Both positive: keep them unchanged.
            cleanWidth = width;
            cleanHeight = height;
        }
    }

    public int getCleanWidth() {
        return cleanWidth;
    }

    public int getCleanHeight() {
        return cleanHeight;
    }

    @Override
    public String toString() {
        return "PictureSizeCleaner [cleanWidth=" + cleanWidth + ", cleanHeight=" + cleanHeight + "]";
    }

}
