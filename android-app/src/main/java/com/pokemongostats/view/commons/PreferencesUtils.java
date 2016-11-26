/**
 * 
 */
package com.pokemongostats.view.commons;

import com.pokemongostats.R;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Zapagon
 *
 */
public final class PreferencesUtils {

	/** Util class not instanciate */
	private PreferencesUtils() {}

	private static final String SHARED_PREFERENCES_NAME = "PokemonGoHelperSharedPreference";

	/** style preference persistance */
	private static final String STYLE = "STYLE";

	/** style preference persistance */
	private static final String LAST_EVOLUTION_ONLY = "LAST_EVOLUTION_ONLY";

	public static SharedPreferences getSharedPreferences(final Context c) {
		return c.getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
	}

	public static int getStyleId(final Context c) {
		return getSharedPreferences(c).getInt(STYLE, R.string.style_round);
	}

	public static void setStyleId(final Context c, final int styleId) {
		SharedPreferences.Editor editor = getSharedPreferences(c).edit();
		editor.putInt(STYLE, styleId);
		editor.commit();
	}

	public static boolean isLastEvolutionOnly(final Context c) {
		return getSharedPreferences(c).getBoolean(LAST_EVOLUTION_ONLY, false);
	}

	public static void setLastEvolutionOnly(final Context c, final boolean isLastEvolutionOnly) {
		SharedPreferences.Editor editor = getSharedPreferences(c).edit();
		editor.putBoolean(LAST_EVOLUTION_ONLY, isLastEvolutionOnly);
		editor.commit();
	}

}
