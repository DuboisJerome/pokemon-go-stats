package com.pokemongostats.model.bean;

import static com.pokemongostats.controller.utils.EffectivenessUtils.EFF;

public enum Effectiveness {

	//
	SUPER_EFFECTIVE(EFF),
	//
	NORMAL(1),
	//
	NOT_VERY_EFFECTIVE(1 / EFF),
	//
	IMMUNE(1 / (EFF * EFF));

	private double multiplier;

	private Effectiveness(final double multiplier) {
		this.multiplier = multiplier;
	}

	/**
	 * @return multiplier
	 */
	public double getMultiplier() {
		return this.multiplier;
	}

}
