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
	private List<PkmnMoveParserJson> lstPkmnMove = new ArrayList<>();
	private List<EvolutionParserJson> lstEvol = new ArrayList<>();

	public PkmnDescParserJson(){
		super();
	}

	public PkmnDescParserJson(PkmnDescParserJson p) {
		super(p);
		this.setIdStr(p.idStr);
	}

	public void setIdStr(String idStr){
		this.idStr = idStr;
		this.i18n.setName(idStr);
	}

	public void addAllPkmnMove(List<PkmnMoveParserJson> l) {
		this.lstPkmnMove.addAll(l);
	}

	public void addAllEvol(List<EvolutionParserJson> l) {
		this.lstEvol.addAll(l);
	}
}