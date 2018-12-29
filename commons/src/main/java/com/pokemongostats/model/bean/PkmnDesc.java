package com.pokemongostats.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

	private final String forme = "NORMAL";

	private Type type1;

	private Type type2;

	private String name;

	private String family;

	private String description;

	private List<Long> evolutionIds = new ArrayList<>();

	private List<Long> moveIds = new ArrayList<>();

	private double kmsPerCandy;

	private double kmsPerEgg;

	private int physicalAttack;

	private int physicalDefense;

	private int specialAttack;

	private int specialDefense;

	private int pv;

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
	 * @return the evolutionIds
	 */
	public List<Long> getEvolutionIds() {
		if (this.evolutionIds == null) this.evolutionIds = new ArrayList<>();
		return this.evolutionIds;
	}

	/**
	 * @param evolutionIds
	 *            the evolutionIds to set
	 */
	public void setEvolutionIds(final List<Long> evolutionIds) {
		this.evolutionIds = evolutionIds;
	}

	/**
	 * @return the evolutionIds
	 */
	public boolean addEvolutionId(final long id) {
		return getEvolutionIds().add(id);
	}

	/**
	 * @return the evolutionIds
	 */
	public boolean removeEvolutionId(final long id) {
		return getEvolutionIds().remove(id);
	}

	/**
	 * @return the moves
	 */
	public List<Long> getMoveIds() {
		return this.moveIds;
	}

	/**
	 * @param moves
	 *            the moves to set
	 */
	public void setMoveIds(final List<Long> moves) {
		this.moveIds = moves;
	}

	/**
	 * @return the evolutionIds
	 */
	public boolean addMoveId(final long id) {
		return getMoveIds().add(id);
	}

	/**
	 * @return the evolutionIds
	 */
	public boolean removeMoveId(final long id) {
		return getMoveIds().remove(id);
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
