package com.pokemongostats.view.viewholder.pokedexdata;

import com.google.android.material.card.MaterialCardView;
import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.databinding.CardViewIncomingDataPkmnMoveBinding;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.bdd.PkmnMove;
import com.pokemongostats.model.external.json.PkmnMoveParserJson;

public class LstPokedexDataPkmnMoveViewHolder extends AbstractLstPokedexDataViewHolder<PkmnMove> {

	private final CardViewIncomingDataPkmnMoveBinding binding;

	public LstPokedexDataPkmnMoveViewHolder(CardViewIncomingDataPkmnMoveBinding binding) {
		super(binding);
		this.binding = binding;
	}

	@Override
	protected void bindCreate(PkmnMove data) {
		setPkmnMove(data);
	}

	@Override
	protected void bindUpdate(PkmnMove oldData, PkmnMove newData) {
		Move oldMove = PokedexDAO.getInstance().getMove(oldData);
		Move move = PokedexDAO.getInstance().getMove(newData);
		move.getI18n().setName(oldMove.getName());
		PkmnDesc oldPkmn = PokedexDAO.getInstance().getPokemon(oldData);
		PkmnDesc pkmn = PokedexDAO.getInstance().getPokemon(newData);
		pkmn.getI18n().setName(oldPkmn.getName());
		setPkmnMove(newData);
	}

	@Override
	protected void bindDelete(PkmnMove data) {
		setPkmnMove(data);
	}

	private void setPkmnMove(PkmnMove pm) {
		Move move = PokedexDAO.getInstance().getMove(pm);
		if (move == null && pm instanceof PkmnMoveParserJson pm2) {
			move = pm2.getMoveSrc();
		}
		PkmnDesc pkmn = PokedexDAO.getInstance().getPokemon(pm);
		if (pkmn == null && pm instanceof PkmnMoveParserJson pm2) {
			pkmn = pm2.getPkmnSrc();
		}
		this.binding.setMove(move);
		this.binding.setPkmndesc(pkmn);
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