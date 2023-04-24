package com.pokemongostats.view.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.pokemongostats.controller.utils.PkmnTags;
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
	private static final String ONLY_IN_GAME = "ONLY_IN_GAME";

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
		return isWithTag(LAST_EVOLUTION_ONLY);
	}

	public void setLastEvolutionOnly(boolean b) {
		setWithTag(LAST_EVOLUTION_ONLY, b);
	}

	public boolean isOnlyInGame() {
		return isWithTag(ONLY_IN_GAME);
	}

	public void setOnlyInGame(boolean b){
		setWithTag(ONLY_IN_GAME, b);
	}

	public boolean isWithTag(String tag) {
		return getSharedPreferences().getBoolean(tag, true);
	}

	public void setWithTag(String tag, boolean b) {
		SharedPreferences.Editor editor = getSharedPreferences().edit();
		editor.putBoolean(tag, b);
		editor.apply();
		notifyObservers();
	}

	public void addPrefToJson(JSONObject j) throws JSONException {
		JSONObject obj = new JSONObject();
		putPrefTagToJson(obj, PkmnTags.LEGENDAIRE);
		putPrefTagToJson(obj, PkmnTags.MYTHIQUE);
		putPrefTagToJson(obj, PkmnTags.ULTRA_CHIMERE);
		putPrefTagToJson(obj, PkmnTags.MEGA);
		putPrefTagToJson(obj, PkmnTags.NOT_IN_GAME);
		obj.put(LAST_EVOLUTION_ONLY, isLastEvolutionOnly());
		j.put(SHARED_PREF_KEY, obj);
	}

	public void putPrefTagToJson(JSONObject obj, String tag) throws JSONException {
		obj.put(tag, isWithTag(tag));
	}


	private final List<Observer> observers = new ArrayList<>();

	@Override
	public List<Observer> getObservers() {
		return this.observers;
	}
}