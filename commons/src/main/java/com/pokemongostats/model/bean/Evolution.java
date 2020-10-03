package com.pokemongostats.model.bean;

import java.io.Serializable;

public class Evolution implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1645998904713356956L;

	private long basePkmnId;
	private String basePkmnForm;
	private long evolutionId;
	private String evolutionForm;

	public Evolution() {}

	public Evolution(final long basePkmnId, final String basePkmnForm, final long evolutionId,
			final String evolutionForm) {
		this.basePkmnId = basePkmnId;
		this.basePkmnForm = basePkmnForm;
		this.evolutionId = evolutionId;
		this.evolutionForm = evolutionForm;
	}

	public long getBasePkmnId() {
		return this.basePkmnId;
	}

	public void setBasePkmnId(final long basePkmnId) {
		this.basePkmnId = basePkmnId;
	}

	public String getBasePkmnForm() {
		return this.basePkmnForm;
	}

	public void setBasePkmnForm(final String basePkmnForm) {
		this.basePkmnForm = basePkmnForm;
	}

	public long getEvolutionId() {
		return this.evolutionId;
	}

	public void setEvolutionId(final long evolutionId) {
		this.evolutionId = evolutionId;
	}

	public String getEvolutionForm() {
		return this.evolutionForm;
	}

	public void setEvolutionForm(final String evolutionForm) {
		this.evolutionForm = evolutionForm;
	}

	public boolean isFrom(final PkmnDesc p) {
		return this.basePkmnId == p.getId() && this.basePkmnForm.equals(p.getForm());
	}

	public boolean isTo(final PkmnDesc p) {
		return this.evolutionId == p.getId() && this.evolutionForm.equals(p.getForm());
	}

}
