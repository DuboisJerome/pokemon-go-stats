package com.pokemongostats.view.viewholder.pokedexdata;

import android.view.View;

import com.google.android.material.card.MaterialCardView;
import com.pokemongostats.databinding.CardViewIncomingDataMoveBinding;
import com.pokemongostats.model.bean.bdd.Move;

public class LstPokedexDataMoveViewHolder extends AbstractLstPokedexDataViewHolder<Move> {

	private final CardViewIncomingDataMoveBinding binding;

	public LstPokedexDataMoveViewHolder(CardViewIncomingDataMoveBinding binding) {
		super(binding);
		this.binding = binding;
	}

	@Override
	protected void bindCreate(Move data) {
		this.binding.setMove(data);
	}

	@Override
	protected void bindUpdate(Move oldData, Move newData) {
		newData.getI18n().setName(oldData.getName());
		this.binding.setOld(oldData);
		this.binding.setMove(newData);
		if (oldData.getPower() != newData.getPower()) {
			this.binding.oldStats.movePower.setVisibility(View.VISIBLE);
			this.binding.newStats.movePower.setVisibility(View.VISIBLE);
		}
		if (oldData.getEnergyDelta() != newData.getEnergyDelta()) {
			this.binding.oldStats.moveEnergy.setVisibility(View.VISIBLE);
			this.binding.newStats.moveEnergy.setVisibility(View.VISIBLE);
		}
		if (oldData.getPowerPvp() != newData.getPowerPvp()) {
			this.binding.oldStats.movePowerPvp.setVisibility(View.VISIBLE);
			this.binding.newStats.movePowerPvp.setVisibility(View.VISIBLE);
		}
		if (oldData.getEnergyPvp() != newData.getEnergyPvp()) {
			this.binding.oldStats.moveEnergyPvp.setVisibility(View.VISIBLE);
			this.binding.newStats.moveEnergyPvp.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void bindDelete(Move data) {
		this.binding.setMove(data);
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