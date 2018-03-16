package com.ers.core.dto;

import java.util.Date;

/**
 *
 * @author avillalobos
 */
public class EventDto {
    
    private String id;
    private String name;
    private String description;
    private String createdByUserEmail;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedByUserEmail() {
        return createdByUserEmail;
    }

    public void setCreatedByUserEmail(String createdByUserEmail) {
        this.createdByUserEmail = createdByUserEmail;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getDateLimitRegister() {
        return dateLimitRegister;
    }

    public void setDateLimitRegister(Date dateLimitRegister) {
        this.dateLimitRegister = dateLimitRegister;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSocialNetworkFb() {
        return socialNetworkFb;
    }

    public void setSocialNetworkFb(String socialNetworkFb) {
        this.socialNetworkFb = socialNetworkFb;
    }

    public String getSocialNetworkIg() {
        return socialNetworkIg;
    }

    public void setSocialNetworkIg(String socialNetworkIg) {
        this.socialNetworkIg = socialNetworkIg;
    }

    public String getSocialNetworkTw() {
        return socialNetworkTw;
    }

    public void setSocialNetworkTw(String socialNetworkTw) {
        this.socialNetworkTw = socialNetworkTw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EventDto{" + "id=" + id + ", name=" + name + ", description=" + description + 
                ", createdByUserEmail=" + createdByUserEmail + ", dateStart=" + dateStart + 
                ", dateEnd=" + dateEnd + ", dateLimitRegister=" + dateLimitRegister + 
                ", country=" + country + ", state=" + state + ", city=" + city +
                ", street1=" + street1 + ", street2=" + street2 + ", website=" + website +
                ", socialNetworkFb=" + socialNetworkFb + ", socialNetworkIg=" + socialNetworkIg + 
                ", socialNetworkTw=" + socialNetworkTw + ", email=" + email + '}';
    }

}
