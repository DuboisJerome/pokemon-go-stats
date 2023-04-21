package com.pokemongostats.view.utils;

import android.content.Context;

import com.pokemongostats.R;

public class InterfaceUtils {

	public static String toNoZeroRoundIntString(Context ctx, double d) {
		return (d > 0)
				? String.valueOf((int) d)
				: ctx.getString(R.string.unknown);
	}
}