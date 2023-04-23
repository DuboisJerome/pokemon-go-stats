package com.pokemongostats.view.viewholder;

import com.pokemongostats.databinding.CardViewMoveBinding;
import com.pokemongostats.model.bean.Move;

import fr.commons.generique.ui.AbstractGeneriqueViewHolder;
import lombok.Getter;

public class LstMoveViewHolder extends AbstractGeneriqueViewHolder<Move> {

	@Getter
	protected final CardViewMoveBinding binding;

	public LstMoveViewHolder(CardViewMoveBinding binding) {
		super(binding.getRoot());
		this.binding = binding;
	}

	@Override
	public void bind(Move a) {
		this.binding.setMove(a);
		this.binding.moveName.refreshDrawableState();
	}

}