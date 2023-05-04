package com.pokemongostats.model.external.json;

import com.pokemongostats.model.bean.bdd.MoveI18N;

public class MoveI18NParserJson extends MoveI18N {

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("MoveI18NParserJson{");
		sb.append("id=").append(id);
		sb.append(", lang='").append(lang).append('\'');
		sb.append(", name='").append(name).append('\'');
		sb.append('}');
		return sb.toString();
	}
}