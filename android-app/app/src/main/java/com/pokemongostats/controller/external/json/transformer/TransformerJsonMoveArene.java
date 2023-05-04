package com.pokemongostats.controller.external.json.transformer;

import com.google.gson.JsonObject;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.controller.external.json.JsonUtils;
import com.pokemongostats.model.external.json.MoveI18NParserJson;
import com.pokemongostats.model.external.json.MoveParserJson;
import com.pokemongostats.controller.utils.CollectionUtils;
import com.pokemongostats.controller.utils.LangUtils;
import com.pokemongostats.controller.external.ParserException;
import com.pokemongostats.controller.external.transformer.ITransformerSimple;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TransformerJsonMoveArene implements ITransformerSimple<JsonObject,MoveParserJson> {
	@Override
	public MoveParserJson transform(JsonObject bloc) throws ParserException {
		var data = bloc.get("moveSettings").getAsJsonObject();
		var templateId = bloc.get("templateId").getAsString();
		MoveParserJson move = new MoveParserJson();
		move.setId(NumberUtils.toInt(templateId.substring(1, 1 + 4)));
		move.setIdStr(data.get("movementId").getAsString());
		move.setType(TransformerJsonType.toType(data.get("pokemonType")));
		move.setPower(JsonUtils.getAsInt(data, "power"));
		move.setStaminaLossScalar(JsonUtils.getAsDouble(data, "staminaLossScalar"));
		move.setDuration(JsonUtils.getAsInt(data, "durationMs"));
		move.setEnergyDelta(JsonUtils.getAsInt(data, "energyDelta"));
		move.setCriticalChance(JsonUtils.getAsDouble(data, "criticalChance"));

		var lstMoveI18N = LangUtils.LST_LANG_GEREES.stream().map(l -> {
			var movei18n = new MoveI18NParserJson();
			movei18n.setId(move.getId());
			movei18n.setName(move.getIdStr());
			movei18n.setLang(l);
			return movei18n;
		}).collect(Collectors.toList());
		move.addAllMoveI18N(lstMoveI18N);
		Log.debug("Move(Arene) créé : " + move);
		return move;
	}

	// TODO deplacer dans TransformerJsonMove ou MergerMove ?
	public static List<MoveParserJson> mergeMoveAreneAndCombat(List<MoveParserJson> lstMoveArene, List<MoveParserJson> lstMoveCombat) {
		lstMoveArene.forEach(moveArene -> {
			// find any pkmn-move with this id
			var found = CollectionUtils.find(lstMoveCombat, moveCombat -> moveCombat.getId() == moveArene.getId());
			if (found != null) {
				moveArene.setPowerPvp(found.getPowerPvp());
				moveArene.setEnergyPvp(found.getEnergyPvp());
				moveArene.setDurationPvp(found.getDurationPvp());
			} else {
				Log.warn("Information Move(Combat) non trouvée pour le move : "+moveArene);
			}
		});
		return lstMoveArene;
	}
}