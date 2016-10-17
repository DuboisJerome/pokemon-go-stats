/**
 * 
 */
package com.pokemongostats.view.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.pokemongostats.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public abstract class CustomExpandable<T> extends RelativeLayout
		implements
			Comparator<T> {

	protected TextView titleTextView;
	protected TextView leftInd;
	protected TextView rightInd;
	protected String title;
	protected LinearLayout layout;
	protected List<T> list = new ArrayList<T>();
	private boolean isExpand = false;
	private boolean keepExpand = false;

	private OnItemCallback<T> mOnClickItemCallback;

	private OnClickListener onClickExpandListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (isExpand() && !keepExpand) {
				retract();
			} else {
				expand();
			}
		}
	};

	public CustomExpandable(Context context) {
		super(context);
		initializeViews(context, null);
	}

	public CustomExpandable(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
		initializeViews(context, attrs);
	}

	public CustomExpandable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initializeViews(context, attrs);
	}

	private void initializeViews(Context context, AttributeSet attrs) {
		title = "";
		int titleStyle = -1;

		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					new int[]{R.attr.title, R.attr.titleStyle}, 0, 0);
			try {
				title = typedArray.getString(0);
				title = title == null ? "" : title;
				titleStyle = typedArray.getResourceId(1,
						android.R.style.TextAppearance_DeviceDefault);

			} finally {
				typedArray.recycle();
			}
		}

		LayoutInflater.from(context).inflate(R.layout.view_custom_expandable,
				this);

		// layout
		layout = (LinearLayout) this.findViewById(R.id.layout);
		layout.setOrientation(LinearLayout.VERTICAL);

		// title text view
		titleTextView = (TextView) this.findViewById(R.id.title);
		titleTextView.setText(title);
		titleTextView.setTextAppearance(context, titleStyle);
		titleTextView.setOnClickListener(onClickExpandListener);

		leftInd = (TextView) this.findViewById(R.id.left_indicator);
		leftInd.setTextAppearance(context, titleStyle);
		leftInd.setTextSize(25);
		leftInd.setText("+");
		leftInd.setOnClickListener(onClickExpandListener);

		rightInd = (TextView) this.findViewById(R.id.right_indicator);
		rightInd.setTextAppearance(context, titleStyle);
		rightInd.setTextSize(25);
		rightInd.setText("+");
		rightInd.setOnClickListener(onClickExpandListener);

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

	/**
	 * @return the keepExpand
	 */
	public boolean isKeepExpand() {
		return keepExpand;
	}

	/**
	 * @param keepExpand
	 *            the keepExpand to set
	 */
	public void setKeepExpand(boolean keepExpand) {
		this.keepExpand = keepExpand;
		int visibility = keepExpand ? View.GONE : View.VISIBLE;
		leftInd.setVisibility(visibility);
		rightInd.setVisibility(visibility);
	}

	public void retract() {
		if (!isExpand()) { return; }
		isExpand = false;
		leftInd.setText("+");
		rightInd.setText("+");
		changeItemsVisibility(GONE);
	}

	public void expand() {
		if (isExpand()) { return; }
		isExpand = true;
		leftInd.setText("-");
		rightInd.setText("-");
		changeItemsVisibility(VISIBLE);
	}

	private void changeItemsVisibility(int visibility) {
		for (int i = 0; i < layout.getChildCount(); i++) {
			layout.getChildAt(i).setVisibility(visibility);
		}
	}

	public boolean isExpand() {
		return isExpand;
	}

	public void add(T item) {
		list.add(item);
		Collections.sort(list, this);
		int index = list.indexOf(item);

		View v = buildView(item);
		v.setVisibility(isExpand ? VISIBLE : GONE);
		v.setOnClickListener(
				new OnClickItemListener<T>(mOnClickItemCallback, item));
		layout.addView(v, index);

		recolorEvenOddRows(index, layout.getChildCount());
	}

	public void remove(T item) {
		int index = list.indexOf(item);
		layout.removeViewAt(index);
		list.remove(index);
		recolorEvenOddRows(index, layout.getChildCount());
	}

	private void recolorEvenOddRows(int start, int end) {
		// repaint odd/even row
		for (int i = start; i < end; ++i) {
			int idColor = (i + 1) % 2 == 0 ? R.color.even_row : R.color.odd_row;
			layout.getChildAt(i)
					.setBackgroundColor(getResources().getColor(idColor));
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

	public abstract View buildView(T item);

}
