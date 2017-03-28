package com.pokemongostats.model.bean;

import java.io.Serializable;

public class PkmnMove implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1645998904713356956L;

	private long pokedexNum;
	private long moveId;
	/**
	 * @return the pokedex_num
	 */
	public long getPokedexNum() {
		return pokedexNum;
	}
	/**
	 * @param pokedex_num
	 *            the pokedex_num to set
	 */
	public void setPokedexNum(long pokedexNum) {
		this.pokedexNum = pokedexNum;
	}
	/**
	 * @return the moveId
	 */
	public long getMoveId() {
		return moveId;
	}
	/**
	 * @param moveId
	 *            the moveId to set
	 */
	public void setMoveId(long moveId) {
		this.moveId = moveId;
	}
}
