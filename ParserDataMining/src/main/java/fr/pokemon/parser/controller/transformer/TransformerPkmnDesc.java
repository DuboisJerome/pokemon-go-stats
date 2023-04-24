package fr.pokemon.parser.controller.transformer;

import com.google.gson.JsonObject;
import fr.pokemon.parser.controller.Logger;
import fr.pokemon.parser.model.PkmnDesc;
import fr.pokemon.parser.model.PkmnI18N;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransformerPkmnDesc {
    public static PkmnDesc toPkmnBase(JsonObject bloc) {
        // exclu des spécificité pokemon home
        if (bloc.has("pokemonHomeFormReversions")) {
            Logger.debug("pkmn exclu home form reversions", bloc);
            return null;
        }
        var templateId = bloc.get("templateId").getAsString();
        var data = bloc.get("pokemonSettings").getAsJsonObject();
        var pokedex_num = NumberUtils.toInt(templateId.substring(1, 1 + 4));
        PkmnDesc pkmn = new PkmnDesc();
        pkmn.id = pokedex_num;
        pkmn.idStr = data.get("pokemonId").getAsString();
        pkmn.form = TransformerForm.toForm(pkmn.idStr, data, "form");
        Logger.debug("toPkmnBase", pkmn.toStringLight());
        return pkmn;
    }

    public static List<PkmnDesc> toPkmn(JsonObject bloc) {
        var pkmn = toPkmnBase(bloc);
        List<PkmnDesc> listPkmn = new ArrayList<>();
        if (pkmn == null || TransformerForm.isFormExclu(pkmn.form)) {
            Logger.debug("pkmn exclu", pkmn);
            return listPkmn;
        }
        var data = bloc.get("pokemonSettings").getAsJsonObject();
        pkmn.type1 = TransformerType.toType(data, "type");
        pkmn.type2 = TransformerType.toType(data, "type2");
        //pkmn.kms_per_egg=data.;
        pkmn.kmsPerCandy = data.get("kmBuddyDistance").getAsDouble();
        var stats = data.get("stats").getAsJsonObject();
        pkmn.stamina = JsonUtils.getAsInt(stats, "baseStamina");
        pkmn.attack = JsonUtils.getAsInt(stats, "baseAttack");
        pkmn.defense = JsonUtils.getAsInt(stats, "baseDefense");
        if (data.has("pokemonClass")) {
            String pkmnClass = data.get("pokemonClass").getAsString();
            pkmn.tags.add(pkmnClass.substring("POKEMON_CLASS_".length()));
        }
        listPkmn.add(pkmn);
        if (data.has("tempEvoOverrides")) {
            data.get("tempEvoOverrides").getAsJsonArray().forEach(e2 -> {
                JsonObject e = (JsonObject) e2;
                if (!e.has("tempEvoId")) {
                    // Il y a peu visiblement y avoir des objets dans l'array tempEvoOverrides
                    // qui ne sont pas des evolutions. On les exclues
                    return;
                }
                PkmnDesc ev = new PkmnDesc(pkmn);
                ev.type1 = TransformerType.toType(e, "typeOverride1");
                ev.type2 = TransformerType.toType(e, "typeOverride2");
                ev.stamina = JsonUtils.getAsInt(e.get("stats").getAsJsonObject(), "baseStamina");
                ev.attack = JsonUtils.getAsInt(e.get("stats").getAsJsonObject(), "baseAttack");
                ev.defense = JsonUtils.getAsInt(e.get("stats").getAsJsonObject(), "baseDefense");
                ev.form = TransformerForm.toForm("TEMP_EVOLUTION", e, "tempEvoId");
                ev.tags.add("MEGA");
                Logger.debug("pkmn evo temp", ev);
                listPkmn.add(ev);
            });
        }
        return listPkmn;
    }

    public static List<PkmnI18N> toPkmnI18N(JsonObject bloc) {
        var p = toPkmnBase(bloc);
        if (p == null || TransformerForm.isFormExclu(p.form)) {
            return new ArrayList<>();
        }
        return Transformer.listLang.stream().map(l -> {
            var pkmnI18N = new PkmnI18N();
            pkmnI18N.id = p.id;
            pkmnI18N.lang = l;
            pkmnI18N.form = p.form;
            pkmnI18N.name = p.idStr + " " + p.form;
            return pkmnI18N;
        }).collect(Collectors.toList());
    }

}
