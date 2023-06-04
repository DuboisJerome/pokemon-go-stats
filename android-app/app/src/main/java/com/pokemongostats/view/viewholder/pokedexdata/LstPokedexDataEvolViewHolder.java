package com.pokemongostats.view.viewholder.pokedexdata;

import com.google.android.material.card.MaterialCardView;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.databinding.CardViewIncomingDataEvolBinding;
import com.pokemongostats.model.bean.bdd.Evolution;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.external.json.EvolutionParserJson;

public class LstPokedexDataEvolViewHolder extends AbstractLstPokedexDataViewHolder<Evolution> {

	private final CardViewIncomingDataEvolBinding binding;

	public LstPokedexDataEvolViewHolder(CardViewIncomingDataEvolBinding binding) {
		super(binding);
		this.binding = binding;

	}

	@Override
	protected void bindCreate(Evolution data) {
		setEvolution(data);
	}

	@Override
	protected void bindUpdate(Evolution oldData, Evolution newData) {
		setEvolution(newData);
	}

	@Override
	protected void bindDelete(Evolution data) {
		setEvolution(data);
	}

	private void setEvolution(Evolution data) {
		PkmnDesc base = PokedexDAO.getInstance().getPokemon(data.getBasePkmnId(), data.getBasePkmnForm());
		if (base == null && data instanceof EvolutionParserJson e2) {
			base = e2.getBaseSrc();
		}
		PkmnDesc evol = PokedexDAO.getInstance().getPokemon(data.getEvolutionId(), data.getEvolutionForm());
		if (evol == null && data instanceof EvolutionParserJson e2) {
			evol = e2.getEvolSrc();
		}
		this.binding.setBase(base);
		this.binding.setEvol(evol);
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