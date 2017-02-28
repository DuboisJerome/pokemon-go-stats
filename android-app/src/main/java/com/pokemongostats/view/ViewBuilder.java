/**
 * 
 */
package com.pokemongostats.view;

import android.view.View;

/**
 * @author Zapagon
 *
 */
public interface ViewBuilder<T extends View> {
	public T buildView();
}
