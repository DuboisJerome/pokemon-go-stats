package com.pokemongostats.model.bean.bdd;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Objects;

import fr.commons.generique.model.db.IObjetBdd;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Evolution implements Serializable, IObjetBdd {

	/**
	 *
	 */
	private static final long serialVersionUID = -1645998904713356956L;

	private long basePkmnId;
	private String basePkmnForm;
	private long evolutionId;
	private String evolutionForm;
	private boolean isTemporaire;
	private int candyToEvolve;

	public Evolution() {
	}

	public long getBasePkmnId() {
		return this.basePkmnId;
	}

	public void setBasePkmnId(long basePkmnId) {
		this.basePkmnId = basePkmnId;
	}

	public String getBasePkmnForm() {
		return this.basePkmnForm;
	}

	public void setBasePkmnForm(String basePkmnForm) {
		this.basePkmnForm = basePkmnForm;
	}

	public long getEvolutionId() {
		return this.evolutionId;
	}

	public void setEvolutionId(long evolutionId) {
		this.evolutionId = evolutionId;
	}

	public String getEvolutionForm() {
		return this.evolutionForm;
	}

	public void setEvolutionForm(String evolutionForm) {
		this.evolutionForm = evolutionForm;
	}

	@Override
	public boolean isNew() {
		return false;
	}


	@Override
	@NonNull
	public String toString() {
		return this.basePkmnId + "|" + this.basePkmnForm +
				" => " +
				this.evolutionId + "|" + this.evolutionForm;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Evolution evolution)) return false;
		return getBasePkmnId() == evolution.getBasePkmnId() && getEvolutionId() == evolution.getEvolutionId() && Objects.equals(getBasePkmnForm(), evolution.getBasePkmnForm()) && Objects.equals(getEvolutionForm(), evolution.getEvolutionForm());
	}

	public boolean isEvolOf(PkmnDesc p) {
		return this.basePkmnId == p.getPokedexNum() && this.basePkmnForm.equals(p.getForm());
	}

	public boolean isEvolTo(PkmnDesc p) {
		return this.evolutionId == p.getPokedexNum() && this.evolutionForm.equals(p.getForm());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getBasePkmnId(), getBasePkmnForm(), getEvolutionId(), getEvolutionForm());
	}
}