package com.pokemongostats.model.bean.bdd;

import fr.commons.generique.model.db.IObjetBdd;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveI18N implements IObjetBdd {
	protected String lang;
	protected long id = -1;
	protected String name;

	@Override
	public boolean isNew() {
		return false;
	}
}