package com.pokemongostats.model.bean.bdd;

import java.io.Serializable;

import fr.commons.generique.model.db.IObjetBdd;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PkmnMove implements Serializable, IObjetBdd {

	/**
	 *
	 */
	private static final long serialVersionUID = -1645998904713356956L;

	private long pokedexNum;
	private String form;
	private long moveId;
	private boolean isElite;

	/**
	 * @return the pokedex_num
	 */
	public long getPokedexNum() {
		return this.pokedexNum;
	}

	/**
	 * @param pokedexNum the pokedex_num to set
	 */
	public void setPokedexNum(long pokedexNum) {
		this.pokedexNum = pokedexNum;
	}

	/**
	 * @return the moveId
	 */
	public long getMoveId() {
		return this.moveId;
	}

	/**
	 * @param moveId the moveId to set
	 */
	public void setMoveId(long moveId) {
		this.moveId = moveId;
	}

	/**
	 * @return the forme
	 */
	public String getForm() {
		return this.form;
	}

	/**
	 * @param form the forme to set
	 */
	public void setForm(String form) {
		this.form = form;
	}

	public String getUniquePkmnId() {
		return this.pokedexNum + "_" + this.form;
	}

	@Override
	public boolean isNew() {
		return false;
	}
}