package com.pokemongostats.model.bean.bdd;

import java.io.Serializable;

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

	public Evolution(long basePkmnId, String basePkmnForm, long evolutionId,
	                 String evolutionForm) {
		this.basePkmnId = basePkmnId;
		this.basePkmnForm = basePkmnForm;
		this.evolutionId = evolutionId;
		this.evolutionForm = evolutionForm;
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

	public boolean isFrom(PkmnDesc p) {
		return this.getUniqueIdBase().equals(p.getUniqueId());
	}

	public boolean isTo(PkmnDesc p) {
		return this.getUniqueIdEvol().equals(p.getUniqueId());
	}

	public String getUniqueIdBase() {
		return this.basePkmnId + "_" + this.basePkmnForm;
	}

	public String getUniqueIdEvol() {
		return this.evolutionId + "_" + this.evolutionForm;
	}

	@Override
	public boolean isNew() {
		return false;
	}
}