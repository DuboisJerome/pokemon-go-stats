package com.pokemongostats.model.external.json;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PkmnI18NParserJson {
	public long id = -1;
	public String lang;
	public String form;
	public String name;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PkmnI18NParserJson{");
		sb.append("id=").append(id);
		sb.append(", lang='").append(lang).append('\'');
		sb.append(", form='").append(form).append('\'');
		sb.append(", name='").append(name).append('\'');
		sb.append('}');
		return sb.toString();
	}
}