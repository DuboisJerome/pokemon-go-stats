package com.pokemongostats.controller.external.json.transformer;

import com.google.gson.JsonObject;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.controller.external.json.JsonUtils;
import com.pokemongostats.controller.utils.CollectionUtils;
import com.pokemongostats.model.external.json.EvolutionParserJson;
import com.pokemongostats.model.external.json.PkmnDescParserJson;

import java.util.List;

public class TransformerJsonEvol {

	public static EvolutionParserJson toEvol(JsonObject evBranche, PkmnDescParserJson p) {
		var ev = new EvolutionParserJson();
		ev.setPkmnBase(p);
		ev.setEvolutionId(-1);
		ev.setEvolutionIdStr(evBranche.get("evolution").getAsString());
		ev.setEvolutionForm(TransformerJsonForm.toForm(ev.getEvolutionIdStr(), evBranche, "form"));
		ev.setCandyToEvolve(JsonUtils.getAsInt(evBranche, "candyCost"));
//            ev.object_to_evolve=evBranche.evolutionItemRequirement;
//            ev.gender_requirement=evBranche.genderRequirement;
//            ev.lure_item_requirement=evBranche.lureItemRequirement;
		Log.debug("Evolution créé " + ev);
		return ev;
	}

	public static void updateLstEvol(List<PkmnDescParserJson> listPkmn) {
		Log.info("Mise à jour des Id des évolutions des pokemons avec le bon numéro de pokedex");
		listPkmn.forEach(p -> p.getLstEvol().forEach(ev -> {
			PkmnDescParserJson pkmnEv = CollectionUtils.find(listPkmn, p2 -> p2.getIdStr().equals(ev.getEvolutionIdStr()) && p2.getForm().equals(ev.getEvolutionForm()));
			if (pkmnEv != null) {
				Log.debug("Mise à jour de l'évolution " + ev + " avec l'id " + pkmnEv.getId());
				ev.setEvolutionId(pkmnEv.getId());
			} else {
				Log.warn("Impossible de trouver le pokémon correspondant à l'évolution " + ev);
				ev.setEvolutionId(-1);
			}
		}));
	}
}