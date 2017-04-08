package com.pokemongostats.model.bean.fight;

import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.Pkmn;

/**
 * 
 * @author Zapagon
 *
 */
public class Attacker extends Fighter {
	public Attacker(final Pkmn att) {
		super(att);
		this.hp = FightUtils.computeHP(att);
	}

	@Override
	protected double getEnergyMax() {
		return 100;
	}
}
