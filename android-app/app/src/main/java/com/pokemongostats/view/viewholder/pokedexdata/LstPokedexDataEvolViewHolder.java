package com.pokemongostats.view.viewholder.pokedexdata;

import com.pokemongostats.databinding.CardViewIncomingDataEvolBinding;
import com.pokemongostats.databinding.CardViewIncomingDataPkmnDescBinding;
import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.bean.bdd.PkmnDesc;

public class LstPokedexDataEvolViewHolder extends AbstractLstPokedexDataViewHolder<Evolution> {

	private CardViewIncomingDataEvolBinding binding;

	public LstPokedexDataEvolViewHolder(CardViewIncomingDataEvolBinding binding) {
		super(binding.getRoot());
		this.binding = binding;
	}

	@Override
	protected void bindCreate(Evolution data) {

	}

	@Override
	protected void bindUpdate(Evolution oldData, Evolution newData) {

	}

	@Override
	protected void bindDelete(Evolution data) {

	}
}