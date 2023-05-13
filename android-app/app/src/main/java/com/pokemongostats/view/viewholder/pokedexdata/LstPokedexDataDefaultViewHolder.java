package com.pokemongostats.view.viewholder.pokedexdata;

import android.annotation.SuppressLint;

import com.google.android.material.card.MaterialCardView;
import com.pokemongostats.databinding.CardViewSimpleTextBinding;

import fr.commons.generique.model.db.IObjetBdd;

public class LstPokedexDataDefaultViewHolder<T extends IObjetBdd> extends AbstractLstPokedexDataViewHolder<T> {

	private final CardViewSimpleTextBinding binding;

	public LstPokedexDataDefaultViewHolder(CardViewSimpleTextBinding binding) {
		super(binding);
		this.binding = binding;
	}

	@SuppressLint("SetTextI18n")
	@Override
	protected void bindCreate(IObjetBdd data) {
		this.binding.text.setText("BindCreate inconnu pour " + data);
	}

	@SuppressLint("SetTextI18n")
	@Override
	protected void bindUpdate(IObjetBdd oldData, IObjetBdd data) {
		this.binding.text.setText("bindUpdate inconnu pour " + data);
	}

	@SuppressLint("SetTextI18n")
	@Override
	protected void bindDelete(IObjetBdd data) {
		this.binding.text.setText("bindDelete inconnu pour " + data);
	}

	@Override
	protected void setCRUDColor(int color) {
		this.binding.text.setTextColor(color);
	}

	@Override
	public MaterialCardView getCardView() {
		return this.binding.cardView;
	}
}