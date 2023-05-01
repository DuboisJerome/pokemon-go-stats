package com.pokemongostats.model.external.json;

public class MoveI18NParserJson {
	public long id = -1;
	public String lang;
	public String name;

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