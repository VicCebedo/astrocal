/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.viccebedo.javamoonphase;

/**
 *
 * @author vcebedo
 */
public interface MoonPhaseService {

    double fixAngle(float angle);

    double kepler(double m, float ecc);

    double meanPhase(int date, float k, float synmonth);

    double truePhase(double k, double phase, float synmonth);

    void phaseHunt();

    double getJulianFromUTC(double timestamp, float synmonth);

    float getPhaseByName(String name);

    String getPhaseName(float phase);

}
