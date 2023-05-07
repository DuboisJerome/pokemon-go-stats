package com.pokemongostats.view.viewholder.pokedexdata;

import com.google.android.material.card.MaterialCardView;
import com.pokemongostats.databinding.CardViewIncomingDataEvolBinding;
import com.pokemongostats.model.bean.bdd.Evolution;

public class LstPokedexDataEvolViewHolder extends AbstractLstPokedexDataViewHolder<Evolution> {

	private final CardViewIncomingDataEvolBinding binding;

	public LstPokedexDataEvolViewHolder(CardViewIncomingDataEvolBinding binding) {
		super(binding);
		this.binding = binding;
	}

	@Override
	protected void bindCreate(Evolution data) {
		// TODO
	}

	@Override
	protected void bindUpdate(Evolution oldData, Evolution newData) {
		// TODO
	}

	@Override
	protected void bindDelete(Evolution data) {
		// TODO
	}

	@Override
	protected void setCRUDColor(int color) {
		this.binding.setColorCRUD(color);
	}


	@Override
	public MaterialCardView getCardView() {
		return this.binding.cardView;
	}
}