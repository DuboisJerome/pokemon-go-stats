package com.pokemongostats.model.bean.bdd;

import fr.commons.generique.model.db.IObjetBdd;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PkmnDescI18N implements IObjetBdd {
	protected String lang = "";
	protected long id = -1;
	protected String form = "";
	protected String name = "";

	protected String family;
	protected String description;

	public PkmnDescI18N() {
		// default ctor
	}

	public PkmnDescI18N(PkmnDescI18N other) {
		this.lang = other.lang;
		this.id = other.id;
		this.form = other.form;
		this.name = other.name;
		this.family = other.family;
		this.description = other.description;
	}

	@Override
	public boolean isNew() {
		return false;
	}

	public void setPkmn(PkmnDesc p) {
		this.id = p.getId();
		this.form = p.getForm();
	}
}