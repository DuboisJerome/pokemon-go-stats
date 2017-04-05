package com.pokemongostats.controller.utils;

import java.util.Collection;

/**
 * Created by Zapagon on 05/04/2017.
 */

public class CollectionUtils {

    private CollectionUtils(){}

    public static boolean isEmpty(final Collection<?> c){
        return c == null || c.isEmpty();
    }
    public static boolean isNotEmpty(final Collection<?> c){
        return !isEmpty(c);
    }

}
