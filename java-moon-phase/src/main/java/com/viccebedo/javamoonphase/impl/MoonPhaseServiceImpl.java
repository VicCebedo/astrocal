/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.viccebedo.javamoonphase.impl;

import com.viccebedo.javamoonphase.MoonPhaseService;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

/**
 *
 * @author vcebedo
 */
@Service
public class MoonPhaseServiceImpl implements MoonPhaseService {

    public static final double EPSILON = 0.000001;

    public static final String[] PHASE_NAMES = {
        "New Moon",
        "Waxing Crescent",
        "First Quarter",
        "Waxing Gibbous",
        "Full Moon",
        "Waning Gibbous",
        "Third Quarter",
        "Waning Crescent",
        "New Moon"
    };

    @Override
    public String getPhaseName(float phase) {
        return PHASE_NAMES[(int) Math.floor((phase + 0.0625) * 8)];
    }

    @Override
    public double getJulianFromUTC(double timestamp) {
        return timestamp / 86400 + 2440587.5;
    }

    @Override
    public float getPhaseByName(String name) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public double fixAngle(float angle) {
        return angle - 360 * Math.floor(angle / 360);
    }

    @Override
    public double kepler(double m, float ecc) {
        double e = m = Math.toRadians(m);

        double delta = e - ecc * Math.sin(e) - m;
        while (Math.abs(delta) > EPSILON) {
            e -= delta / (1 - ecc * Math.cos(e));
            delta = e - ecc * Math.sin(e) - m;
        }

        return e;
    }

    @Override
    public double meanPhase(int date, float k, float synmonth) {
        double jt = (date - 2415020.0) / 36525;
        double t2 = jt * jt;
        double t3 = t2 * jt;

        double nt1 = 2415020.75933 + synmonth * k
                + 0.0001178 * t2
                - 0.000000155 * t3
                + 0.00033 * Math.sin(Math.toRadians(166.56 + 132.87 * jt - 0.009173 * t2));

        return nt1;
    }

    @Override
    public double truePhase(double k, double phase, float synmonth) {
        boolean apcor = false;

        // Add phase to new moon time
        k += phase;

        // Time in Julian centuries from 1900 January 0.5
        double t = k / 1236.85;

        // Square for frequent use
        double t2 = t * t;

        // Cube for frequent use
        double t3 = t2 * t;

        // Mean time of phase
        double pt = 2_415_020.75933
                + synmonth * k
                + 0.0001178 * t2
                - 0.000000155 * t3
                + 0.00033 * Math.sin(Math.toRadians(166.56 + 132.87 * t - 0.009173 * t2));

        // Sun's mean anomaly
        double m = 359.2242 + 29.10535608 * k - 0.0000333 * t2 - 0.00000347 * t3;

        // Moon's mean anomaly
        double mprime = 306.0253 + 385.81691806 * k + 0.0107306 * t2 + 0.00001236 * t3;

        // Moon's argument of latitude
        double f = 21.2964 + 390.67050646 * k - 0.0016528 * t2 - 0.00000239 * t3;

        if (phase < 0.01 || Math.abs(phase - 0.5) < 0.01) {
            // Corrections for New and Full Moon
            pt += (0.1734 - 0.000393 * t) * Math.sin(Math.toRadians(m))
                    + 0.0021 * Math.sin(Math.toRadians(2 * m))
                    - 0.4068 * Math.sin(Math.toRadians(mprime))
                    + 0.0161 * Math.sin(Math.toRadians(2 * mprime))
                    - 0.0004 * Math.sin(Math.toRadians(3 * mprime))
                    + 0.0104 * Math.sin(Math.toRadians(2 * f))
                    - 0.0051 * Math.sin(Math.toRadians(m + mprime))
                    - 0.0074 * Math.sin(Math.toRadians(m - mprime))
                    + 0.0004 * Math.sin(Math.toRadians(2 * f + m))
                    - 0.0004 * Math.sin(Math.toRadians(2 * f - m))
                    - 0.0006 * Math.sin(Math.toRadians(2 * f + mprime))
                    + 0.0010 * Math.sin(Math.toRadians(2 * f - mprime))
                    + 0.0005 * Math.sin(Math.toRadians(m + 2 * mprime));

            apcor = true;
        } else if (Math.abs(phase - 0.25) < 0.01 || Math.abs(phase - 0.75) < 0.01) {
            pt += (0.1721 - 0.0004 * t) * Math.sin(Math.toRadians(m))
                    + 0.0021 * Math.sin(Math.toRadians(2 * m))
                    - 0.6280 * Math.sin(Math.toRadians(mprime))
                    + 0.0089 * Math.sin(Math.toRadians(2 * mprime))
                    - 0.0004 * Math.sin(Math.toRadians(3 * mprime))
                    + 0.0079 * Math.sin(Math.toRadians(2 * f))
                    - 0.0119 * Math.sin(Math.toRadians(m + mprime))
                    - 0.0047 * Math.sin(Math.toRadians(m - mprime))
                    + 0.0003 * Math.sin(Math.toRadians(2 * f + m))
                    - 0.0004 * Math.sin(Math.toRadians(2 * f - m))
                    - 0.0006 * Math.sin(Math.toRadians(2 * f + mprime))
                    + 0.0021 * Math.sin(Math.toRadians(2 * f - mprime))
                    + 0.0003 * Math.sin(Math.toRadians(m + 2 * mprime))
                    + 0.0004 * Math.sin(Math.toRadians(m - 2 * mprime))
                    - 0.0003 * Math.sin(Math.toRadians(2 * m + mprime));

            // First and last quarter corrections
            if (phase < 0.5) {
                pt += 0.0028 - 0.0004 * Math.cos(Math.toRadians(m)) + 0.0003 * Math.cos(Math.toRadians(mprime));
            } else {
                pt += -0.0028 + 0.0004 * Math.cos(Math.toRadians(m)) - 0.0003 * Math.cos(Math.toRadians(mprime));
            }

            apcor = true;
        }

        return apcor ? pt : null;
    }

    @Override
    public void phaseHunt(double timestamp, double synmonth) {
        double sdate = getJulianFromUTC(timestamp);
        double adate = sdate - 45;
        double ats = timestamp - 86400 * 45;
        int yy = LocalDate.ofEpochDay((long) (ats / 86400)).getYear();
        int mm = LocalDate.ofEpochDay((long) (ats / 86400)).getMonthValue();

        int k1 = (int) Math.floor((yy + ((mm - 1) * (1.0 / 12.0)) - 1900) * 12.3685);
        adate = nt1 = meanPhase((int) adate, k1, synmonth);

        while (true) {
            adate += synmonth;
            int k2 = k1 + 1;
            double nt2 = meanPhase((int) adate, k2);

            // If nt2 is close to sdate, then mean phase isn't good enough, we have to be more accurate
            if (Math.abs(nt2 - sdate) < 0.75) {
                nt2 = truePhase(k2, 0.0);
            }

            if (nt1 <= sdate && nt2 > sdate) {
                break;
            }

            nt1 = nt2;
            k1 = k2;
        }

        // Results in Julian dates
        double[] dates = new double[]{
            truePhase(k1, 0.0),
            truePhase(k1, 0.25),
            truePhase(k1, 0.5),
            truePhase(k1, 0.75),
            truePhase(k2, 0.0),
            truePhase(k2, 0.25),
            truePhase(k2, 0.5),
            truePhase(k2, 0.75)
        };

        quarters = new double[dates.length];

        for (int i = 0; i < dates.length; i++) {
            // Convert to UNIX time
            quarters[i] = (dates[i] - 2_440_587.5) * 86400;
        }
    }

}
