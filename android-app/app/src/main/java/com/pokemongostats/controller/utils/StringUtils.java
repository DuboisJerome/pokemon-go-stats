package com.pokemongostats.controller.utils;

/**
 * Created by Zapagon on 05/04/2017.
 */
public class StringUtils {

    private StringUtils(){}

    public static boolean isEmpty(final String s){
        return s == null || s.isEmpty();
    }

    public static boolean isNotEmpty(final String s){
        return !isEmpty(s);
    }
}
