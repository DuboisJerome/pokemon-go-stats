package com.pokemongostats.view.viewholder;

import com.pokemongostats.databinding.CardViewPkmnMoveBinding;
import com.pokemongostats.model.bean.PkmnMoveComplet;

import fr.commons.generique.ui.AbstractGeneriqueViewHolder;
import lombok.Getter;

public class LstPkmnMoveViewHolder extends AbstractGeneriqueViewHolder<PkmnMoveComplet> {

	@Getter
	protected final CardViewPkmnMoveBinding binding;

	public LstPkmnMoveViewHolder(CardViewPkmnMoveBinding binding) {
		super(binding.getRoot());
		this.binding = binding;
	}

	@Override
	public void bind(PkmnMoveComplet pm) {
		this.binding.setPm(pm);
		this.binding.moveName.refreshDrawableState();
	}

}