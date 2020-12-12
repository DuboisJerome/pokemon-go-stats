package com.pokemongostats.controller.utils;

/**
 * Math utilities
 * Created by Zapagon on 31/03/2017.
 */
public final class MathUtils {

    private MathUtils() {
    }

    public static double round(double d, int nbDecimals) {
        double multiplier = Math.pow(10d, nbDecimals);
        return Math.floor(d * multiplier) / multiplier;
    }
}
