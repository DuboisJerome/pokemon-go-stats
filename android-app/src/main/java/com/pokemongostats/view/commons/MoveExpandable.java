/**
 * 
 */
package com.pokemongostats.view.commons;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PokemonDescription;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Zapagon
 *
 */
public class MoveExpandable extends CustomExpandable<Move> {

	public MoveExpandable(Context context) {
		super(context);
	}

	public MoveExpandable(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public MoveExpandable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public View buildView(Move m) {
		return MoveView.create(getContext(), m);
	}

	public void add(Move m, PokemonDescription p) {
		add(MoveView.create(getContext(), m, p), m);
	}

	@Override
	public int compare(Move m1, Move m2) {
		if (m1 == null && m2 == null) { return 0; }
		if (m2 == null) { return 1; }
		if (m1 == null) { return -1; }

		String nameM1 = m1.getName();
		String nameM2 = m2.getName();

		if (nameM1 == null && nameM2 == null) { return 0; }
		if (nameM2 == null) { return 1; }
		if (nameM1 == null) { return -1; }

		return nameM1.compareTo(nameM2);
	}
}
