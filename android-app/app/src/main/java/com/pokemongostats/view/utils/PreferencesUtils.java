/**
 *
 */
package com.pokemongostats.view.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 */
public final class PreferencesUtils {

    private static final String SHARED_PREFERENCES_NAME = "PokemonGoHelperSharedPreference";
    /**
     * style preference persistance
     */
    private static final String STYLE = "STYLE";
    /**
     * style preference persistance
     */
    private static final String LAST_EVOLUTION_ONLY = "LAST_EVOLUTION_ONLY";

    /**
     * Util class not instanciate
     */
    private PreferencesUtils() {
    }

    public static SharedPreferences getSharedPreferences(final Context c) {
        return c.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
    }

    public static int getStyleId(final Context c) {
        return getSharedPreferences(c).getInt(STYLE, R.drawable.type_flat);
    }

    public static void setStyleId(final Context c, final int styleId) {
        SharedPreferences.Editor editor = getSharedPreferences(c).edit();
        editor.putInt(STYLE, styleId);
        editor.commit();
    }

    public static boolean isLastEvolutionOnly(final Context c) {
        return getSharedPreferences(c).getBoolean(LAST_EVOLUTION_ONLY, true);
    }

    public static void setLastEvolutionOnly(final Context c, final boolean isLastEvolutionOnly) {
        SharedPreferences.Editor editor = getSharedPreferences(c).edit();
        editor.putBoolean(LAST_EVOLUTION_ONLY, isLastEvolutionOnly);
        editor.commit();
    }

    public static int getColorId(final Type type) {
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
