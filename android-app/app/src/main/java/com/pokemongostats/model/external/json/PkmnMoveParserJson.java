package com.pokemongostats.model.external.json;

import androidx.annotation.NonNull;

import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnMove;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PkmnMoveParserJson extends PkmnMove {

	private String pkmnIdStr;
	private String moveIdStr;

	// Dans le fichier json, c'est l'association au pkmn qui permet de savoir le type d'attaque (chargée/rapide)
	// et non pas l'attaque elle meme
	private Move.MoveType moveType = null;

	public void setPkmn(PkmnDescParserJson p) {
		this.pkmnIdStr = p.getIdStr();
		setPokedexNum(p.getPokedexNum());
		setForm(p.getForm());
	}

	public void setMove(MoveParserJson m) {
		this.moveIdStr = m.getIdStr();
		setMoveId(m.getId());
	}

	@Override
	@NonNull
	public String toString() {
		return this.pkmnIdStr +
				" - " + this.moveIdStr + '\'';
	}
}