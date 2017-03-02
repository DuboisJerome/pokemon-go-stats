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
public abstract class CustomExpandableList<T> extends CustomExpandable
		implements
			Comparator<T> {

	// protected List<T> mListItem = new ArrayList<T>();
	// protected List<V> mListItemView = new ArrayList<V>();
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

	// public void add(T item) {
	// add(item, null);
	// }
	//
	// public void add(T item, OnClickListener clickListener) {
	// V v = buildOrGetView();
	// v.updateWith(item);
	//
	// mListItem.add(item);
	// Collections.sort(mListItem, this);
	// int index = mListItem.indexOf(item);
	//
	// View view = v.getView();
	// view.setVisibility(isExpand() ? VISIBLE : GONE);
	// view.setOnClickListener(clickListener);
	// if (view.getParent() == null) {
	// layout.addView(view);
	// }
	//
	// recolorEvenOddRows(index, layout.getChildCount());
	// }

	// public void remove(T item) {
	// int index = mListItem.indexOf(item);
	// layout.removeViewAt(index);
	// mListItem.remove(index);
	// recolorEvenOddRows(index, layout.getChildCount());
	// }

	protected void recolorEvenOddRows(int start, int end) {
		// repaint odd/even row
		for (int i = start; i < end; ++i) {
			int color = (i + 1) % 2 == 0 ? evenRowColor : oddRowColor;
			layout.getChildAt(i).setBackgroundColor(color);
		}
	}

	// public void clear() {
	// mListItem.clear();
	// for (V v : mListItemView) {
	// v.getView().setVisibility(GONE);
	// }
	// }

	// @Override
	// protected void changeVisibleItemsVisibility(int visibility) {
	// for (int i = 0; i < mListItem.size(); i++) {
	// layout.getChildAt(i).setVisibility(visibility);
	// }
	// }

	// protected abstract V buildViewImpl();
	//
	// protected V buildOrGetView() {
	// final V v;
	// if (mListItemView.size() > mListItem.size()) {
	// // get last view available
	// v = mListItemView.get(mListItem.size());
	// } else {
	// // build new view
	// v = buildViewImpl();
	// mListItemView.add(v);
	// }
	// return v;
	// }

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
		public void onItemClick(T item);
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
