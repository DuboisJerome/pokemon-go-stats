package com.pokemongostats.model.bean.pokedexdata;

import fr.commons.generique.model.db.IObjetBdd;
import lombok.Getter;

public class PokedexDataItemUpdate<T extends IObjetBdd> implements IPokedexDataItem<T> {

	@Getter
	private final T oldData;

	@Getter
	private final T newData;

	public PokedexDataItemUpdate(T oldT, T newT) {
		this.oldData = oldT;
		this.newData = newT;
	}

	@Override
	public T getData() {
		return this.newData;
	}

	@Override
	public void addToData(PokedexData.Data<T> datas) {
		datas.addUpdate(this.oldData, this.newData);
	}
}