package com.pokemongostats.model.bean.pokedexdata;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.model.db.IObjetBdd;
import lombok.Getter;

public class PokedexDataItemCreate<T extends IObjetBdd> implements IPokedexDataItem<T> {


	@Getter
	private final T data;

	public PokedexDataItemCreate(T t) {
		this.data = t;
	}

	@Override
	public String toSql(TableDAO<T> dao) {
		return dao.buildReqInsert(getData());
	}
}