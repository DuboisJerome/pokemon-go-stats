/**
 * 
 */
package com.pokemongostats.view.adapters;

import java.util.List;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.rows.TypeRowView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * @author Zapagon
 *
 */
public class TypeAdapter extends ArrayAdapter<Type> {

	public TypeAdapter(Context context, int textViewResourceId, Type[] list) {
		super(context, textViewResourceId, list);
	}

	public TypeAdapter(Context context, int textViewResourceId,
			List<Type> list) {
		super(context, textViewResourceId, list);
	}

	public TypeAdapter(Context applicationContext, int simpleSpinnerItem) {
		super(applicationContext, simpleSpinnerItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		return getTextViewAtPosition(position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int position, View v, ViewGroup parent) {
		return getTextViewAtPosition(position);
	}

	/**
	 * Change text view color & text from Trainer at position
	 * 
	 * @param textView
	 * @param position
	 * @return textView
	 */
	private View getTextViewAtPosition(int position) {
		Type type = getItem(position);
		if (type == null) {
			type = Type.NORMAL;
		}
		TypeRowView view = new TypeRowView(getContext());
		view.setType(type);
		return view;
	}
}