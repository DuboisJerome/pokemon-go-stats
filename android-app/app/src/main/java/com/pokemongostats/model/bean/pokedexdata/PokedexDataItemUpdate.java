package com.pokemongostats.model.bean.pokedexdata;

import fr.commons.generique.model.db.IObjetBdd;
import lombok.Getter;

public class PokedexDataItemUpdate<T extends IObjetBdd> implements IPokedexDataItem<T> {

	@Getter
	private final T dataOld;

	@Getter
	private final T dataNew;

	public PokedexDataItemUpdate(T oldT, T newT) {
		this.dataOld = oldT;
		this.dataNew = newT;
	}

	@Override
	public T getData() {
		return this.dataNew;
	}
}