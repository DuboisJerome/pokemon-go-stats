package com.pokemongostats.view.viewholder.pokedexdata;

import android.view.View;

import com.pokemongostats.model.bean.pokedexdata.IPokedexDataItem;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemCreate;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemDelete;
import com.pokemongostats.model.bean.pokedexdata.PokedexDataItemUpdate;

import fr.commons.generique.model.db.IObjetBdd;
import fr.commons.generique.ui.AbstractGeneriqueViewHolder;

public abstract class AbstractLstPokedexDataViewHolder<T extends IObjetBdd> extends AbstractGeneriqueViewHolder<IPokedexDataItem<T>> {

	public AbstractLstPokedexDataViewHolder(View view) {
		super(view);
	}

	@Override
	protected final void bind(IPokedexDataItem<T> item) {
		if(item instanceof PokedexDataItemCreate<T> create){
			bindCreate(create.getData());
		} else if(item instanceof PokedexDataItemUpdate<T> update){
			bindUpdate(update.getDataOld(), update.getDataNew());
		} else if(item instanceof PokedexDataItemDelete<T> delete){
			bindDelete(delete.getData());
		}
	}

	protected abstract void bindCreate(T data);
	protected abstract void bindUpdate(T oldData, T newData);
	protected abstract void bindDelete(T data);
}