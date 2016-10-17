package com.pokemongostats.model.bean;

import java.io.Serializable;

public class Move implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4372310325501132352L;

	public static enum MoveType {
		QUICK, CHARGE;

		public static MoveType valueOfIgnoreCase(final String type) {
			if (type == null || type.isEmpty()) { return null; }
			return MoveType.valueOf(type.toUpperCase());
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
	public long getId() {
		return id;
	}
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the moveType
	 */
	public MoveType getMoveType() {
		return moveType;
	}
	/**
	 * @param moveType
	 *            the moveType to set
	 */
	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}
	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}
	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}
	/**
	 * @return the power
	 */
	public int getPower() {
		return power;
	}
	/**
	 * @param power
	 *            the power to set
	 */
	public void setPower(int power) {
		this.power = power;
	}
	/**
	 * @return the staminaLossScalar
	 */
	public double getStaminaLossScalar() {
		return staminaLossScalar;
	}
	/**
	 * @param staminaLossScalar
	 *            the staminaLossScalar to set
	 */
	public void setStaminaLossScalar(double staminaLossScalar) {
		this.staminaLossScalar = staminaLossScalar;
	}
	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}
	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	/**
	 * @return the energyDelta
	 */
	public int getEnergyDelta() {
		return energyDelta;
	}
	/**
	 * @param energyDelta
	 *            the energyDelta to set
	 */
	public void setEnergyDelta(int energyDelta) {
		this.energyDelta = energyDelta;
	}
	/**
	 * @return the criticalChance
	 */
	public double getCriticalChance() {
		return criticalChance;
	}
	/**
	 * @param criticalChance
	 *            the criticalChance to set
	 */
	public void setCriticalChance(double criticalChance) {
		this.criticalChance = criticalChance;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "#" + id + " : " + name + " [" + type.name() + " ]";
	}

}