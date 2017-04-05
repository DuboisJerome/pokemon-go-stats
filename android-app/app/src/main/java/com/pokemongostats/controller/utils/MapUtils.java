package com.pokemongostats.controller.utils;

import java.util.Map;

/**
 * Created by Zapagon on 05/04/2017.
 */
public class MapUtils {

    private MapUtils() {
    }

    public static boolean isEmpty(final Map<?, ?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isNotEmpty(final Map<?, ?> c) {
        return !isEmpty(c);
    }

}
