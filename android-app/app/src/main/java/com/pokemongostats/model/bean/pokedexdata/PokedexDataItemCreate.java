package com.pokemongostats.model.bean.pokedexdata;

import fr.commons.generique.model.db.IObjetBdd;
import lombok.Getter;

public class PokedexDataItemCreate<T extends IObjetBdd> implements IPokedexDataItem<T> {


	@Getter
	private final T data;
	public PokedexDataItemCreate(T t) {
		this.data = t;
	}

}