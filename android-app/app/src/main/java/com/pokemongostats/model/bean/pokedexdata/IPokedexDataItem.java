package com.pokemongostats.model.bean.pokedexdata;

import fr.commons.generique.model.db.IObjetBdd;

public interface IPokedexDataItem<T extends IObjetBdd> {

	IObjetBdd getData();
}