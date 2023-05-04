package com.pokemongostats.controller.external.json.transformer;

import com.google.gson.JsonObject;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.controller.utils.PkmnTags;
import com.pokemongostats.model.external.json.PkmnDescParserJson;
import com.pokemongostats.controller.utils.LangUtils;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.external.json.EvolutionParserJson;
import com.pokemongostats.controller.external.ParserException;
import com.pokemongostats.controller.external.json.JsonUtils;
import com.pokemongostats.model.external.json.PkmnI18NParserJson;
import com.pokemongostats.model.external.json.PkmnMoveParserJson;
import com.pokemongostats.controller.external.transformer.ITransformerMultiple;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TransformerJsonPkmnDesc implements ITransformerMultiple<JsonObject,PkmnDescParserJson> {

	@Override
	public List<PkmnDescParserJson> transform(JsonObject bloc) throws ParserException {
		var p = toPkmnBase(bloc);
		List<PkmnDescParserJson> listPkmn = new ArrayList<>();
		if (p == null || TransformerJsonForm.isFormExclu(p.getForm())) {
			return listPkmn;
		}
		var data = bloc.get("pokemonSettings").getAsJsonObject();
		completePkmnData(p, data);
		// I18N
		completePkmnI18N(p);
		// PkmnMove
		completePkmnMove(p, data);
		// Evols classique
		completeEvolClassique(p, data);

		Log.debug("PkmnDesc créé " + p);
		listPkmn.add(p);

		// MEGA
		if (data.has("tempEvoOverrides")) {
			List<PkmnDescParserJson> lstPkmnEvol = new ArrayList<>();
			List<EvolutionParserJson> lstEvol = new ArrayList<>();
			for(var e2 : data.get("tempEvoOverrides").getAsJsonArray()) {
				JsonObject e = (JsonObject) e2;
				if (!e.has("tempEvoId")) {
					// Il y a peu visiblement y avoir des objets dans l'array tempEvoOverrides
					// qui ne sont pas des evolutions. On les exclues
					continue;
				}
				PkmnDescParserJson pkmnEvol = new PkmnDescParserJson(p);
				pkmnEvol.setType1(TransformerJsonType.toType(e.get("typeOverride1")));
				pkmnEvol.setType2(TransformerJsonType.toType(e.get("typeOverride2")));
				pkmnEvol.setStamina(JsonUtils.getAsInt(e.get("stats").getAsJsonObject(), "baseStamina"));
				pkmnEvol.setAttack(JsonUtils.getAsInt(e.get("stats").getAsJsonObject(), "baseAttack"));
				pkmnEvol.setDefense(JsonUtils.getAsInt(e.get("stats").getAsJsonObject(), "baseDefense"));
				pkmnEvol.setForm(TransformerJsonForm.toForm("TEMP_EVOLUTION", e, "tempEvoId"));
				pkmnEvol.getTags().add("MEGA");

				completePkmnI18N(pkmnEvol);

				// PkmnMove : relie les data, on pourrait aussi copié les moves et changer la form
				completePkmnMove(pkmnEvol, data);

				Log.debug("PkmnDesc méga évol créé " + pkmnEvol);
				lstPkmnEvol.add(pkmnEvol);

				var ev = new EvolutionParserJson();
				ev.setPkmnBase(p);
				ev.setPkmnEvol(pkmnEvol);
				ev.setTemporaire(true);
				lstEvol.add(ev);
				Log.debug("Evolution méga evol créé " + ev);

			}
			p.addAllEvol(lstEvol);

			listPkmn.addAll(lstPkmnEvol);
		}
		return listPkmn;
	}

	private static PkmnDescParserJson toPkmnBase(JsonObject bloc) {
		// exclu des spécificité pokemon home
		if (bloc.has("pokemonHomeFormReversions")) {
			Log.debug("Pkmn exclu car contient la propriété pokemonHomeFormReversions" + bloc);
			return null;
		}
		var templateId = bloc.get("templateId").getAsString();
		var data = bloc.get("pokemonSettings").getAsJsonObject();
		long pokedex_num = NumberUtils.toLong(templateId.substring(1, 1 + 4));
		PkmnDescParserJson pkmn = new PkmnDescParserJson();
		pkmn.setId(pokedex_num);
		pkmn.setIdStr(data.get("pokemonId").getAsString());
		pkmn.setForm(TransformerJsonForm.toForm(pkmn.getIdStr(), data, "form"));
		return pkmn;
	}

	private static void completePkmnData(PkmnDescParserJson p, JsonObject data) throws ParserException {
		p.setType1(TransformerJsonType.toType(data.get("type")));
		p.setType2(TransformerJsonType.toType(data.get("type2")));
		p.setKmsPerCandy(data.get("kmBuddyDistance").getAsDouble());
		// Stats
		var stats = data.get("stats").getAsJsonObject();
		p.setStamina(JsonUtils.getAsInt(stats, "baseStamina"));
		p.setAttack(JsonUtils.getAsInt(stats, "baseAttack"));
		p.setDefense(JsonUtils.getAsInt(stats, "baseDefense"));
		if (data.has("pokemonClass")) {
			String pkmnClass = data.get("pokemonClass").getAsString();
			p.getTags().add(pkmnClass.substring("POKEMON_CLASS_".length()));
		}
//		if(!data.has("isDeployable")){
//			p.getTags().add(PkmnTags.NOT_IN_GAME);
//		}
	}

	private static void completePkmnI18N(PkmnDescParserJson p) {
		var lstPkmni18n = LangUtils.LST_LANG_GEREES.stream().map(l -> {
			var pkmnI18N = new PkmnI18NParserJson();
			pkmnI18N.id = p.getId();
			pkmnI18N.lang = l;
			pkmnI18N.form = p.getForm();
			pkmnI18N.name = p.getIdStr() + " " + p.getForm();
			return pkmnI18N;
		}).collect(Collectors.toList());
		p.addAllI18N(lstPkmni18n);
	}
	private void completePkmnMove(PkmnDescParserJson p, JsonObject data) {
		var lstPkmnMove = new ArrayList<PkmnMoveParserJson>();
		lstPkmnMove.addAll(TransformerJsonPkmnMove.toPkmnMove(p, data.get("quickMoves"), Move.MoveType.QUICK, false));
		lstPkmnMove.addAll(TransformerJsonPkmnMove.toPkmnMove(p, data.get("cinematicMoves"), Move.MoveType.CHARGE, false));
		lstPkmnMove.addAll(TransformerJsonPkmnMove.toPkmnMove(p, data.get("eliteQuickMove"), Move.MoveType.QUICK, true));
		lstPkmnMove.addAll(TransformerJsonPkmnMove.toPkmnMove(p, data.get("eliteCinematicMove"), Move.MoveType.CHARGE, true));
		if (lstPkmnMove.isEmpty()) {
			Log.warn("Aucun Move trouvée pour "+ p + " à partir de "+ data);
		}
		p.addAllPkmnMove(lstPkmnMove);
	}

	private void completeEvolClassique(PkmnDescParserJson p, JsonObject data) {
		var listEvol = new ArrayList<EvolutionParserJson>();
		if (data.has("evolutionBranch")) {
			listEvol.addAll(StreamSupport.stream(data.get("evolutionBranch").getAsJsonArray().spliterator(), false)
					.map(je -> (JsonObject) je)
					// exclu les mega
					.filter(evBranche -> evBranche.has("evolution"))
					.map(evBranche -> TransformerJsonEvol.toEvol(evBranche, p))
					.filter(ev -> ev.getEvolutionIdStr() != null
							&& !ev.getEvolutionIdStr().isEmpty()
							&& !TransformerJsonForm.isFormExclu(ev.getEvolutionForm()))
					.collect(Collectors.toList()));
		}
		p.addAllEvol(listEvol);
	}

}