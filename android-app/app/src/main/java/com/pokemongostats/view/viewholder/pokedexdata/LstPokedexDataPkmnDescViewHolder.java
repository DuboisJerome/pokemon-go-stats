package com.pokemongostats.view.viewholder.pokedexdata;

import com.pokemongostats.R;
import com.pokemongostats.databinding.CardViewIncomingDataPkmnDescBinding;
import com.pokemongostats.model.bean.bdd.PkmnDesc;

public class LstPokedexDataPkmnDescViewHolder extends AbstractLstPokedexDataViewHolder<PkmnDesc> {

	private CardViewIncomingDataPkmnDescBinding binding;

	public LstPokedexDataPkmnDescViewHolder(CardViewIncomingDataPkmnDescBinding binding) {
		super(binding.getRoot());
		this.binding = binding;
	}

	@Override
	protected void bindCreate(PkmnDesc data) {
		this.binding.setPkmndesc(data);
		this.binding.setColorCRUD(this.binding.getRoot().getContext().getColor(R.color.grass_bg));
	}

	@Override
	protected void bindUpdate(PkmnDesc oldData, PkmnDesc newData) {
		newData.getI18n().setName(oldData.getName());
		this.binding.setPkmndesc(newData);
		this.binding.setColorCRUD(this.binding.getRoot().getContext().getColor(R.color.electric_bg));
	}

	@Override
	protected void bindDelete(PkmnDesc data) {
		this.binding.setPkmndesc(data);
		this.binding.setColorCRUD(this.binding.getRoot().getContext().getColor(R.color.fire_bg));
	}
}