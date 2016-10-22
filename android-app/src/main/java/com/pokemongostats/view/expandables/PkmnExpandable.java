/**
 * 
 */
package com.pokemongostats.view.expandables;

import com.pokemongostats.model.bean.PokemonDescription;
import com.pokemongostats.view.rows.PkmnDescRowView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Zapagon
 *
 */
public class PkmnExpandable extends CustomExpandableList<PokemonDescription> {

	public PkmnExpandable(Context context) {
		super(context);
	}

	public PkmnExpandable(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public PkmnExpandable(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public View buildView(PokemonDescription p) {
		return PkmnDescRowView.create(getContext(), p);
	}

	@Override
	public int compare(PokemonDescription p1, PokemonDescription p2) {
		if (p1 == null && p2 == null) {
			return 0;
		}
		if (p2 == null) {
			return 1;
		}
		if (p1 == null) {
			return -1;
		}

		String nameP1 = p1.getName();
		String nameP2 = p2.getName();

		if (nameP1 == null && nameP2 == null) {
			return 0;
		}
		if (nameP2 == null) {
			return 1;
		}
		if (nameP1 == null) {
			return -1;
		}

		return nameP1.compareTo(nameP2);
	}
}
