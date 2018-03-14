package com.ers.core.orm;
// Generated Mar 9, 2018 2:36:46 PM by Hibernate Tools 4.3.1

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 * EventRegistrationOption generated by hbm2java
 */
@Entity
@Table(name = "event_registration_option")
public class EventRegistrationOption implements java.io.Serializable {
    
    public enum Currency {
        CRC, USD
    }

    private String id;
    private Event event;
    private String name;
    private String description;
    private double registrationCost;
    private Currency registrationCostCurrency;

    public EventRegistrationOption() {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    public Event getEvent() {
        return this.event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Column(name = "name", nullable = false, length = 128)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description", nullable = false, length = 65535)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "registration_cost", nullable = false, precision = 22, scale = 0)
    public double getRegistrationCost() {
        return this.registrationCost;
    }

    public void setRegistrationCost(double registrationCost) {
        this.registrationCost = registrationCost;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "registration_cost_currency", nullable = false, length = 4)
    public Currency getRegistrationCostCurrency() {
        return this.registrationCostCurrency;
    }

    public void setRegistrationCostCurrency(Currency registrationCostCurrency) {
        this.registrationCostCurrency = registrationCostCurrency;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
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
        final EventRegistrationOption other = (EventRegistrationOption) obj;
        return Objects.equals(this.id, other.id);
    }

}
