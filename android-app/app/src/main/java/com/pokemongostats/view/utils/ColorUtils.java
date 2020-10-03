package com.pokemongostats.view.utils;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;

/**
 * Created by Zapagon on 15/04/2017.
 * Color utilities
 */
public class ColorUtils {

    public static int getTypeColor(final Type type) {
        if (type == null) {
            return R.color.no_type_bg;
        }
        switch (type) {
            case BUG:
                return R.color.bug_bg;
            case DARK:
                return R.color.dark_bg;
            case DRAGON:
                return R.color.dragon_bg;
            case ELECTRIC:
                return R.color.electric_bg;
            case FAIRY:
                return R.color.fairy_bg;
            case FIGHTING:
                return R.color.fighting_bg;
            case FIRE:
                return R.color.fire_bg;
            case FLYING:
                return R.color.flying_bg;
            case GHOST:
                return R.color.ghost_bg;
            case GRASS:
                return R.color.grass_bg;
            case GROUND:
                return R.color.ground_bg;
            case ICE:
                return R.color.ice_bg;
            case POISON:
                return R.color.poison_bg;
            case PSYCHIC:
                return R.color.psychic_bg;
            case ROCK:
                return R.color.rock_bg;
            case STEEL:
                return R.color.steel_bg;
            case WATER:
                return R.color.water_bg;
            case NORMAL:
                return R.color.normal_bg;
            default:
                return R.color.no_type_bg;
        }
    }
}
