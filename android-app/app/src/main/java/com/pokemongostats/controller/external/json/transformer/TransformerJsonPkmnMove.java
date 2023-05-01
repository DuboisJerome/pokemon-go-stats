package com.pokemongostats.controller.external.json.transformer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.model.external.json.PkmnDescParserJson;
import com.pokemongostats.controller.utils.CollectionUtils;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.external.json.MoveParserJson;
import com.pokemongostats.model.external.json.PkmnMoveParserJson;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TransformerJsonPkmnMove {

	public static List<PkmnMoveParserJson> toPkmnMove(PkmnDescParserJson p, JsonElement elem, Move.MoveType moveType, boolean isElite) {
		if (elem != null && elem.isJsonArray()) {
			JsonArray listMoveName = elem.getAsJsonArray();
			return StreamSupport.stream(listMoveName.spliterator(), false).map(m -> {
				var pkmnMove = new PkmnMoveParserJson();
				pkmnMove.setPkmn(p);
				pkmnMove.setMoveIdStr(m.getAsString());
				pkmnMove.setMoveId(-1);
				pkmnMove.setMoveType(moveType);
				pkmnMove.setElite(isElite);
				Log.debug("PkmnMoveParserJson créé : "+ pkmnMove);
				return pkmnMove;
			}).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	public static void updateLstPkmnMove(List<PkmnDescParserJson> lstPkmn, List<MoveParserJson> lstMove) {
		List<PkmnMoveParserJson> lstPkmnMove = lstPkmn.stream().flatMap(p -> p.getLstPkmnMove().stream()).collect(Collectors.toList());
		// Met à jour l'id du move des pkmnmove
		Log.info("Mise à jour des ids des PkmnMove des PkmnDesc avec le bon numéro d'id de Move");
		lstPkmnMove.forEach(pm ->{
			// Find move by id str
			var move = CollectionUtils.find(lstMove, m -> m.getIdStr().equals(pm.getMoveIdStr()));
			// update pkmn-move
			if (move != null) {
				Log.debug("Mise à jour du PkmnMove "+ pm+" avec l'id "+move.getId());
				pm.setMoveId(move.getId());
			} else {
				pm.setMoveId(-1);
				Log.warn("Aucun Move trouvé pour "+ pm+" impossible de déterminer l'id réel du Move");
			}
		});
		Log.info("Mise à jour des MoveType des Move avec le type QUICK/CHARGE");
		lstMove.forEach(m -> {
			// find any pkmn-move with this id
			var pkmnMove = CollectionUtils.find(lstPkmnMove, pm -> m.getIdStr().equals(pm.getMoveIdStr()));
			// update move
			if (pkmnMove != null) {
				Log.debug("Mise à jour du Move "+ m+" avec le type "+pkmnMove.getMoveType());
				m.setMoveType(pkmnMove.getMoveType());
			} else {
				m.setMoveType(Move.MoveType.UNKNOWN);
				Log.warn("Aucun PkmnMove trouvé pour "+ m+" impossible de déterminer le MoveType");
			}
		});
	}
}