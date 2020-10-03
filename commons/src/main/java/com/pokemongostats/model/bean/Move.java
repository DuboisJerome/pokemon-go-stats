package com.pokemongostats.model.bean;

import java.io.Serializable;

public class Move implements Serializable, HasID, Comparable<Move> {

	/**
	 *
	 */
	private static final long serialVersionUID = -4372310325501132352L;

	public static enum MoveType {
		QUICK, CHARGE, UNKNOWN;

		public static MoveType valueOfIgnoreCase(final String type) {
			if (type == null || type.isEmpty()) { return null; }
			try {
				return MoveType.valueOf(type.toUpperCase());
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
				return UNKNOWN;
			}
		}
	}

	private long id;
	private String name;
	private MoveType moveType;
	private Type type;
	private int power;
	private double staminaLossScalar;

	/** duration in miliseconds */
	private int duration;
	private int energyDelta;
	private double criticalChance;

	/**
	 * @return the id
	 */
	@Override
	public long getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	@Override
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * @return the moveType
	 */
	public MoveType getMoveType() {
		return this.moveType;
	}

	/**
	 * @param moveType
	 *            the moveType to set
	 */
	public void setMoveType(final MoveType moveType) {
		this.moveType = moveType;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(final Type type) {
		this.type = type;
	}

	/**
	 * @return the power
	 */
	public int getPower() {
		return this.power;
	}

	/**
	 * @param power
	 *            the power to set
	 */
	public void setPower(final int power) {
		this.power = power;
	}

	/**
	 * @return the staminaLossScalar
	 */
	public double getStaminaLossScalar() {
		return this.staminaLossScalar;
	}

	/**
	 * @param staminaLossScalar
	 *            the staminaLossScalar to set
	 */
	public void setStaminaLossScalar(final double staminaLossScalar) {
		this.staminaLossScalar = staminaLossScalar;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return this.duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(final int duration) {
		this.duration = duration;
	}

	/**
	 * @return the energyDelta
	 */
	public int getEnergyDelta() {
		return this.energyDelta;
	}

	/**
	 * @param energyDelta
	 *            the energyDelta to set
	 */
	public void setEnergyDelta(final int energyDelta) {
		this.energyDelta = energyDelta;
	}

	/**
	 * @return the criticalChance
	 */
	public double getCriticalChance() {
		return this.criticalChance;
	}

	/**
	 * @param criticalChance
	 *            the criticalChance to set
	 */
	public void setCriticalChance(final double criticalChance) {
		this.criticalChance = criticalChance;
	}

	/**
	 * @return #000 : Name [Type]
	 */
	@Override
	public String toString() {
		return "#" + this.id + " : " + this.name + " [" + this.type.name() + "]";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int compareTo(final Move m) {
		if (m == null || this.name == null) { return 0; }
		return this.name.compareTo(m.getName());
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || !(o instanceof Move)) { return false; }
		final Move other = (Move) o;
		return this.id == other.id;
	}

	@Override
	public int hashCode() {
		return (int) this.id;
	}

}