package com.pokemongostats.view.viewholder;

import com.pokemongostats.databinding.CardViewPkmnDescBinding;
import com.pokemongostats.model.bean.PkmnDesc;

import fr.commons.generique.ui.AbstractGeneriqueViewHolder;
import lombok.Getter;

public class LstPkmnDescViewHolder extends AbstractGeneriqueViewHolder<PkmnDesc> {

	@Getter
	protected final CardViewPkmnDescBinding binding;

	public LstPkmnDescViewHolder(CardViewPkmnDescBinding binding) {
		super(binding.getRoot());
		this.binding = binding;
	}

	@Override
	public void bind(PkmnDesc a) {
		this.binding.setPkmndesc(a);
	}

}