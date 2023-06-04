package com.pokemongostats.view.viewholder.pokedexdata;

import com.google.android.material.card.MaterialCardView;
import com.pokemongostats.databinding.CardViewIncomingDataPkmnDescBinding;
import com.pokemongostats.model.bean.bdd.PkmnDesc;

public class LstPokedexDataPkmnDescViewHolder extends AbstractLstPokedexDataViewHolder<PkmnDesc> {

	private final CardViewIncomingDataPkmnDescBinding binding;

	public LstPokedexDataPkmnDescViewHolder(CardViewIncomingDataPkmnDescBinding binding) {
		super(binding);
		this.binding = binding;

	}

	@Override
	protected void bindCreate(PkmnDesc data) {
		this.binding.setPkmndesc(data);
	}

	@Override
	protected void bindUpdate(PkmnDesc oldData, PkmnDesc newData) {
		newData.getI18n().setName(oldData.getName());
		this.binding.setOld(oldData);
		this.binding.setPkmndesc(newData);
	}

	@Override
	protected void bindDelete(PkmnDesc data) {
		this.binding.setPkmndesc(data);

	}

	@Override
	protected void setTypeData(int drawable, int color) {
		this.binding.setDrawableCRUD(drawable);
		this.binding.setColorCRUD(color);
	}

	@Override
	public MaterialCardView getCardView() {
		return this.binding.cardView;
	}
}