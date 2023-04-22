package com.pokemongostats.controller.filters;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

import fr.commons.generique.controller.utils.TagUtils;

/**
 * Created by Zapagon on 11/04/2017.
 */
public class InputFilterMinMax implements InputFilter {

	private final Context context;
	private final double min;
	private final double max;

	public InputFilterMinMax(Context context, double min, double max) {
		this.context = context;
		this.min = min;
		this.max = max;
	}

	public InputFilterMinMax(Context context, String min, String max) {
		this.context = context;
		this.min = Double.parseDouble(min);
		this.max = Double.parseDouble(max);
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
		try {
			String newVal = dest.toString().substring(0, dstart) + dest.toString().substring(dend);
			// Add the new string in
			newVal = newVal.substring(0, dstart) + source.toString() + newVal.substring(dstart);
			double input = Double.parseDouble(newVal);
			if (isInRange(this.min, this.max, input)) {
				return null;
			}
		} catch (NumberFormatException nfe) {
			Log.e(TagUtils.DEBUG, "Error parsing double", nfe);
		}
		Toast.makeText(this.context, "La valeur doit etre entre " + this.min + " et " + this.max, Toast.LENGTH_SHORT).show();
		return "";
	}

	private boolean isInRange(double a, double b, double c) {
		return b > a ? c >= a && c <= b : c >= b && c <= a;
	}
}