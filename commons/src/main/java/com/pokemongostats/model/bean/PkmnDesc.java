package com.pokemongostats.model.bean;

import java.io.Serializable;

/**
 * Business object representing a description of a pokemon
 *
 * @author Zapagon
 *
 */
public class PkmnDesc implements HasID, Comparable<PkmnDesc>, Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2768420569277625730L;

	private long pokedexNum = HasID.NO_ID;

	private String forme = "NORMAL";

	private Type type1;

	private Type type2;

	private String name;

	private String family;

	private String description;

	private double kmsPerCandy;

	private double kmsPerEgg;

	private int physicalAttack;

	private int physicalDefense;

	private int specialAttack;

	private int specialDefense;

	private int pv;

	private int speed;

	/**
	 * @return the pokedexNum
	 */
	public long getPokedexNum() {
		return this.pokedexNum;
	}

	/**
	 * @param pokedexNum
	 *            the pokedexNum to set
	 */
	public void setPokedexNum(final long pokedexNum) {
		this.pokedexNum = pokedexNum;
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
	 * @return the family
	 */
	public String getFamily() {
		return this.family;
	}

	/**
	 * @param family
	 *            the family to set
	 */
	public void setFamily(final String family) {
		this.family = family;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return the type1
	 */
	public Type getType1() {
		return this.type1;
	}

	/**
	 * @param type1
	 *            the type1 to set
	 */
	public void setType1(final Type type1) {
		this.type1 = type1;
	}

	/**
	 * @return the type2
	 */
	public Type getType2() {
		return this.type2;
	}

	/**
	 * @param type2
	 *            the type2 to set
	 */
	public void setType2(final Type type2) {
		this.type2 = type2;
	}

	/**
	 * @return #000 : Name [Type1] OR #000 : Name [Type1|Type2]
	 */
	@Override
	public String toString() {
		return "#"
				+ this.pokedexNum
				+ " : "
				+ this.name
				+ (this.type1 == null
						? "" : " [" + this.type1.name() + (this.type2 == null ? "" : "|" + this.type2.name()) + "]");
	}

	/**
	 * @return pokedex number
	 */
	@Override
	public long getId() {
		return this.pokedexNum;
	}

	/**
	 * @param pokedex
	 *            number
	 */
	@Override
	public void setId(final long pokedexNum) {
		this.pokedexNum = pokedexNum;
	}

	/**
	 * @return the kmsPerCandy
	 */
	public double getKmsPerCandy() {
		return this.kmsPerCandy;
	}

	/**
	 * @param kmsPerCandy
	 *            the kmsPerCandy to set
	 */
	public void setKmsPerCandy(final double kmsPerCandy) {
		this.kmsPerCandy = kmsPerCandy;
	}

	/**
	 * @return the kmsPerEgg
	 */
	public double getKmsPerEgg() {
		return this.kmsPerEgg;
	}

	/**
	 * @param kmsPerEgg
	 *            the kmsPerEgg to set
	 */
	public void setKmsPerEgg(final double kmsPerEgg) {
		this.kmsPerEgg = kmsPerEgg;
	}

	/**
	 * @return the physicalAttack
	 */
	public int getPhysicalAttack() {
		return this.physicalAttack;
	}

	/**
	 * @param physicalAttack
	 *            the physicalAttack to set
	 */
	public void setPhysicalAttack(final int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}

	/**
	 * @return the physicalDefense
	 */
	public int getPhysicalDefense() {
		return this.physicalDefense;
	}

	/**
	 * @param physicalDefense
	 *            the physicalDefense to set
	 */
	public void setPhysicalDefense(final int physicalDefense) {
		this.physicalDefense = physicalDefense;
	}

	/**
	 * @return the specialAttack
	 */
	public int getSpecialAttack() {
		return this.specialAttack;
	}

	/**
	 * @param specialAttack
	 *            the specialAttack to set
	 */
	public void setSpecialAttack(final int specialAttack) {
		this.specialAttack = specialAttack;
	}

	/**
	 * @return the specialDefense
	 */
	public int getSpecialDefense() {
		return this.specialDefense;
	}

	/**
	 * @param specialDefense
	 *            the specialDefense to set
	 */
	public void setSpecialDefense(final int specialDefense) {
		this.specialDefense = specialDefense;
	}

	/**
	 * @return the pv
	 */
	public int getPv() {
		return this.pv;
	}

	/**
	 * @param pv
	 *            the pv to set
	 */
	public void setPv(final int pv) {
		this.pv = pv;
	}

	/**
	 * @return the forme
	 */
	public String getForme() {
		return this.forme;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return this.speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(final int speed) {
		this.speed = speed;
	}

	/**
	 * @param forme
	 *            the forme to set
	 */
	public void setForme(final String forme) {
		this.forme = forme;
	}

	public double getBaseAttack() {
		return getBaseAttack(isNerf());
	}

	private double getBaseAttack(final boolean isNerf) {
		return Math.round(getScaledAttack() * getSpeedMod() * (isNerf ? 0.91 : 1.0));
	}

	private double getScaledAttack() {
		final int higher = Math.max(this.physicalAttack, this.specialAttack);
		final int lower = Math.min(this.physicalAttack, this.specialAttack);
		final double higherScaled = ((7.0 / 8.0) * higher);
		final double lowerScaled = ((1.0 / 8.0) * lower);
		return Math.round(2.0 * (higherScaled + lowerScaled));
	}

	public double getBaseDefense() {
		return getBaseDefense(isNerf());
	}

	private double getBaseDefense(final boolean isNerf) {
		return Math.round(getScaledDefense() * getSpeedMod() * (isNerf ? 0.91 : 1.0));
	}

	private double getScaledDefense() {
		final int higher = Math.max(this.physicalDefense, this.specialDefense);
		final int lower = Math.min(this.physicalDefense, this.specialDefense);
		final double higherScaled = ((5.0 / 8.0) * higher);
		final double lowerScaled = ((3.0 / 8.0) * lower);
		return Math.round(2.0 * (higherScaled + lowerScaled));
	}

	public double getSpeedMod() {
		return 1.0 + ((this.speed - 75.0) / 500.0);
	}

	public double getBaseStamina() {
		return getBaseStamina(isNerf());
	}

	private double getBaseStamina(final boolean isNerf) {
		return Math.floor(((this.pv * 1.75) + 50.0) * (isNerf ? 0.91 : 1.0));
	}

	public double getMaxCP() {
		return isNerf() ? getMaxCPNerf() : getMaxCPNormal();
	}

	private boolean isNerf() {
		return getMaxCPNormal() > 4000.0;
	}

	private double getMaxCPNormal() {
		final double a = getBaseAttack(false) + 15.0;
		final double d = Math.pow((getBaseDefense(false) + 15.0), 0.5);
		final double s = Math.pow((getBaseStamina(false) + 15.0), 0.5);
		final double multiplier40 = Math.pow(0.7903001, 2.0);
		return (a * d * s * multiplier40) / 10.0;
	}

	private double getMaxCPNerf() {
		final double a = getBaseAttack(true) + 15.0;
		final double d = Math.pow((getBaseDefense(true) + 15.0), 0.5);
		final double s = Math.pow((getBaseStamina(true) + 15.0), 0.5);
		final double multiplier40 = Math.pow(0.7903001, 2.0);
		return (a * d * s * multiplier40) / 10.0;
	}

	/**
	 * @param other
	 * @return compare by pokedexNum
	 */
	@Override
	public int compareTo(final PkmnDesc other) {
		if (other == null || other.getPokedexNum() <= 0) { return 1; }
		if (getPokedexNum() <= 0) { return -1; }
		return new Long(this.pokedexNum).compareTo(new Long(other.getPokedexNum()));
	}

	@Override
	public boolean equals(final Object o) {
		if (o == null || !(o instanceof PkmnDesc)) { return false; }
		final PkmnDesc other = (PkmnDesc) o;
		return this.pokedexNum == other.pokedexNum;
	}

	@Override
	public int hashCode() {
		return (int) this.pokedexNum;
	}

}
