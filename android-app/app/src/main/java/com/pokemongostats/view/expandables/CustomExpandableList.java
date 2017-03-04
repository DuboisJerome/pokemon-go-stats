/**
 * 
 */
package com.pokemongostats.view.expandables;

import java.util.Comparator;

import com.pokemongostats.R;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @author Zapagon
 *
 */
public abstract class CustomExpandableList<T> extends CustomExpandable {

	private int oddRowColor;
	private int evenRowColor;
	protected OnItemClickListener<T> onItemClickListener;

	public CustomExpandableList(Context context) {
		super(context);
		init(context, null, 0);
	}

	public CustomExpandableList(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public CustomExpandableList(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	protected void init(Context context, AttributeSet attrs, int defStyle) {
		oddRowColor = ContextCompat.getColor(context, R.color.odd_row);
		evenRowColor = ContextCompat.getColor(context, R.color.even_row);
	}

	protected void recolorEvenOddRows(int start, int end) {
		// repaint odd/even row
		for (int i = start; i < end; ++i) {
			int color = (i + 1) % 2 == 0 ? evenRowColor : oddRowColor;
			layout.getChildAt(i).setBackgroundColor(color);
		}
	}

	@Override
	protected void refreshViews(){
		super.refreshViews();
		recolorEvenOddRows(0, layout.getChildCount());
	}

	/**
	 * 
	 * @param l
	 */
	public void setOnItemClickListener(final OnItemClickListener<T> l) {
		this.onItemClickListener = l;
	}

	/**
	 * 
	 * @return
	 */
	public OnItemClickListener<T> getOnItemClickListener() {
		return this.onItemClickListener;
	}

	public interface OnItemClickListener<T> {
		void onItemClick(T item);
	}

	@Override
	protected View getOrCreateChildView(final int position, View convertView) {
		final View view = super.getOrCreateChildView(position, convertView);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					try {
						@SuppressWarnings("unchecked")
						T item = (T) mAdapter.getItem(position);
						onItemClickListener.onItemClick(item);
					} catch (Exception e) {
						Log.e("STATE", e.getLocalizedMessage());
					}
				}
			}
		});
		return view;
	}
}
