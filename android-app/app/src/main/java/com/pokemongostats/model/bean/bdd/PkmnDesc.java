package com.pokemongostats.model.bean.bdd;

import com.pokemongostats.controller.utils.PkmnTags;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.comparators.CheckNullComparator;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import fr.commons.generique.model.db.AbstractObjetBddAvecId;
import fr.commons.generique.model.db.IObjetBdd;
import lombok.Getter;
import lombok.Setter;

/**
 * Business object representing a description of a pokemon
 *
 * @author Zapagon
 */
@Getter
@Setter
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

	private Set<String> tags = new HashSet<>();

	private boolean isLastEvol;

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

	public boolean isLegendaire() {
		return hasTag(PkmnTags.LEGENDAIRE);
	}

	public boolean isFabuleux() {
		return hasTag(PkmnTags.FABULEUX);
	}

	public boolean isUltraChimere() {
		return hasTag(PkmnTags.ULTRA_CHIMERE);
	}

	public boolean isMega() {
		return hasTag(PkmnTags.MEGA);
	}

	public boolean isInGame() {
		return !hasTag(PkmnTags.NOT_IN_GAME);
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
	 * @param other o
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

	public void setTags(String str) {
		if(str != null){
			this.tags.addAll(Arrays.asList(str.split(",")));
		}
	}

	public void setTags(Set<String> t) {
		this.tags = t;
	}

	public boolean hasTag(String tag) {
		return this.tags.contains(tag);
	}
}