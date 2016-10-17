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

	public PokemonDescription() {
	}

	public PokemonDescription(final long pokedexNum, final String name,
			final Type type1) {
		this(pokedexNum, name, type1, null);
	}

	public PokemonDescription(final long pokedexNum, final String name,
			final Type type1, final Type type2) {
		this.setPokedexNum(pokedexNum);
		this.setName(name);
		this.setType1(type1);
		this.setType2(type2);
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "#" + pokedexNum + " : " + name
			+ (type1 == null
					? ""
					: " [" + type1.name()
						+ (type2 == null ? "" : " | " + type2.name()) + " ]");
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
