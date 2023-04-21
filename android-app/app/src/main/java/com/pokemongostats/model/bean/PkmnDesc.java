package com.pokemongostats.model.bean;

import com.pokemongostats.model.comparators.CheckNullComparator;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

import fr.commons.generique.model.db.AbstractObjetBddAvecId;
import fr.commons.generique.model.db.IObjetBdd;

/**
 * Business object representing a description of a pokemon
 *
 * @author Zapagon
 */
public class PkmnDesc extends AbstractObjetBddAvecId implements Comparable<PkmnDesc>, Serializable, IObjetBdd {

	/**
	 *
	 */
	private static final long serialVersionUID = -2768420569277625730L;

	public static final String DEFAULT_FORM = "NORMAL";
	private String form = DEFAULT_FORM;

	private Type type1;

	private Type type2;

	private String name;

	private String family;

	private String description;

	private double kmsPerCandy;

	private double kmsPerEgg;

	private boolean isLegendary;

	// 6 stats
	private int physicalAttack;
	private int physicalDefense;
	private int specialAttack;
	private int specialDefense;
	private int pv;
	private int speed;

	// 3 stats
	private int stamina = -1;
	private int attack = -1;
	private int defense = -1;

	/**
	 * @return the pokedexNum
	 */
	public long getPokedexNum() {
		return getId();
	}

	/**
	 * @param pokedexNum the pokedexNum to set
	 */
	public void setPokedexNum(long pokedexNum) {
		setId(pokedexNum);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the family
	 */
	public String getFamily() {
		return this.family;
	}

	/**
	 * @param family the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the type1
	 */
	public Type getType1() {
		return this.type1;
	}

	/**
	 * @param type1 the type1 to set
	 */
	public void setType1(Type type1) {
		this.type1 = type1;
	}

	/**
	 * @return the type2
	 */
	public Type getType2() {
		return this.type2;
	}

	/**
	 * @param type2 the type2 to set
	 */
	public void setType2(Type type2) {
		this.type2 = type2;
	}

	/**
	 * @return #000 : Name [Type1] OR #000 : Name [Type1|Type2]
	 */
	@Override
	public String toString() {
		return "#"
				+ this.id
				+ " : "
				+ this.name
				+ (this.type1 == null
				? "" : " [" + this.type1.name() + (this.type2 == null ? "" : "|" + this.type2.name()) + "]");
	}

	/**
	 * @return the kmsPerCandy
	 */
	public double getKmsPerCandy() {
		return this.kmsPerCandy;
	}

	/**
	 * @param kmsPerCandy the kmsPerCandy to set
	 */
	public void setKmsPerCandy(double kmsPerCandy) {
		this.kmsPerCandy = kmsPerCandy;
	}

	/**
	 * @return the kmsPerEgg
	 */
	public double getKmsPerEgg() {
		return this.kmsPerEgg;
	}

	/**
	 * @param kmsPerEgg the kmsPerEgg to set
	 */
	public void setKmsPerEgg(double kmsPerEgg) {
		this.kmsPerEgg = kmsPerEgg;
	}

	/**
	 * @return the physicalAttack
	 */
	public int getPhysicalAttack() {
		return this.physicalAttack;
	}

	/**
	 * @param physicalAttack the physicalAttack to set
	 */
	public void setPhysicalAttack(int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}

	/**
	 * @return the physicalDefense
	 */
	public int getPhysicalDefense() {
		return this.physicalDefense;
	}

	/**
	 * @param physicalDefense the physicalDefense to set
	 */
	public void setPhysicalDefense(int physicalDefense) {
		this.physicalDefense = physicalDefense;
	}

	/**
	 * @return the specialAttack
	 */
	public int getSpecialAttack() {
		return this.specialAttack;
	}

	/**
	 * @param specialAttack the specialAttack to set
	 */
	public void setSpecialAttack(int specialAttack) {
		this.specialAttack = specialAttack;
	}

	/**
	 * @return the specialDefense
	 */
	public int getSpecialDefense() {
		return this.specialDefense;
	}

	/**
	 * @param specialDefense the specialDefense to set
	 */
	public void setSpecialDefense(int specialDefense) {
		this.specialDefense = specialDefense;
	}

	/**
	 * @return the pv
	 */
	public int getPv() {
		return this.pv;
	}

	/**
	 * @param pv the pv to set
	 */
	public void setPv(int pv) {
		this.pv = pv;
	}

	/**
	 * @return the forme
	 */
	public String getForm() {
		return this.form;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return this.speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(String form) {
		this.form = form;
	}

	/**
	 * @return the stamina
	 */
	public int getStamina() {
		return this.stamina;
	}

	/**
	 * @param stamina the stamina to set
	 */
	public void setStamina(int stamina) {
		this.stamina = stamina;
	}

	/**
	 * @return the attack
	 */
	public int getAttack() {
		return this.attack;
	}

	/**
	 * @param attack the attack to set
	 */
	public void setAttack(int attack) {
		this.attack = attack;
	}

	/**
	 * @return the defense
	 */
	public int getDefense() {
		return this.defense;
	}

	/**
	 * @param defense the defense to set
	 */
	public void setDefense(int defense) {
		this.defense = defense;
	}

	/**
	 * @return the isLegendary
	 */
	public boolean isLegendary() {
		return this.isLegendary;
	}

	/**
	 * @param isLegendary the isLegendary to set
	 */
	public void setLegendary(boolean isLegendary) {
		this.isLegendary = isLegendary;
	}

	public double getBaseAttack() {
		return this.attack >= 0 ? this.attack : getBaseAttack(isNerf());
	}

	private double getBaseAttack(boolean isNerf) {
		return this.attack >= 0 ? this.attack : Math.round(getScaledAttack() * getSpeedMod() * (isNerf ? 0.91 : 1.0));
	}

	private double getScaledAttack() {
		int higher = Math.max(this.physicalAttack, this.specialAttack);
		int lower = Math.min(this.physicalAttack, this.specialAttack);
		double higherScaled = ((7.0 / 8.0) * higher);
		double lowerScaled = ((1.0 / 8.0) * lower);
		return Math.round(2.0 * (higherScaled + lowerScaled));
	}

	public double getBaseDefense() {
		return this.defense >= 0 ? this.defense : getBaseDefense(isNerf());
	}

	private double getBaseDefense(boolean isNerf) {
		return this.defense >= 0
				? this.defense : Math.round(getScaledDefense() * getSpeedMod() * (isNerf ? 0.91 : 1.0));
	}

	private double getScaledDefense() {
		int higher = Math.max(this.physicalDefense, this.specialDefense);
		int lower = Math.min(this.physicalDefense, this.specialDefense);
		double higherScaled = ((5.0 / 8.0) * higher);
		double lowerScaled = ((3.0 / 8.0) * lower);
		return Math.round(2.0 * (higherScaled + lowerScaled));
	}

	private double getSpeedMod() {
		return 1.0 + ((this.speed - 75.0) / 500.0);
	}

	public double getBaseStamina() {
		return this.stamina >= 0 ? this.stamina : getBaseStamina(isNerf());
	}

	private double getBaseStamina(boolean isNerf) {
		return this.stamina >= 0 ? this.stamina : Math.floor(((this.pv * 1.75) + 50.0) * (isNerf ? 0.91 : 1.0));
	}

	public double getMaxCP() {
		return isNerf() ? getMaxCPNerf() : getMaxCPNormal();
	}

	private boolean isNerf() {
		return getMaxCPNormal() > 4000.0;
	}

	private double getMaxCPNormal() {
		double a = getBaseAttack(false) + 15.0;
		double d = Math.pow((getBaseDefense(false) + 15.0), 0.5);
		double s = Math.pow((getBaseStamina(false) + 15.0), 0.5);
		double multiplier40 = Math.pow(0.7903001, 2.0);
		return (a * d * s * multiplier40) / 10.0;
	}

	private double getMaxCPNerf() {
		double a = getBaseAttack(true) + 15.0;
		double d = Math.pow((getBaseDefense(true) + 15.0), 0.5);
		double s = Math.pow((getBaseStamina(true) + 15.0), 0.5);
		double multiplier40 = Math.pow(0.7903001, 2.0);
		return (a * d * s * multiplier40) / 10.0;
	}

	/**
	 * @param other
	 * @return compare by pokedexNum
	 */
	@Override
	public int compareTo(PkmnDesc other) {
		return COMPARATOR_BY_ID.compare(this, other);
	}

	public static final Comparator<PkmnDesc> COMPARATOR_BY_ID = (p1, p2) -> {
		Integer nullParams = CheckNullComparator.checkNull(p1, p2);
		if (nullParams != null) {
			return nullParams;
		}
		int res = Long.compare(p1.getPokedexNum(), p2.getPokedexNum());
		if (res == 0) {
			if (DEFAULT_FORM.equals(p1.form)) {
				res = DEFAULT_FORM.equals(p2.form) ? 0 : -1;
			} else {
				res = DEFAULT_FORM.equals(p2.form) ? 1 : p1.form.compareTo(p2.form);
			}
		}
		return res;
	};

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PkmnDesc)) return false;
		if (!super.equals(o)) return false;
		PkmnDesc pkmnDesc = (PkmnDesc) o;
		return Objects.equals(getUniqueId(), pkmnDesc.getUniqueId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getUniqueId());
	}

	public String getUniqueId() {
		return getId() + "_" + getForm();
	}
}