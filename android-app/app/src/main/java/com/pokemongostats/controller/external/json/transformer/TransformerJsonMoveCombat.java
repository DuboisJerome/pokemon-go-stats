package com.pokemongostats.controller.external.json.transformer;

import com.google.gson.JsonObject;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.controller.external.json.JsonUtils;
import com.pokemongostats.controller.external.ParserException;
import com.pokemongostats.model.external.json.MoveParserJson;
import com.pokemongostats.model.bean.bdd.transformer.ITransformerSimple;

import org.apache.commons.lang3.math.NumberUtils;

public class TransformerJsonMoveCombat implements ITransformerSimple<JsonObject,MoveParserJson> {

	private static final String prefixIdPvp = "COMBAT_V";
	private static final int sizePrefixIdPvp = prefixIdPvp.length();
	@Override
	public MoveParserJson transform(JsonObject bloc) throws ParserException {
		Log.debug("Création d'un Move(Combat) à partir de " + bloc);
		var templateId = bloc.get("templateId").getAsString();
		var data = bloc.get("combatMove").getAsJsonObject();
		var move = new MoveParserJson();
		move.setId(NumberUtils.toInt(templateId.substring(sizePrefixIdPvp, sizePrefixIdPvp + 4)));
		move.setIdStr(data.get("uniqueId").getAsString());
		move.setType(TransformerJsonType.toType(data.get("type")));
		move.setPowerPvp(JsonUtils.getAsInt(data, "power"));
		move.setEnergyPvp(JsonUtils.getAsInt(data, "energyDelta"));
		move.setDurationPvp(data.has("durationTurns") ? JsonUtils.getAsInt(data, "durationTurns") : 0);
		Log.debug("Move créé : " + move);
		return move;
	}
}