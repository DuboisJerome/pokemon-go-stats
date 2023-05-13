package com.pokemongostats.model.external.json;

import com.pokemongostats.model.bean.bdd.Evolution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvolutionParserJson extends Evolution {

	private String basePkmnIdStr;
	private String evolutionIdStr;

	public void setPkmnBase(PkmnDescParserJson p) {
		setBasePkmnId(p.getPokedexNum());
		setBasePkmnIdStr(p.getIdStr());
		setBasePkmnForm(p.getForm());
	}

	public void setPkmnEvol(PkmnDescParserJson p) {
		setEvolutionId(p.getPokedexNum());
		setEvolutionIdStr(p.getIdStr());
		setEvolutionForm(p.getForm());
	}

	@Override
	public String toString() {
		return this.basePkmnIdStr + "|" + getBasePkmnForm() + " => " + this.evolutionIdStr + "|" + getEvolutionForm();
	}
}