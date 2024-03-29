package com.pokemongostats.controller.external.json.splitter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.pokemongostats.controller.external.AbstractSplitter;
import com.pokemongostats.controller.external.Log;
import com.pokemongostats.controller.external.ParserException;
import com.pokemongostats.model.bean.UpdateStatus;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import lombok.Getter;

/**
 * Split le fichier json d'entrée par catégorie
 */
@Getter
public abstract class AbstractSplitterJson<I> extends AbstractSplitter<I,JsonObject> {

	private Grp<JsonObject> creerGrp(String name, Predicate<JsonObject> funcFilter) {
		var grp = new Grp<JsonObject>();
		grp.setName(name);
		grp.setFuncFilter(funcFilter);
		return grp;
	}

	private Predicate<JsonObject> funcTemplateIdStartWith(String prefix) {
		return bloc -> bloc.get("templateId").getAsString().startsWith(prefix);
	}

	@Override
    protected List<Grp<JsonObject>> initGrps() {
		List<Grp<JsonObject>> lstGrp = new ArrayList<>();
		// Move
		var grp = creerGrp(GRP_MOVE_COMBAT, funcTemplateIdStartWith("COMBAT_V"));
		lstGrp.add(grp);
		var regexMovePve = "^V\\d{4}_MOVE_.*";
		Pattern patternMovePve = Pattern.compile(regexMovePve);
		grp = creerGrp(GRP_MOVE_ARENE, bloc -> {
			String txt = bloc.get("templateId").getAsString();
			return patternMovePve.matcher(txt).matches();
		});
		lstGrp.add(grp);
		// List forms
		grp = creerGrp(GRP_FORMS, funcTemplateIdStartWith("FORMS_V"));
		lstGrp.add(grp);
		// Pkmn + Pkmn move + evolutionBranch + candy + buddy
		var regexPkmn = "^V\\d{4}_POKEMON_.*";
		Pattern patternPkmn = Pattern.compile(regexPkmn);
		grp = creerGrp(GRP_PKMN, bloc -> {
			String txt = bloc.get("templateId").getAsString();
			return patternPkmn.matcher(txt).matches();
		});
		lstGrp.add(grp);
		return lstGrp;
	}

	protected void tryAdd(JsonObject bloc, Grp<JsonObject> grp) {
		boolean test = grp.filter(bloc);
		if (test) {
			try {
				JsonElement data = bloc.get("data");
				if (data.isJsonObject()) {
					JsonObject blocJson = data.getAsJsonObject();
					grp.addItem(blocJson);
				} else {
					Log.warn("Data n'est pas un obj json : " + data);
				}
			} catch (Exception e) {
				Log.error("Impossible de parser le bloc " + bloc.toString(), e);
			}
		}
	}

	@Override
    public void split(I in, UpdateStatus us) throws ParserException {
		this.lstGrp.clear();

		us.startingEtape("Adding groups");
		List<Grp<JsonObject>> l = initGrps();
		this.lstGrp.addAll(l);
		us.finnishEtape(10);

		us.startingEtape("Reading input");
		readJsonFromInput(in, us);
		us.finnishEtape(100);

	}

	protected abstract Reader toReader(I in) throws ParserException;

	protected void readJsonFromInput(I in, UpdateStatus us) throws ParserException {
		try (JsonReader reader = new JsonReader(toReader(in))) {
			reader.beginArray();
			Gson gson = new Gson();
			int nbBloc = 0;
			int nbMaxBloc = 10000;
			while (reader.hasNext()) {
				JsonObject bloc = gson.fromJson(reader, JsonObject.class);
				this.lstGrp.forEach((grp) -> tryAdd(bloc, grp));
				us.updateProgressionGlobale((int) (nbBloc++ * 100D / nbMaxBloc));
			}
			reader.endArray();
		} catch (IOException e) {
			throw new ParserException(e);
		}
	}
}