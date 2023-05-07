package com.pokemongostats.model.bean.pokedexdata;

import fr.commons.generique.controller.db.TableDAO;
import fr.commons.generique.model.db.IObjetBdd;

public interface IPokedexDataItem<T extends IObjetBdd> {

	T getData();

	String toSql(TableDAO<T> dao);
}