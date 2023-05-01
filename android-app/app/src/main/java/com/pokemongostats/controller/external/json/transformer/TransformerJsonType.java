package com.pokemongostats.controller.external.json.transformer;

import com.google.gson.JsonElement;
import com.pokemongostats.model.bean.Type;
import com.pokemongostats.controller.external.ParserException;
import com.pokemongostats.model.bean.bdd.transformer.ITransformerSimple;

public class TransformerJsonType implements ITransformerSimple<JsonElement, Type> {

	private static final TransformerJsonType instance = new TransformerJsonType();

	public static Type toType(JsonElement elem) throws ParserException {
		return instance.transform(elem);
	}

	@Override
	public Type transform(JsonElement elem) throws ParserException {
		if (elem == null) return null;
		String type = elem.getAsString();
		if (type == null || type.isEmpty()) return null;
		return Type.valueOf(type.substring("POKEMON_TYPE_".length()));
	}
}