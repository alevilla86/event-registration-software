/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.orm;
// Generated Mar 9, 2018 2:36:46 PM by Hibernate Tools 4.3.1

import com.ers.core.constants.EventConstants;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * Event generated by hbm2java
 */
@Entity
@Table(name = "event")
public class Event implements java.io.Serializable {

    private String id;
    private String name;
    private String description;
    private String createdByUserId;
    private String createdByUserEmail;
    private Date dateCreated;
    private Date dateModified;
    private Date dateStart;
    private Date dateEnd;
    private Date dateLimitRegister;
    private String country;
    private String state;
    private String city;
    private String street1;
    private String street2;
    private String website;
    private String socialNetworkFb;
    private String socialNetworkIg;
    private String socialNetworkTw;
    private String email;
    private List<UserJoinEvent> userJoinEvents = new ArrayList<>(0);
    private List<EventRegistrationOption> eventRegistrationOptions = new ArrayList<>(0);

    public Event() {
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

    @Column(name = "name", nullable = false, length = EventConstants.MAX_EVENT_NAME_LENGTH)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", length = EventConstants.MAX_EVENT_DESCRIPTION_LENGTH)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "created_by_user_id", nullable = false, length = 36)
    public String getCreatedByUserId() {
        return this.createdByUserId;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    @Column(name = "created_by_user_email", nullable = false, length = EventConstants.MAX_EVENT_CREATED_BY_USER_EMAIL_LENGTH)
    public String getCreatedByUserEmail() {
        return this.createdByUserEmail;
    }

    public void setCreatedByUserEmail(String createdByUserEmail) {
        this.createdByUserEmail = createdByUserEmail;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false, length = 19)
    public Date getDateCreated() {
        return this.dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_modified", nullable = false, length = 19)
    public Date getDateModified() {
        return this.dateModified;
    }

    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_start", nullable = false, length = 19)
    public Date getDateStart() {
        return this.dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_end", nullable = false, length = 19)
    public Date getDateEnd() {
        return this.dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_limit_register", nullable = false, length = 19)
    public Date getDateLimitRegister() {
        return this.dateLimitRegister;
    }

    public void setDateLimitRegister(Date dateLimitRegister) {
        this.dateLimitRegister = dateLimitRegister;
    }

    @Column(name = "country", nullable = false, length = EventConstants.MAX_EVENT_COUNTRY_LENGTH)
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "state", nullable = false, length = EventConstants.MAX_EVENT_STATE_LENGTH)
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "city", nullable = false, length = EventConstants.MAX_EVENT_CITY_LENGTH)
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "street1", length = EventConstants.MAX_EVENT_STREET_LENGTH)
    public String getStreet1() {
        return this.street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    @Column(name = "street2", length = EventConstants.MAX_EVENT_STREET_LENGTH)
    public String getStreet2() {
        return this.street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    @Column(name = "website", length = EventConstants.MAX_EVENT_WEBSITE_LENGTH)
    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Column(name = "social_network_fb", length = EventConstants.MAX_EVENT_SOCIAL_NETWORK_LENGTH)
    public String getSocialNetworkFb() {
        return this.socialNetworkFb;
    }

    public void setSocialNetworkFb(String socialNetworkFb) {
        this.socialNetworkFb = socialNetworkFb;
    }

    @Column(name = "social_network_ig", length = EventConstants.MAX_EVENT_SOCIAL_NETWORK_LENGTH)
    public String getSocialNetworkIg() {
        return this.socialNetworkIg;
    }

    public void setSocialNetworkIg(String socialNetworkIg) {
        this.socialNetworkIg = socialNetworkIg;
    }

    @Column(name = "social_network_tw", length = EventConstants.MAX_EVENT_SOCIAL_NETWORK_LENGTH)
    public String getSocialNetworkTw() {
        return this.socialNetworkTw;
    }

    public void setSocialNetworkTw(String socialNetworkTw) {
        this.socialNetworkTw = socialNetworkTw;
    }

    @Column(name = "email", nullable = false, length = EventConstants.MAX_EVENT_EMAIL_LENGTH)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OneToMany(fetch=FetchType.LAZY, mappedBy="event")
    public List<UserJoinEvent> getUserJoinEvents() {
        return this.userJoinEvents;
    }
    
    public void setUserJoinEvents(List<UserJoinEvent> userJoinEvents) {
        this.userJoinEvents = userJoinEvents;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event")
    public List<EventRegistrationOption> getEventRegistrationOptions() {
        return this.eventRegistrationOptions;
    }

    public void setEventRegistrationOptions(List<EventRegistrationOption> eventRegistrationOptions) {
        this.eventRegistrationOptions = eventRegistrationOptions;
    }
    
}
