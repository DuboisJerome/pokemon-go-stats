/**
 * 
 */
package com.pokemongostats.view.expandables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.view.rows.ItemView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Zapagon
 *
 */
public abstract class CustomExpandableList<T, V extends ItemView<T>> extends CustomExpandable implements Comparator<T> {

	protected List<T> mListItem = new ArrayList<T>();
	protected List<V> mListItemView = new ArrayList<V>();
	private int oddRowColor;
	private int evenRowColor;

	public CustomExpandableList(Context context) {
		super(context);
		oddRowColor = getResources().getColor(R.color.odd_row);
		evenRowColor = getResources().getColor(R.color.even_row);
	}

	public CustomExpandableList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomExpandableList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void add(T item) {
		add(item, null);
	}

	public void add(T item, OnClickListener clickListener) {
		V v = buildOrGetView();
		v.updateWith(item);

		mListItem.add(item);
		Collections.sort(mListItem, this);
		int index = mListItem.indexOf(item);

		View view = v.getView();
		view.setVisibility(isExpand() ? VISIBLE : GONE);
		view.setOnClickListener(clickListener);
		if (view.getParent() == null) {
			layout.addView(view, index);
		}

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
			int color = (i + 1) % 2 == 0 ? evenRowColor : oddRowColor;
			layout.getChildAt(i).setBackgroundColor(color);
		}
	}

	public void clear() {
		mListItem.clear();
	}

	protected abstract V buildView();

	protected V buildOrGetView() {
		final V v;
		if (mListItemView.size() > mListItem.size()) {
			// get last view available
			v = mListItemView.get(mListItem.size());
		} else {
			// build new view
			v = buildView();
			mListItemView.add(v);
		}
		return v;
	}
}
