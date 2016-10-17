/**
 * 
 */
package com.pokemongostats.view.commons;

import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author Zapagon
 *
 */
public class OnClickItemListener<T> implements OnClickListener {

	private final T item;

	private final OnItemCallback<T> mOnItemCallback;

	public OnClickItemListener(final OnItemCallback<T> onItemCallback,
			final T item) {
		this.item = item;
		this.mOnItemCallback = onItemCallback;
	}

	@Override
	public void onClick(View v) {
		if (mOnItemCallback == null || v == null || item == null) { return; }
		mOnItemCallback.onItem(v, item);
	}
}
