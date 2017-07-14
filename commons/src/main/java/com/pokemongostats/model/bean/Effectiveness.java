package com.pokemongostats.model.bean;

import static com.pokemongostats.controller.utils.EffectivenessUtils.EFF;

public enum Effectiveness {

	//
	REALLY_SUPER_EFFECTIVE(EFF * EFF, 2),
	//
	SUPER_EFFECTIVE(EFF, 1),
	//
	NORMAL(1, 0),
	//
	NOT_VERY_EFFECTIVE(1 / EFF, -1),
	//
	REALLY_NOT_VERY_EFFECTIVE(1 / (EFF * EFF), -2);

	private double multiplier;

	private int weight;

	private Effectiveness(final double multiplier, final int weight) {
		this.multiplier = multiplier;
		this.weight = weight;
	}

	/**
	 * @return multiplier
	 */
	public double getMultiplier() {
		return this.multiplier;
	}

	/**
	 * @return weight
	 */
	public int getWeight() {
		return this.weight;
	}

}
