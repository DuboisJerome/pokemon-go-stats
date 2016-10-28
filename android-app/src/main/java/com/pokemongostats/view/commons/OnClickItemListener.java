/**
 * 
 */
package com.pokemongostats.view.commons;

import com.pokemongostats.view.listeners.SelectedVisitor;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author Zapagon
 *
 */
public class OnClickItemListener<T> implements OnClickListener {
	private final T item;
	private final SelectedVisitor<T> mHandler;

	public OnClickItemListener(final SelectedVisitor<T> mHandler,
			final T item) {
		this.item = item;
		this.mHandler = mHandler;
	}

	@Override
	public void onClick(View v) {
		if (mHandler == null || item == null) { return; }
		mHandler.select(item);
	}
}
