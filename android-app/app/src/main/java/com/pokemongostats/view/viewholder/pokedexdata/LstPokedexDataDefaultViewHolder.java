package com.pokemongostats.view.viewholder.pokedexdata;

import com.google.android.material.card.MaterialCardView;
import com.pokemongostats.databinding.CardViewSimpleTextBinding;

import fr.commons.generique.model.db.IObjetBdd;

public class LstPokedexDataDefaultViewHolder<T extends IObjetBdd> extends AbstractLstPokedexDataViewHolder<T> {

	private final CardViewSimpleTextBinding binding;

	public LstPokedexDataDefaultViewHolder(CardViewSimpleTextBinding binding) {
		super(binding);
		this.binding = binding;
	}

	@Override
	protected void bindCreate(IObjetBdd data) {
		this.binding.text.setText("BindCreate inconnu pour " + data);
	}

	@Override
	protected void bindUpdate(IObjetBdd oldData, IObjetBdd data) {
		this.binding.text.setText("BindCreate inconnu pour " + data);
	}

	@Override
	protected void bindDelete(IObjetBdd data) {
		this.binding.text.setText("BindCreate inconnu pour " + data);
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