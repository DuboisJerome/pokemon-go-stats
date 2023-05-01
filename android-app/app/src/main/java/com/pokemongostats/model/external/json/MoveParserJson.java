package com.pokemongostats.model.external.json;

import com.pokemongostats.model.bean.bdd.Move;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveParserJson extends Move {
	private String idStr;
	private List<MoveI18NParserJson> lstMoveI18N = new ArrayList<>();

	public void setIdStr(String idStr){
		this.idStr = idStr;
		setName(idStr);
	}

	public void addAllMoveI18N(List<MoveI18NParserJson> lstMoveI18N) {
		this.lstMoveI18N.addAll(lstMoveI18N);
	}
}