package com.pokemongostats.model.external.json;

import com.pokemongostats.model.bean.bdd.PkmnDesc;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PkmnDescParserJson extends PkmnDesc {
	private String idStr;
	private List<PkmnI18NParserJson> lstPkmnI18N = new ArrayList<>();
	private List<PkmnMoveParserJson> lstPkmnMove = new ArrayList<>();
	private List<EvolutionParserJson> lstEvol = new ArrayList<>();

	public PkmnDescParserJson(){
		super();
	}

	public PkmnDescParserJson(PkmnDescParserJson p) {
		this.id = p.id;
		this.idStr = p.idStr;
		this.form = p.form;
		this.type1 = p.type1;
		this.type2 = p.type2;
		this.kmsPerCandy = p.kmsPerCandy;
		this.stamina = p.stamina;
		this.attack = p.attack;
		this.defense = p.defense;
		this.name = p.name;
		this.tags.addAll(p.tags);
	}

	public void setIdStr(String idStr){
		this.idStr = idStr;
		this.name = idStr;
	}

	public void addAllI18N(List<PkmnI18NParserJson> l) {
		lstPkmnI18N.addAll(l);
	}

	public void addAllPkmnMove(List<PkmnMoveParserJson> l) {
		lstPkmnMove.addAll(l);
	}

	public void addAllEvol(List<EvolutionParserJson> l) {
		this.lstEvol.addAll(l);
	}
}