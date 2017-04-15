package com.pokemongostats.controller.utils;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Team;

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

    public static int getTeamStringId(final Team team){
        if (team == null){
            return R.string.none;
        }
        switch (team){
            case INSTINCT:
                return R.string.instinct;
            case MYSTIC:
                return R.string.mystic;
            case VALOR:
                return R.string.valor;
            default:
                return R.string.none;
        }
    }
}
