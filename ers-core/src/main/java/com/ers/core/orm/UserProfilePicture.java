/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.orm;
// Generated Mar 9, 2018 2:36:46 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * UserProfilePicture generated by hbm2java
 */
@Entity
@Table(name = "user_profile_picture")
public class UserProfilePicture implements java.io.Serializable, Picture {

    private String id;
    private String userId;
    private int width;
    private int height;
    private byte[] picture;

    public UserProfilePicture() {
    }
    
    public UserProfilePicture(String userId, byte[] picture, int width, int height) {
        this.userId = userId;
        this.picture = picture;
        this.width = width;
        this.height = height;
    }
    
    /**
     * "Factory" method: 
     * @param userProfileId
     * @param picture
     * @return
     */
    public static UserProfilePicture newOriginalPicture(String userProfileId, byte[] picture) {
        // The original picture is indicated by width == height == 0;
        return new UserProfilePicture(userProfileId, picture, 0, 0);
    }

    @Id
    @GenericGenerator(name = "id", strategy = "com.ers.core.util.RandomUUIDGenerator")
    @GeneratedValue(generator = "id")
    @Column(name = "id", unique = true, nullable = false, length = 36)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "user_id", nullable = false)
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name = "width", nullable = false)
    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Column(name = "height", nullable = false)
    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Column(name = "picture", nullable = false)
    @Override
    public byte[] getPicture() {
        return this.picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

}
