package com.pokemongostats.model.external.json;

import androidx.annotation.NonNull;

import com.pokemongostats.model.bean.bdd.Evolution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EvolutionParserJson extends Evolution {

	private String basePkmnIdStr;
	private String evolutionIdStr;

	private PkmnDescParserJson baseSrc;
	private PkmnDescParserJson evolSrc;

	public void setPkmnBase(PkmnDescParserJson p) {
		this.baseSrc = p;
		if (p != null) {
			setBasePkmnId(p.getPokedexNum());
			setBasePkmnIdStr(p.getIdStr());
			setBasePkmnForm(p.getForm());
		}
	}

	public void setPkmnEvol(PkmnDescParserJson p) {
		this.evolSrc = p;
		if (p != null) {
			setEvolutionId(p.getPokedexNum());
			setEvolutionIdStr(p.getIdStr());
			setEvolutionForm(p.getForm());
		}
	}

	@Override
	@NonNull
	public String toString() {
		return this.basePkmnIdStr + "|" + getBasePkmnForm() + " => " + this.evolutionIdStr + "|" + getEvolutionForm();
	}
}