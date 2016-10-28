/**
 * 
 */
package com.pokemongostats.view.expandables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.pokemongostats.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Zapagon
 *
 */
public abstract class CustomExpandableList<T> extends CustomExpandable
		implements
			Comparator<T> {

	protected List<T> mListItem = new ArrayList<T>();

	public CustomExpandableList(Context context) {
		super(context);
	}

	public CustomExpandableList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomExpandableList(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public void add(T item) {
		add(buildView(item), item, null);
	}

	public void add(T item, OnClickListener clickListener) {
		View v = buildView(item);
		add(v, item, clickListener);
	}

	protected void add(View v, T item, OnClickListener clickListener) {
		v.setVisibility(isExpand() ? VISIBLE : GONE);
		v.setOnClickListener(clickListener);

		mListItem.add(item);
		Collections.sort(mListItem, this);
		int index = mListItem.indexOf(item);
		layout.addView(v, index);

		recolorEvenOddRows(index, layout.getChildCount());
	}

	public void remove(T item) {
		int index = mListItem.indexOf(item);
		layout.removeViewAt(index);
		mListItem.remove(index);
		recolorEvenOddRows(index, layout.getChildCount());
	}

	protected void recolorEvenOddRows(int start, int end) {
		// repaint odd/even row
		for (int i = start; i < end; ++i) {
			int idColor = (i + 1) % 2 == 0 ? R.color.even_row : R.color.odd_row;
			layout.getChildAt(i)
					.setBackgroundColor(getResources().getColor(idColor));
		}
	}

	public void clear() {
		layout.removeAllViews();
		mListItem.clear();
	}

	protected abstract View buildView(T item);
}
