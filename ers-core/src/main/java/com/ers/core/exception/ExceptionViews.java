/*
 * Copyright (C) 2018-2019 ERS - Alejandro Villalobos Hernandez (alevilla86@hotmail.com). All rights reserved.
 */
package com.ers.core.exception;

/**
 * Jackson JSON Views.
 * They are "marker" classes for fields that should be included when serializing/deserializing objects.
 * 
 * @author avillalobos
 */
public class ExceptionViews {

    public static class Summary {
        // Nothing to do
    }

    public static class Details extends Summary {
        // Nothing to do
    }

    public static class DetailsCollections extends Details {
        // Nothing to do
    }
    
}
