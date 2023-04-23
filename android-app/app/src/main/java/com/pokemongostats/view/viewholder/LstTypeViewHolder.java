package com.pokemongostats.view.viewholder;

import com.pokemongostats.databinding.CardViewTypeBinding;
import com.pokemongostats.model.bean.Type;

import fr.commons.generique.ui.AbstractGeneriqueViewHolder;
import lombok.Getter;

public class LstTypeViewHolder extends AbstractGeneriqueViewHolder<Type> {

	@Getter
	protected final CardViewTypeBinding binding;

	public LstTypeViewHolder(CardViewTypeBinding binding) {
		super(binding.getRoot());
		this.binding = binding;
	}

	@Override
	public void bind(Type a) {
		this.binding.setType(a);
	}

}