/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.dto;

/**
 *
 * @author avillalobos
 */
public class EventRegistrationOptionDto {
    
    private String id;
    private String name;
    private String description;
    private double registrationCost;
    private String registrationCostCurrency;

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

    public double getRegistrationCost() {
        return registrationCost;
    }

    public void setRegistrationCost(double registrationCost) {
        this.registrationCost = registrationCost;
    }

    public String getRegistrationCostCurrency() {
        return registrationCostCurrency;
    }

    public void setRegistrationCostCurrency(String registrationCostCurrency) {
        this.registrationCostCurrency = registrationCostCurrency;
    }

    @Override
    public String toString() {
        return "EventRegistrationOptionDto{" + "id=" + id + ", name=" + name + 
                ", description=" + description + ", registrationCost=" + registrationCost + 
                ", registrationCostCurrency=" + registrationCostCurrency + '}';
    }

}
