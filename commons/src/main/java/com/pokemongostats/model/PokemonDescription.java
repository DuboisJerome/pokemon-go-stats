package com.pokemongostats.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Business object representing a description of a pokemon
 * 
 * @author Zapagon
 *
 */
public class PokemonDescription implements HasID {

	private long pokedexNum = HasID.NO_ID;

	private String name;

	private Set<Type> types;

	public PokemonDescription(final long pokedexNum, final String name,
			final Type type1) {
		this(pokedexNum, name, type1, null);
	}

	public PokemonDescription(final long pokedexNum, final String name,
			final Type type1, final Type type2) {
		this.setPokedexNum(pokedexNum);
		this.name = name;
		this.types = new HashSet<Type>();
		if (type1 != null) {
			this.types.add(type1);
		}
		if (type2 != null) {
			this.types.add(type2);
		}
	}

	public PokemonDescription(final long pokedexNum, final String name,
			final Set<Type> types) {
		this.setPokedexNum(pokedexNum);
		this.name = name;
		this.types = new HashSet<Type>();
		if (types != null) {
			this.types.addAll(types);
		}
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
	 * @return the types
	 */
	public Set<Type> getTypes() {
		return types;
	}

	/**
	 * @param types
	 *            the types to set
	 */
	public void setTypes(Set<Type> types) {
		this.types = types;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String typesStr = "[";
		for (Type t : this.types) {
			typesStr += t.name() + ",";
		}
		typesStr = typesStr.substring(0, typesStr.length() - 1);
		typesStr += "]";
		return "#" + this.pokedexNum + " : " + this.name + " " + typesStr;
	}

	/**
	 * @return pokedex number
	 */
	public long getId() {
		return pokedexNum;
	}

	/**
	 * @param pokedex
	 *            number
	 */
	public void setId(long pokedexNum) {
		this.pokedexNum = pokedexNum;
	}

}
