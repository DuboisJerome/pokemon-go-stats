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

	private PkmnDescParserJson pkmnSrc;

	private MoveParserJson moveSrc;

	public void setPkmn(PkmnDescParserJson p) {
		this.pkmnSrc = p;
		if (p != null) {
			this.pkmnIdStr = p.getIdStr();
			setPokedexNum(p.getPokedexNum());
			setForm(p.getForm());
		}
	}

	public void setMove(MoveParserJson m) {
		this.moveSrc = m;
		if (m != null) {
			this.moveIdStr = m.getIdStr();
			setMoveId(m.getId());
		} else {
			setMoveId(-1L);
		}
	}

	@Override
	@NonNull
	public String toString() {
		return this.pkmnIdStr +
				" - " + this.moveIdStr + '\'';
	}
}