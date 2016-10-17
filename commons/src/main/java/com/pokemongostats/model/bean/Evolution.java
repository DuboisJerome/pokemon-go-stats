package com.pokemongostats.model.bean;

import java.io.Serializable;

public class Evolution implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1645998904713356956L;

	private long pokedexNum;
	private long evolutionId;
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
	 * @return the evolutionId
	 */
	public long getEvolutionId() {
		return evolutionId;
	}
	/**
	 * @param evolutionId
	 *            the evolutionId to set
	 */
	public void setEvolutionId(long evolutionId) {
		this.evolutionId = evolutionId;
	}
}
