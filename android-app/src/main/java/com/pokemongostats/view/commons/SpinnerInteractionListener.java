package com.pokemongostats.view.commons;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

public abstract class SpinnerInteractionListener
		implements
			AdapterView.OnItemSelectedListener,
			View.OnTouchListener {

	boolean userSelect = false;

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		userSelect = true;
		return false;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (pos != AdapterView.INVALID_POSITION) {
			if (userSelect) {
				onItemSelectedFromUser(parent, view, pos, id);
			} else {
				onItemSelectedFromCode(parent, view, pos, id);
			}
		}
		userSelect = false;
	}

	protected abstract void onItemSelectedFromCode(AdapterView<?> parent,
			View view, int pos, long id);

	protected abstract void onItemSelectedFromUser(AdapterView<?> parent,
			View view, int pos, long id);

}