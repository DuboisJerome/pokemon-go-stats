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
	public void setIdStr(String idStr){
		this.idStr = idStr;
		getI18n().setName(idStr);
	}
}