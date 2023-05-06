package com.pokemongostats.view.viewholder.pokedexdata;

import android.view.View;

import com.pokemongostats.model.bean.pokedexdata.IPokedexDataItem;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemCreate;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemDelete;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemUpdate;

import fr.commons.generique.model.db.IObjetBdd;
import fr.commons.generique.ui.AbstractGeneriqueViewHolder;

public class LstPokedexDataDefaultViewHolder<T extends IObjetBdd> extends AbstractLstPokedexDataViewHolder<T> {

	public LstPokedexDataDefaultViewHolder(View view) {
		super(view);
	}

	@Override
	protected void bindCreate(IObjetBdd data) {

	}

	@Override
	protected void bindUpdate(IObjetBdd oldData, IObjetBdd newData) {

	}

	@Override
	protected void bindDelete(IObjetBdd data) {

	}

}