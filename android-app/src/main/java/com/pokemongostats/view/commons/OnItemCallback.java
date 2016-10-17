/**
 * 
 */
package com.pokemongostats.view.commons;

import android.view.View;

/**
 * @author Zapagon
 *
 */
public interface OnItemCallback<T> {
	public void onItem(View v, T item);
}
