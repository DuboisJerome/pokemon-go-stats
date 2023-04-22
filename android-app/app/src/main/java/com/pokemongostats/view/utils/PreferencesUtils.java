package com.pokemongostats.view.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pokemongostats.view.listeners.Observable;
import com.pokemongostats.view.listeners.Observer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zapagon
 */
public final class PreferencesUtils implements Observable {

	private static final String SHARED_PREFERENCES_NAME = "PokemonGoHelperSharedPreference";
	
	private static final String LAST_EVOLUTION_ONLY = "LAST_EVOLUTION_ONLY";
	private static final String WITH_MEGA_EVOLUTION = "WITH_MEGA_EVOLUTION";
	private static final String WITH_LEGENDARY = "WITH_LEGENDARY";

	private static final String SHARED_PREF_KEY = "SHARED_PREF_KEY";

	private static final PreferencesUtils instance = new PreferencesUtils();
	private SharedPreferences sp;

	public static PreferencesUtils getInstance() {
		return instance;
	}

	public static void initInstance(Context ctx) {
		instance.sp = ctx.getSharedPreferences(PreferencesUtils.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	private SharedPreferences getSharedPreferences() {
		return this.sp;
	}

	public boolean isLastEvolutionOnly() {
		return getSharedPreferences().getBoolean(LAST_EVOLUTION_ONLY, true);
	}

	public void setLastEvolutionOnly(boolean isLastEvolutionOnly) {
		SharedPreferences.Editor editor = getSharedPreferences().edit();
		editor.putBoolean(LAST_EVOLUTION_ONLY, isLastEvolutionOnly);
		editor.apply();
		notifyObservers();
	}

	public boolean isWithMega() {
		return getSharedPreferences().getBoolean(WITH_MEGA_EVOLUTION, true);
	}

	public void setWithMega(boolean b) {
		SharedPreferences.Editor editor = getSharedPreferences().edit();
		editor.putBoolean(WITH_MEGA_EVOLUTION, b);
		editor.apply();
		notifyObservers();
	}

	public boolean isWithLegendary() {
		return getSharedPreferences().getBoolean(WITH_LEGENDARY, true);
	}

	public void setWithLegendary(boolean b) {
		SharedPreferences.Editor editor = getSharedPreferences().edit();
		editor.putBoolean(WITH_LEGENDARY, b);
		editor.apply();
		notifyObservers();
	}

	public void addPrefToJson(JSONObject j) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put(WITH_LEGENDARY, isWithLegendary());
		obj.put(WITH_MEGA_EVOLUTION, isWithMega());
		obj.put(LAST_EVOLUTION_ONLY, isLastEvolutionOnly());
		j.put(SHARED_PREF_KEY, obj);
	}

	private final List<Observer> observers = new ArrayList<>();

	@Override
	public List<Observer> getObservers() {
		return this.observers;
	}
}