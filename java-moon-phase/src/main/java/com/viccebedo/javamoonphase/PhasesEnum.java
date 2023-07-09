/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.viccebedo.javamoonphase;

/**
 *
 * @author vcebedo
 */
public enum PhasesEnum {

    NEW_MOON("New Moon"),
    WAXING_CRESCENT("Waxing Crescent"),
    FIRST_QUARTER("First Quarter"),
    WAXING_GIBBOUS("Waxing Gibbous"),
    FULL_MOON("Full Moon"),
    WANING_GIBBOUS("Waning Gibbous"),
    THIRD_QUARTER("Third Quarter"),
    WANING_CRESCENT("Waning Crescent");

    private final String name;

    private PhasesEnum(String description) {
        this.name = description;
    }

    public String getName() {
        return name;
    }

}
