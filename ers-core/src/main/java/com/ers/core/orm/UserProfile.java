/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.orm;
// Generated Mar 9, 2018 2:36:46 PM by Hibernate Tools 4.3.1

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * UserProfile generated by hbm2java
 */
@Entity
@Table(name = "user_profile")
public class UserProfile implements java.io.Serializable {
    
    public enum Hand {
        LEFT, RIGHT;
        
        public static Hand getByName(String name) {
            for(Hand hand : values()) {
                if (StringUtils.equalsIgnoreCase(name, hand.name())) {
                    return hand;
                }
            }
            return null;
        }
    }
    
    public enum Genre {
        MALE, FEMALE;
        
        public static Genre getByName(String name) {
            for(Genre genre : values()) {
                if (StringUtils.equalsIgnoreCase(name, genre.name())) {
                    return genre;
                }
            }
            return null;
        }
    }
    
    public enum ShirtSize {
        XS, S, M, L, XL, XL2;
        
        public static ShirtSize getByName(String name) {
            for(ShirtSize shirtSize : values()) {
                if (StringUtils.equalsIgnoreCase(name, shirtSize.name())) {
                    return shirtSize;
                }
            }
            return null;
        }
    }

    private String userId;
    private Category category;
    private User user;
    private Role role;
    private String governmentId;
    private String externalId;
    private Hand hand;
    private String team;
    private Date dateBirth;
    private int age;
    private String phone;
    private Genre genre;
    private String weight;
    private String height;
    private String country;
    private String state;
    private String city;
    private String street1;
    private String street2;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private ShirtSize shirtSize;
    private List<Discipline> disciplines = new ArrayList<>(0);

    public UserProfile() {
    }
    
    @Id
    @GenericGenerator(name = "generator", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
    @GeneratedValue(generator = "generator")
    @Column(name = "user_id", unique = true, nullable = false, length = 36)
    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Column(name = "government_id", nullable = false,length = 45)
    public String getGovernmentId() {
        return this.governmentId;
    }

    public void setGovernmentId(String governmentId) {
        this.governmentId = governmentId;
    }

    @Column(name = "external_id", length = 45)
    public String getExternalId() {
        return this.externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "hand")
    public Hand getHand() {
        return this.hand;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    @Column(name = "team", length = 128)
    public String getTeam() {
        return this.team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    @Temporal(TemporalType.DATE)
    @Column(name = "date_birth", length = 10)
    public Date getDateBirth() {
        return this.dateBirth;
    }

    public void setDateBirth(Date dateBirth) {
        this.dateBirth = dateBirth;
    }

    @Column(name = "age")
    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Column(name = "phone", length = 45)
    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Column(name = "weight", length = 6)
    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Column(name = "height", length = 3)
    public String getHeight() {
        return this.height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @Column(name = "country", nullable = false,length = 128)
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Column(name = "state", length = 128)
    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name = "city", length = 128)
    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "street1", length = 512)
    public String getStreet1() {
        return this.street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    @Column(name = "street2", length = 512)
    public String getStreet2() {
        return this.street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    @Column(name = "emergency_contact_name", length = 128)
    public String getEmergencyContactName() {
        return this.emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    @Column(name = "emergency_contact_phone", length = 45)
    public String getEmergencyContactPhone() {
        return this.emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "shirt_size")
    public ShirtSize getShirtSize() {
        return this.shirtSize;
    }

    public void setShirtSize(ShirtSize shirtSize) {
        this.shirtSize = shirtSize;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_profile_has_discipline", joinColumns = {
        @JoinColumn(name = "user_id", nullable = false, updatable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "discipline_id", nullable = false, updatable = false)})
    public List<Discipline> getDisciplines() {
        return this.disciplines;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    @Override
    public String toString() {
        return "UserProfile{" + "userId=" + userId + ", governmentId=" + governmentId + 
                ", externalId=" + externalId + ", hand=" + hand + ", team=" + team + 
                ", dateBirth=" + dateBirth + ", age=" + age + ", phone=" + phone + 
                ", genre=" + genre + ", weight=" + weight + ", height=" + height + 
                ", country=" + country + ", state=" + state + ", city=" + city + 
                ", street1=" + street1 + ", street2=" + street2 + 
                ", emergencyContactName=" + emergencyContactName + 
                ", emergencyContactPhone=" + emergencyContactPhone + 
                ", shirtSize=" + shirtSize + '}';
    }

}
