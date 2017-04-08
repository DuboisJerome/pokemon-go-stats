package com.pokemongostats.model.bean.fight;

import com.pokemongostats.model.bean.Move;

public interface OnAttackListener {
	public void onAttack(final int time, final Fighter att, final Fighter def,
			final Move m);
}
