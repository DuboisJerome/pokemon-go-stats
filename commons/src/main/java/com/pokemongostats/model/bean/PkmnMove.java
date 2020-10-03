package com.pokemongostats.model.bean;

import java.io.Serializable;

public class PkmnMove implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1645998904713356956L;

	private long pokedexNum;
	private String form;
	private long moveId;

	/**
	 * @return the pokedex_num
	 */
	public long getPokedexNum() {
		return this.pokedexNum;
	}

	/**
	 * @param pokedex_num
	 *            the pokedex_num to set
	 */
	public void setPokedexNum(final long pokedexNum) {
		this.pokedexNum = pokedexNum;
	}

	/**
	 * @return the moveId
	 */
	public long getMoveId() {
		return this.moveId;
	}

	/**
	 * @param moveId
	 *            the moveId to set
	 */
	public void setMoveId(final long moveId) {
		this.moveId = moveId;
	}

	/**
	 * @return the forme
	 */
	public String getForm() {
		return this.form;
	}

	/**
	 * @param forme
	 *            the forme to set
	 */
	public void setForm(final String form) {
		this.form = form;
	}
}
