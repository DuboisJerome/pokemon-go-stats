package com.pokemongostats.controller.external.json.transformer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.model.external.json.PkmnDescParserJson;
import com.pokemongostats.model.external.json.EvolutionParserJson;
import com.pokemongostats.model.external.json.PkmnI18NParserJson;
import com.pokemongostats.model.external.json.PkmnMoveParserJson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TransformerJsonForm {

	private static final String NORMAL_TEMP = "NORMAL_TEMP";

	private static final Set<Integer> pkmnIdExcluNonNormal = new HashSet<>();

	static {
		// Pikachu
		pkmnIdExcluNonNormal.add(25);
		// Lépidonille
		pkmnIdExcluNonNormal.addAll(Set.of(664, 665, 666));
		// Flabébé
		pkmnIdExcluNonNormal.addAll(Set.of(669, 670, 671));
		// Coafarel
		pkmnIdExcluNonNormal.add(676);
	}

	public static String toForm(String pokemonId, JsonObject obj, String field) {
		JsonElement elem = obj.get(field);
		return toForm(pokemonId, elem);
	}

	private static String toForm(String pokemonId, JsonElement elem) {
		if (elem == null) {
			return NORMAL_TEMP;
		}
		return toForm(pokemonId, elem.getAsString());
	}

	private static String toForm(String pokemonId, String form) {
		if (form == null || form.isEmpty()) {
			return NORMAL_TEMP;
		}
		String res;
		var idx = form.indexOf(pokemonId);
		if (idx < 0) {
			idx = form.indexOf("_");
			if (idx < 0) {
				Log.warn("Impossible de trouver un underscore dans la form "+ form+" pour le pkmn d'id "+pokemonId+", elle est donc prise telle quelle");
				res = form;
			} else {
				res = form.substring(idx + 1);
				if (res.contains("_")) {
					Log.warn("Il reste des underscore dans la form "+res+" pour le pkmn d'id "+pokemonId+", on garde donc la form de base "+ form);
					res = form;
				}
			}
			Log.info("La forme "+form+" ne contient pas l'id "+ pokemonId+ ", la forme calculée "+ res);
		} else {
			res = form.substring(idx + pokemonId.length() + 1);
		}
		return res;
	}

	public static boolean isFormExclu(String form) {
		boolean isExclu = form == null || form.isEmpty() || "SHADOW".equals(form) || "S".equals(form) || "PURIFIED".equals(form) || form.contains("201") || form.contains("202");
		if (isExclu) {
			Log.info("La forme "+ form+" fait partie des formes exclues");
		}
		return isExclu;
	}

	private static boolean isPkmnFormExclu(long id, String form) {
		boolean isExclu;
		// All force normal temp
		isExclu = isPkmnFormExcluNonNormal(id, form);
		// Cheniti
		isExclu |= isPkmnFormExclu(id, form, 412, "PLANT");
		// Vivaldaim
		isExclu |= isPkmnFormExclu(id, form, 585, "SPRING");
		isExclu |= isPkmnFormExclu(id, form, 586, "SPRING");
		// Pitrouille, on exclue la forme normal
		isExclu |= (id == 710 || id == 711) && form.equals(NORMAL_TEMP);
		// Météno
		isExclu |= isPkmnFormExclu(id, form, 774, "BLUE");

		if (isExclu) {
			Log.info("La forme "+ form+" pour le pkmn d'id"+id+" fait partie des formes exclues");
		}
		return isExclu;
	}

	private static boolean isPkmnFormExcluNonNormal(long id, String form) {
		return pkmnIdExcluNonNormal.stream().anyMatch(idAttendu -> isPkmnFormExclu(id, form, idAttendu, NORMAL_TEMP));
	}

	private static boolean isPkmnFormExclu(long id, String form, long idAttendu, String formAttendu) {
		return id == idAttendu && !form.equals(formAttendu);
	}

	// TODO déplacer ?
	public static List<PkmnDescParserJson> cleanForms(List<PkmnDescParserJson> lst) {
		Log.info("Supprime les formes des pokemons qui ne doivent pas être incluses dans l'application");
		// Filtre les formes qui ne sont pas de type NORMAL_TEMP OU qui le sont mais qu'il n'y a aucun autre element de la liste pour le meme id
		Map<Long, List<PkmnDescParserJson>> mapElemById = lst.stream().collect(Collectors.groupingBy(PkmnDescParserJson::getPokedexNum));
		List<PkmnDescParserJson> lstAllConserver = new ArrayList<>();
		mapElemById.forEach((id, lstElemById) -> {
			// Supprime les formes exclues
			lstElemById.forEach(p -> {
				var form = p.getForm();
				var pkmnNum = p.getPokedexNum();
				if (isFormExclu(form) || isPkmnFormExclu(pkmnNum, form)) {
					return;
				}
				// La forme récupéré n'est pas NORMAL_TEMP => elle doit forcement passer le filtre
				// OU S'il n'y a qu'une seule forme, c'est forcement la courante NORMAL_TEMP => On la conserve
				if (!NORMAL_TEMP.equals(form) || lstElemById.size() == 1) {
					lstAllConserver.add(p);
					return;
				}

				// On va regarder les autres formes de la liste
				// S'il n'y en a aucune, on conserve la forme normal, sinon on l'exclu

				var lstFormsStream = lstElemById.stream()
						// Exclue de la liste l'objet courant en cours de lecture
						.filter(e -> e != p)
						// Dans le cas ou il y aurait plusieurs NORMAL_TEMP dans la liste de départ, on exclue les autres
						.filter(e -> !NORMAL_TEMP.equals(e.getForm()))
						// S'il existe une forme MEGA il faut garder la forme NORMAL_TEMP
						.filter(e -> !e.getForm().startsWith("MEGA") && !e.getForm().startsWith("PRIMAL"))
						// Recup les formes
						.map(PkmnDescParserJson::getForm);

				// On conserve normal temp si aucune autre forme est trouvée
				boolean isDoitConserverNormalTemp = !lstFormsStream.distinct().findAny().isPresent();
				if (isDoitConserverNormalTemp) {
					lstAllConserver.add(p);
				}
			});
		});
		return lstAllConserver.stream().peek(p -> {
			// Les NORMAL_TEMP n'ont pas d'autres formes
			updateForm(p, PkmnDescParserJson::getForm, PkmnDescParserJson::setForm);
			p.getLstPkmnI18N().forEach(p2 -> {
				updateForm(p2, PkmnI18NParserJson::getForm, PkmnI18NParserJson::setForm);
			});
			p.getLstEvol().forEach(p2 -> {
				updateForm(p2, EvolutionParserJson::getBasePkmnForm, EvolutionParserJson::setBasePkmnForm);
				updateForm(p2, EvolutionParserJson::getEvolutionForm, EvolutionParserJson::setEvolutionForm);
			});
			p.getLstPkmnMove().forEach(p2 -> {
				updateForm(p2, PkmnMoveParserJson::getForm, PkmnMoveParserJson::setForm);
			});
		}).collect(Collectors.toList());
	}

	private static <T> void updateForm(T p, Function<T, String> getterForm, BiConsumer<T, String> setterForm){
		var form = getterForm.apply(p);
		if (NORMAL_TEMP.equals(form)) {
			setterForm.accept(p, "NORMAL");
		}
	}


}