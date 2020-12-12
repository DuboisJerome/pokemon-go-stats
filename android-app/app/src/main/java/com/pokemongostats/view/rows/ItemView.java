package com.pokemongostats.view.rows;

import android.view.View;

/**
 * @author Zapagon
 */
public interface ItemView<T> {

    View getView();

    void update();

    void updateWith(final T item);
}
