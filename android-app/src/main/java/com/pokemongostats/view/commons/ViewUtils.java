package com.pokemongostats.view.commons;

import com.pokemongostats.R;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout.LayoutParams;

/**
 * 
 * @author Zapagon
 *
 */
public class ViewUtils {

	private ViewUtils() {
	}

	/**
	 * @param c
	 * @return a horizontal separator
	 */
	public static View createHorizontalSeparator(final Context c) {
		View v = new View(c);
		v.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 1));
		v.setBackgroundColor(c.getResources().getColor(R.color.separator));
		return v;
	}
}
