/**
 * 
 */
package com.pokemongostats.view.adapters;

import java.util.List;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.view.commons.TypeViewUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * @author Zapagon
 *
 */
public class TypeArrayAdapter extends ArrayAdapter<Type> {

	public TypeArrayAdapter(Context context, int textViewResourceId,
			List<Type> list) {
		super(context, textViewResourceId, list);
	}

	public TypeArrayAdapter(Context applicationContext, int simpleSpinnerItem) {
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
		Type t = getItem(position);
		if (t == null) {
			t = Type.NORMAL;
		}
		return TypeViewUtils.createTypeView(getContext(), t);
	}

}