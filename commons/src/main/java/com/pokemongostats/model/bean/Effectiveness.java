package com.pokemongostats.model.bean;

public enum Effectiveness {

	//
	REALLY_SUPER_EFFECTIVE(1.25 * 1.25, 2),
	//
	SUPER_EFFECTIVE(1.25, 1),
	//
	NORMAL(1, 0),
	//
	NOT_VERY_EFFECTIVE(0.8, -1),
	//
	REALLY_NOT_VERY_EFFECTIVE(0.8 * 0.8, -2);

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
