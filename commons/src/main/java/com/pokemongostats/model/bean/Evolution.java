package com.pokemongostats.model.bean;

import java.io.Serializable;

public class Evolution implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1645998904713356956L;

	private long pokedexNum;
	private String forme;
	private long evolutionId;
	private String formeEvolution;

	public Evolution() {}

	public Evolution(final long pokedexNum, final String forme, final long evolutionId, final String formeEvolution) {
		this.pokedexNum = pokedexNum;
		this.evolutionId = evolutionId;
	}

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
	 * @return the evolutionId
	 */
	public long getEvolutionId() {
		return this.evolutionId;
	}

	/**
	 * @param evolutionId
	 *            the evolutionId to set
	 */
	public void setEvolutionId(final long evolutionId) {
		this.evolutionId = evolutionId;
	}

	/**
	 * @return the forme
	 */
	public String getForme() {
		return this.forme;
	}

	/**
	 * @param forme
	 *            the forme to set
	 */
	public void setForme(final String forme) {
		this.forme = forme;
	}

	/**
	 * @return the formeEvolution
	 */
	public String getFormeEvolution() {
		return this.formeEvolution;
	}

	/**
	 * @param formeEvolution
	 *            the formeEvolution to set
	 */
	public void setFormeEvolution(final String formeEvolution) {
		this.formeEvolution = formeEvolution;
	}

}
