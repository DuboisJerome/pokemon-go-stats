/**
 * 
 */
package com.pokemongostats.view.expandables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.pokemongostats.R;
import com.pokemongostats.view.commons.OnClickItemListener;
import com.pokemongostats.view.commons.OnItemCallback;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Zapagon
 *
 */
public abstract class CustomExpandableList<T> extends CustomExpandable implements Comparator<T> {

	protected List<T> list = new ArrayList<T>();

	private OnItemCallback<T> mOnClickItemCallback;

	public CustomExpandableList(Context context) {
		super(context);
	}

	public CustomExpandableList(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomExpandableList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * @return the onClickItemListener
	 */
	public OnItemCallback<T> getOnClickItemListener() {
		return mOnClickItemCallback;
	}

	/**
	 * @param onClickItemListener
	 *            the onClickItemListener to set
	 */
	public void setOnClickItemListener(OnItemCallback<T> onClickItemListener) {
		this.mOnClickItemCallback = onClickItemListener;
	}

	public void add(T item) {
		add(buildView(item), item);
	}

	public void add(View v, T item) {
		v.setVisibility(isExpand() ? VISIBLE : GONE);
		v.setOnClickListener(new OnClickItemListener<T>(mOnClickItemCallback, item));

		list.add(item);
		Collections.sort(list, this);
		int index = list.indexOf(item);
		layout.addView(v, index);

		recolorEvenOddRows(index, layout.getChildCount());
	}

	public void remove(T item) {
		int index = list.indexOf(item);
		layout.removeViewAt(index);
		list.remove(index);
		recolorEvenOddRows(index, layout.getChildCount());
	}

	protected void recolorEvenOddRows(int start, int end) {
		// repaint odd/even row
		for (int i = start; i < end; ++i) {
			int idColor = (i + 1) % 2 == 0 ? R.color.even_row : R.color.odd_row;
			layout.getChildAt(i).setBackgroundColor(getResources().getColor(idColor));
		}
	}

	public void addAll(Collection<? extends T> col) {
		for (T t : col) {
			add(t);
		}
	}

	public void clear() {
		layout.removeAllViews();
		list.clear();
	}

	protected abstract View buildView(T item);

}
