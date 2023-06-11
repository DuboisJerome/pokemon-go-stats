package com.pokemongostats.controller.external.json.transformer;

import com.pokemongostats.controller.external.Log;
import com.pokemongostats.controller.utils.CollectionUtils;
import com.pokemongostats.model.external.json.MoveParserJson;

import java.util.List;

public class TransformerJsonMove {

	public static int SIZE_ID_MOVE = 4;

	public static List<MoveParserJson> mergeMoveAreneAndCombat(List<MoveParserJson> lstMoveArene, List<MoveParserJson> lstMoveCombat) {
		lstMoveArene.forEach(moveArene -> {
			// find any pkmn-move with this id
			var found = CollectionUtils.find(lstMoveCombat, moveCombat -> moveCombat.getId() == moveArene.getId());
			if (found != null) {
				moveArene.setPowerPvp(found.getPowerPvp());
				moveArene.setEnergyPvp(found.getEnergyPvp());
				moveArene.setDurationPvp(found.getDurationPvp());
			} else {
				Log.warn("Information Move(Combat) non trouvée pour le move : " + moveArene);
			}
		});
		return lstMoveArene;
	}
}