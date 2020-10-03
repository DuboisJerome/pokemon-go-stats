/**
 *
 */
package com.pokemongostats.view.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pokemongostats.R;
import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public final class PreferencesUtils implements Observable {

    private static final String SHARED_PREFERENCES_NAME = "PokemonGoHelperSharedPreference";
    private static final String STYLE = "STYLE";
    private static final String LAST_EVOLUTION_ONLY = "LAST_EVOLUTION_ONLY";
    private static final String WITH_MEGA_EVOLUTION = "WITH_MEGA_EVOLUTION";
    private static final String WITH_LEGENDARY = "WITH_LEGENDARY";

    private static final PreferencesUtils instance = new PreferencesUtils();

    /**
     * Util class not instanciate
     */
    private PreferencesUtils() {
    }

    public static PreferencesUtils getInstance() {
        return instance;
    }

    private SharedPreferences getSharedPreferences(final Context c) {
        return c.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public int getStyleId(final Context c) {
        return getSharedPreferences(c).getInt(STYLE, R.drawable.type_flat);
    }

    public void setStyleId(final Context c, final int styleId) {
        SharedPreferences.Editor editor = getSharedPreferences(c).edit();
        editor.putInt(STYLE, styleId);
        editor.apply();
        notifyObservers();
    }

    public boolean isLastEvolutionOnly(final Context c) {
        return getSharedPreferences(c).getBoolean(LAST_EVOLUTION_ONLY, true);
    }

    public void setLastEvolutionOnly(final Context c, final boolean isLastEvolutionOnly) {
        SharedPreferences.Editor editor = getSharedPreferences(c).edit();
        editor.putBoolean(LAST_EVOLUTION_ONLY, isLastEvolutionOnly);
        editor.apply();
        notifyObservers();
    }

    public boolean isWithMega(final Context c) {
        return getSharedPreferences(c).getBoolean(WITH_MEGA_EVOLUTION, false);
    }

    public void setWithMega(final Context c, final boolean b) {
        SharedPreferences.Editor editor = getSharedPreferences(c).edit();
        editor.putBoolean(WITH_MEGA_EVOLUTION, b);
        editor.apply();
        notifyObservers();
    }

    public boolean isWithLegendary(final Context c) {
        return getSharedPreferences(c).getBoolean(WITH_LEGENDARY, false);
    }

    public void setWithLegendary(final Context c, final boolean b) {
        SharedPreferences.Editor editor = getSharedPreferences(c).edit();
        editor.putBoolean(WITH_LEGENDARY, b);
        editor.apply();
        notifyObservers();
    }

    private final List<Observer> observers = new ArrayList<>();
    @Override
    public List<Observer> getObservers() {
        return observers;
    }
}
