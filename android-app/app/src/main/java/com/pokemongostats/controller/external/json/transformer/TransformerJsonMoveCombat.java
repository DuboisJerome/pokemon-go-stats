package com.pokemongostats.controller.external.json.transformer;

import com.google.gson.JsonObject;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.controller.external.json.JsonUtils;
import com.pokemongostats.controller.external.transformer.ITransformerSimple;
import com.pokemongostats.model.external.json.MoveParserJson;

import org.apache.commons.lang3.math.NumberUtils;

public class TransformerJsonMoveCombat implements ITransformerSimple<JsonObject,MoveParserJson> {

	private static final String prefixIdPvp = "COMBAT_V";
	private static final int sizePrefixIdPvp = prefixIdPvp.length();

	@Override
	public MoveParserJson transform(JsonObject bloc) {
		var templateId = bloc.get("templateId").getAsString();
		var data = bloc.get("combatMove").getAsJsonObject();
		MoveParserJson move = new MoveParserJson();
		move.setId(NumberUtils.toInt(templateId.substring(sizePrefixIdPvp, sizePrefixIdPvp + TransformerJsonMove.SIZE_ID_MOVE)));
		move.setIdStr(data.get("uniqueId").getAsString());
		move.setType(TransformerJsonType.toType(data.get("type")));
		move.setPowerPvp(JsonUtils.getAsInt(data, "power"));
		move.setEnergyPvp(JsonUtils.getAsInt(data, "energyDelta"));
		move.setDurationPvp(data.has("durationTurns") ? JsonUtils.getAsInt(data, "durationTurns") : 0);
		Log.debug("Move(Combat) créé : " + move);
		return move;
	}
}