/**
 * 
 */
package com.pokemongostats.view.adapters;

import java.util.List;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.view.rows.MoveRowView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * @author Zapagon
 *
 */
public class MoveAdapter extends ArrayAdapter<Move> {

	public MoveAdapter(Context context, int textViewResourceId, Move[] list) {
		super(context, textViewResourceId, list);
	}

	public MoveAdapter(Context context, int textViewResourceId,
			List<Move> list) {
		super(context, textViewResourceId, list);
	}

	public MoveAdapter(Context applicationContext, int simpleSpinnerItem) {
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
	 * @param textView
	 * @param position
	 * @return textView
	 */
	private View getTextViewAtPosition(int position) {
		Move move = getItem(position);
		if (move == null) { return null; }
		MoveRowView view = new MoveRowView(getContext());
		view.setMove(move);
		return view;
	}
}