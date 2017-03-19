/**
 * 
 */
package com.pokemongostats.view.commons;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.adapters.TypeAdapter;
import com.pokemongostats.view.listeners.SelectedVisitor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;

/**
 * @author Zapagon
 *
 */
public class ChooseTypeView extends RelativeLayout {

	private SelectedVisitor<Type> mCallbackType;

	private Type currentType;

	public ChooseTypeView(Context context, Type currentType,
			SelectedVisitor<Type> mCallbackType) {
		super(context);
		this.currentType = currentType;
		this.mCallbackType = mCallbackType;
		initializeViews(null);
	}

	public ChooseTypeView(Context context, AttributeSet attrs, Type currentType,
			SelectedVisitor<Type> mCallbackType) {
		super(context, attrs);
		this.currentType = currentType;
		this.mCallbackType = mCallbackType;
		initializeViews(attrs);
	}

	public ChooseTypeView(Context context, AttributeSet attrs, int defStyle,
			Type currentType, SelectedVisitor<Type> mCallbackType) {
		super(context, attrs, defStyle);
		this.currentType = currentType;
		this.mCallbackType = mCallbackType;
		initializeViews(attrs);
	}

	private void initializeViews(final AttributeSet attrs) {
		if (attrs != null) {
		}

		inflate(getContext(), R.layout.view_choose_type, this);
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));

		final ChooseTypeAdapter adapter = new ChooseTypeAdapter(getContext(),
				android.R.layout.simple_spinner_item, Type.values());
		GridView gv = new GridView(getContext());
		gv.setNumColumns(3);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position != AdapterView.INVALID_POSITION
					&& mCallbackType != null && adapter != null) {
					mCallbackType.select(adapter.getItem(position));
				}
			}
		});

		this.addView(gv);
	}

	private class ChooseTypeAdapter extends TypeAdapter {

		ChooseTypeAdapter(Context context, int textViewResourceId,
				Type[] list) {
			super(context, textViewResourceId, list);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public View getView(final int position, final View v,
				final ViewGroup parent) {
			View view = super.getView(position, v, parent);
			view.setPadding(20, 20, 20, 20);
			view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// already selected ?
					return (getItem(position) == currentType);
				}
			});
			return view;
		}
	}

}
