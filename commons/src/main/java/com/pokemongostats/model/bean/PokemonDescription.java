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
public class PokemonDescription
		implements
			HasID,
			Comparable<PokemonDescription>,
			Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2768420569277625730L;

	private long pokedexNum = HasID.NO_ID;

	private String name;

	private String family;

	private String description;

	private Type type1;

	private Type type2;

	private List<Long> evolutionIds = new ArrayList<Long>();

	private List<Long> moveIds = new ArrayList<Long>();

	private double kmsPerCandy;

	private double kmsPerEgg;

	private double baseAttack;

	private double baseDefense;

	private double baseStamina;

	private int candyToEvolve;

	private double maxCP;

	/**
	 * @return the pokedexNum
	 */
	public long getPokedexNum() {
		return pokedexNum;
	}

	/**
	 * @param pokedexNum
	 *            the pokedexNum to set
	 */
	public void setPokedexNum(long pokedexNum) {
		this.pokedexNum = pokedexNum;
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
	 * @return the family
	 */
	public String getFamily() {
		return family;
	}

	/**
	 * @param family
	 *            the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the type1
	 */
	public Type getType1() {
		return type1;
	}

	/**
	 * @param type1
	 *            the type1 to set
	 */
	public void setType1(Type type1) {
		this.type1 = type1;
	}

	/**
	 * @return the type2
	 */
	public Type getType2() {
		return type2;
	}

	/**
	 * @param type2
	 *            the type2 to set
	 */
	public void setType2(Type type2) {
		this.type2 = type2;
	}

	/**
	 * @return #000 : Name [Type1] OR #000 : Name [Type1|Type2]
	 */
	@Override
	public String toString() {
		return "#" + pokedexNum + " : " + name
			+ (type1 == null
					? ""
					: " [" + type1.name()
						+ (type2 == null ? "" : "|" + type2.name()) + "]");
	}

	/**
	 * @return pokedex number
	 */
	@Override
	public long getId() {
		return pokedexNum;
	}

	/**
	 * @param pokedex
	 *            number
	 */
	@Override
	public void setId(long pokedexNum) {
		this.pokedexNum = pokedexNum;
	}

	/**
	 * @return the evolutionIds
	 */
	public List<Long> getEvolutionIds() {
		if (evolutionIds == null) evolutionIds = new ArrayList<Long>();
		return evolutionIds;
	}

	/**
	 * @param evolutionIds
	 *            the evolutionIds to set
	 */
	public void setEvolutionIds(List<Long> evolutionIds) {
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
		return moveIds;
	}

	/**
	 * @param moves
	 *            the moves to set
	 */
	public void setMoveIds(List<Long> moves) {
		this.moveIds = moves;
	}

	/**
	 * @return the evolutionIds
	 */
	public boolean addMoveId(final long id) {
		return getEvolutionIds().add(id);
	}

	/**
	 * @return the evolutionIds
	 */
	public boolean removeMoveId(final long id) {
		return getEvolutionIds().remove(id);
	}

	/**
	 * @return the kmsPerCandy
	 */
	public double getKmsPerCandy() {
		return kmsPerCandy;
	}

	/**
	 * @param kmsPerCandy
	 *            the kmsPerCandy to set
	 */
	public void setKmsPerCandy(double kmsPerCandy) {
		this.kmsPerCandy = kmsPerCandy;
	}

	/**
	 * @return the kmsPerEgg
	 */
	public double getKmsPerEgg() {
		return kmsPerEgg;
	}

	/**
	 * @param kmsPerEgg
	 *            the kmsPerEgg to set
	 */
	public void setKmsPerEgg(double kmsPerEgg) {
		this.kmsPerEgg = kmsPerEgg;
	}

	/**
	 * @return the baseAttack
	 */
	public double getBaseAttack() {
		return baseAttack;
	}

	/**
	 * @param baseAttack
	 *            the baseAttack to set
	 */
	public void setBaseAttack(double baseAttack) {
		this.baseAttack = baseAttack;
	}

	/**
	 * @return the baseDefense
	 */
	public double getBaseDefense() {
		return baseDefense;
	}

	/**
	 * @param baseDefense
	 *            the baseDefense to set
	 */
	public void setBaseDefense(double baseDefense) {
		this.baseDefense = baseDefense;
	}

	/**
	 * @return the baseStamina
	 */
	public double getBaseStamina() {
		return baseStamina;
	}

	/**
	 * @param baseStamina
	 *            the baseStamina to set
	 */
	public void setBaseStamina(double baseStamina) {
		this.baseStamina = baseStamina;
	}

	/**
	 * @return the candyToEvolve
	 */
	public int getCandyToEvolve() {
		return candyToEvolve;
	}

	/**
	 * @param candyToEvolve
	 *            the candyToEvolve to set
	 */
	public void setCandyToEvolve(int candyToEvolve) {
		this.candyToEvolve = candyToEvolve;
	}

	/**
	 * @return the maxCP
	 */
	public double getMaxCP() {
		return maxCP;
	}

	/**
	 * @param maxCP
	 *            the maxCP to set
	 */
	public void setMaxCP(double maxCP) {
		this.maxCP = maxCP;
	}

	/**
	 * @param other
	 * @return compare by pokedexNum
	 */
	@Override
	public int compareTo(PokemonDescription other) {
		if (other == null || other.getPokedexNum() <= 0) { return 1; }
		if (getPokedexNum() <= 0) { return -1; }
		return new Long(pokedexNum).compareTo(new Long(other.getPokedexNum()));
	}

}
