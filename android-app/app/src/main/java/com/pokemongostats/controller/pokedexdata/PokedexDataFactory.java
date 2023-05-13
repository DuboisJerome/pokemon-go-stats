package com.pokemongostats.controller.pokedexdata;

import com.pokemongostats.model.bean.pokedexdata.IPokedexDataItem;
import com.pokemongostats.model.bean.pokedexdata.PokedexData;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemCreate;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemDelete;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemUpdate;

import java.util.ArrayList;
import java.util.List;

import fr.commons.generique.model.db.IObjetBdd;

public class PokedexDataFactory {

	public static <T extends IObjetBdd> List<IPokedexDataItem<T>> createLstItem(PokedexData.Data<T> datas) {
		List<IPokedexDataItem<T>> results = new ArrayList<>();
		for (T t : datas.getLstToCreate()) {
			results.add(createrItemCreate(t));
		}
		for (var entry : datas.getLstToUpdate().entrySet()) {
			T oldT = entry.getKey();
			T newT = entry.getValue();
			results.add(createrItemUpdate(oldT, newT));
		}
		for (T t : datas.getLstToDelete()) {
			results.add(createrItemDelete(t));
		}
		return results;
	}

	public static <T extends IObjetBdd> PokedexDataItemCreate<T> createrItemCreate(T t) {
		return new PokedexDataItemCreate<>(t);
	}

	public static <T extends IObjetBdd> PokedexDataItemUpdate<T> createrItemUpdate(T oldT, T newT) {
		return new PokedexDataItemUpdate<>(oldT, newT);
	}

	public static <T extends IObjetBdd> PokedexDataItemDelete<T> createrItemDelete(T t) {
		return new PokedexDataItemDelete<>(t);
	}
}