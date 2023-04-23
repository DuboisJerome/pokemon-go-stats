package fr.pokemon.parser.controller.transformer;

import com.google.gson.JsonObject;
import fr.pokemon.parser.controller.Logger;
import fr.pokemon.parser.model.Evolution;
import fr.pokemon.parser.model.PkmnDesc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TransformerEvol {

    /**
     * Update la liste des évolutions en retirant celle dont la base ou l'évolution n'existe pas
     * Met également à jour l'id de l'évolution
     *
     * @param listEvol
     * @param listPkmn
     * @return
     */
    public static List<Evolution> updateListEvol(List<Evolution> listEvol, List<PkmnDesc> listPkmn) {
        Logger.info("===== updateListEvol");
        // remove evol without pkmn
        var result = listEvol.stream()
                .filter(e -> listPkmn.stream().anyMatch(p -> p.id == e.basePkmnId && p.form.equals(e.basePkmnForm))).toList();

        result.forEach(ev -> {
            var pkmnEv = Transformer.find(listPkmn, p -> p.idStr.equals(ev.evolutionIdStr) && p.form.equals(ev.evolutionForm));
            ev.evolutionId = pkmnEv != null ? pkmnEv.id : -1;
        });
        return result.stream().filter(ev -> ev.evolutionId > 0).collect(Collectors.toList());
    }

    public static List<Evolution> toEvol(JsonObject bloc) {
        var p = TransformerPkmnDesc.toPkmnBase(bloc);
        var listEvol = new ArrayList<Evolution>();
        if (p == null || TransformerForm.isFormExclu(p.form)) {
            return listEvol;
        }
        var data = bloc.get("pokemonSettings").getAsJsonObject();
        if (data.has("evolutionBranch")) {
            listEvol.addAll(StreamSupport.stream(data.get("evolutionBranch").getAsJsonArray().spliterator(), false)
                    .map(je -> (JsonObject) je)
                    // exclu les mega
                    .filter(evBranche -> evBranche.has("evolution"))
                    .map(evBranche -> toEvol(evBranche, p))
                    .filter(ev -> ev.evolutionIdStr != null && !ev.evolutionIdStr.isEmpty() && !TransformerForm.isFormExclu(ev.evolutionForm)).toList());
        }
        if (data.has("tempEvoOverrides")) {
            data.get("tempEvoOverrides").getAsJsonArray().forEach(e2 -> {
                JsonObject e = (JsonObject) e2;
                if (!e.has("tempEvoId")) {
                    // Il y a peu visiblement y avoir des objets dans l'array tempEvoOverrides
                    // qui ne sont pas des evolutions. On les exclues
                    return;
                }
                var ev = new Evolution();
                ev.basePkmnId = p.id;
                ev.basePkmnIdStr = p.idStr;
                ev.basePkmnForm = p.form;
                ev.evolutionId = p.id;
                ev.evolutionIdStr = p.idStr;
                ev.evolutionForm = TransformerForm.toForm("TEMP_EVOLUTION", e, "tempEvoId");
                ev.setTemporaire(true);
//                var branche = StreamSupport.stream(data.get("evolutionBranch").getAsJsonArray().spliterator(), false).map(je -> (JsonObject) je)
//                        .filter(b -> b.get("temporaryEvolution").getAsString().equals(e.get("tempEvoId").getAsString()))
//                        .findAny().orElse(null);
//                if (branche != null) {
//                ev.temporaryEvolutionEnergyCost = branche.temporaryEvolutionEnergyCost;
//                ev.temporaryEvolutionEnergyCostSubsequent = branche.temporaryEvolutionEnergyCostSubsequent;
//                }
                listEvol.add(ev);
            });
        }
        return listEvol;
    }

    private static Evolution toEvol(JsonObject evBranche, PkmnDesc p) {
        var ev = new Evolution();
        ev.basePkmnId = p.id;
        ev.basePkmnIdStr = p.idStr;
        ev.basePkmnForm = p.form;
        ev.evolutionId = -1;
        ev.evolutionIdStr = evBranche.get("evolution").getAsString();
        ev.evolutionForm = TransformerForm.toForm(ev.evolutionIdStr, evBranche, "form");
        ev.candyToEvolve = JsonUtils.getAsInt(evBranche, "candyCost");
//            ev.object_to_evolve=evBranche.evolutionItemRequirement;
//            ev.gender_requirement=evBranche.genderRequirement;
//            ev.lure_item_requirement=evBranche.lureItemRequirement;
        return ev;
    }

}
