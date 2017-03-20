/**
 * 
 */
package com.pokemongostats.view.adapters;

import java.util.List;

import com.pokemongostats.model.bean.Trainer;
import com.pokemongostats.view.commons.TeamUtils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Zapagon
 *
 */
public class TrainerAdapter extends ArrayAdapter<Trainer> {

	public TrainerAdapter(Context context, int textViewResourceId,
			List<Trainer> list) {
		super(context, textViewResourceId, list);
	}

	public TrainerAdapter(Context applicationContext,
			int simpleSpinnerItem) {
		super(applicationContext, simpleSpinnerItem);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView(int position, View v, ViewGroup parent) {
		TextView textView = (TextView) super.getView(position, v, parent);
		return getTextViewAtPosition(textView, position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getDropDownView(int position, View v, ViewGroup parent) {
		TextView textView = (TextView) super.getDropDownView(position, v,
				parent);
		return getTextViewAtPosition(textView, position);
	}

	/**
	 * Change text view color & text from Trainer at position
	 * 
	 * @param textView
	 * @param position
	 * @return textView
	 */
	private TextView getTextViewAtPosition(TextView textView, int position) {
		Trainer t = getItem(position);
		// color
		Integer idColor = TeamUtils.getTeamIdColor(t.getTeam());
		if (idColor != null) {
			textView.setTextColor(
					ContextCompat.getColor(getContext(), idColor));
		}
		// text
		textView.setText(t.getName() + " - " + t.getLevel());
		return textView;
	}

}