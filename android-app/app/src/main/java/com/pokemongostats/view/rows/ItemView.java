/**
 *
 */
package com.pokemongostats.view.rows;

import android.view.View;

/**
 * @author Zapagon
 */
public interface ItemView<T> {

    public View getView();

    public void update();

    public void updateWith(final T item);
}
