package com.pokemongostats.controller.external.json.transformer;

import com.google.gson.JsonObject;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.controller.external.json.JsonUtils;
import com.pokemongostats.controller.external.transformer.ITransformerSimple;
import com.pokemongostats.controller.utils.LangUtils;
import com.pokemongostats.model.bean.bdd.MoveI18N;
import com.pokemongostats.model.external.json.MoveParserJson;

import org.apache.commons.lang3.math.NumberUtils;

public class TransformerJsonMoveArene implements ITransformerSimple<JsonObject,MoveParserJson> {
	@Override
	public MoveParserJson transform(JsonObject bloc) {
		var templateId = bloc.get("templateId").getAsString();
		var data = bloc.get("moveSettings").getAsJsonObject();
		MoveParserJson move = new MoveParserJson();
		move.setId(NumberUtils.toInt(templateId.substring(1, 1 + +TransformerJsonMove.SIZE_ID_MOVE)));
		move.setIdStr(data.get("movementId").getAsString());
		move.setType(TransformerJsonType.toType(data.get("pokemonType")));
		move.setPower(JsonUtils.getAsInt(data, "power"));
		move.setStaminaLossScalar(JsonUtils.getAsDouble(data, "staminaLossScalar"));
		move.setDuration(JsonUtils.getAsInt(data, "durationMs"));
		move.setEnergyDelta(JsonUtils.getAsInt(data, "energyDelta"));
		move.setCriticalChance(JsonUtils.getAsDouble(data, "criticalChance"));

		MoveI18N i18n = move.getI18n();
		i18n.setId(move.getId());
		i18n.setName(move.getIdStr());
		i18n.setLang(LangUtils.LANG_FR);
		Log.debug("Move(Arene) créé : " + move);
		return move;
	}
}