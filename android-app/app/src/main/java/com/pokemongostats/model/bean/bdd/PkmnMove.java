package com.pokemongostats.model.bean.bdd;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

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

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	@NonNull
	public String toString() {
		return "PkmnMove{" + "pokedexNum=" + this.pokedexNum +
				", form='" + this.form + '\'' +
				", moveId=" + this.moveId +
				", isElite=" + this.isElite +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof PkmnMove pkmnMove)) return false;
		return getPokedexNum() == pkmnMove.getPokedexNum() && getMoveId() == pkmnMove.getMoveId() && Objects.equals(getForm(), pkmnMove.getForm());
	}

	public boolean isFor(PkmnDesc p) {
		return this.pokedexNum == p.getPokedexNum() && this.form.equals(p.getForm());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getPokedexNum(), getForm(), getMoveId());
	}
}