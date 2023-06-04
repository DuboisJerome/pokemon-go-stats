package com.pokemongostats.model.bean.pokedexdata;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.model.db.IObjetBdd;
import lombok.Getter;

public class PokedexDataItemDelete<T extends IObjetBdd> implements IPokedexDataItem<T> {

	@Getter
	private final T data;

	public PokedexDataItemDelete(T t) {
		this.data = t;
	}

	@Override
	public String toSql(TableDAO<T> dao) {
		return dao.buildReqDelete(getData());
	}

	@Override
	public void addToData(PokedexData.Data<T> datas) {
		datas.addDelete(this.data);
	}
}