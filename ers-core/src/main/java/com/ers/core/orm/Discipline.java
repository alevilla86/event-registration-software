/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.orm;
// Generated Mar 9, 2018 2:36:46 PM by Hibernate Tools 4.3.1

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * Discipline generated by hbm2java
 */
@Entity
@Table(name = "discipline")
public class Discipline implements java.io.Serializable {

    private int id;
    private String name;
    private List<UserProfile> userProfiles = new ArrayList<>(0);

    public Discipline() {
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name", nullable = false, length = 128)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "disciplines")
    public List<UserProfile> getUserProfiles() {
        return this.userProfiles;
    }

    public void setUserProfiles(List<UserProfile> userProfiles) {
        this.userProfiles = userProfiles;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Discipline other = (Discipline) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Discipline{" + "id=" + id + ", name=" + name + '}';
    }

}
