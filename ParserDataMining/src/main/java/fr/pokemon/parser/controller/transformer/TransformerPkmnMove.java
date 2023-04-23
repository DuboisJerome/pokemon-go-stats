package fr.pokemon.parser.controller.transformer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.pokemon.parser.controller.Logger;
import fr.pokemon.parser.model.Evolution;
import fr.pokemon.parser.model.Move;
import fr.pokemon.parser.model.PkmnDesc;
import fr.pokemon.parser.model.PkmnMove;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TransformerPkmnMove {

    public static List<PkmnMove> toPkmnMove(JsonObject bloc) {
        var p = TransformerPkmnDesc.toPkmnBase(bloc);
        var list = new ArrayList<PkmnMove>();
        if (p == null || TransformerForm.isFormExclu(p.form)) {
            Logger.debug("pkmn move exclu", p != null ? p.toStringLight() : null);
            return new ArrayList<>();
        }
        var data = bloc.get("pokemonSettings").getAsJsonObject();
        list.addAll(toPkmnMove(p, data.get("quickMoves"), "QUICK", false));
        list.addAll(toPkmnMove(p, data.get("cinematicMoves"), "CHARGE", false));
        list.addAll(toPkmnMove(p, data.get("eliteQuickMove"), "QUICK", true));
        list.addAll(toPkmnMove(p, data.get("eliteCinematicMove"), "CHARGE", true));
        if (list.isEmpty()) {
            Logger.warn("No PkmnMove", p.toStringLight(), bloc);
        }
        return list;
    }

    private static List<PkmnMove> toPkmnMove(PkmnDesc p, JsonElement elem, String moveType, boolean isElite) {
        if (elem != null && elem.isJsonArray()) {
            JsonArray listMoveName = elem.getAsJsonArray();
            return StreamSupport.stream(listMoveName.spliterator(), false).map(m -> {
                var pkmnMove = new PkmnMove();
                pkmnMove.pkmnIdStr = p.idStr;
                pkmnMove.moveIdStr = m.getAsString();
                pkmnMove.pokedexNum = p.id;
                pkmnMove.form = p.form;
                pkmnMove.moveId = -1;
                pkmnMove.moveType = moveType;
                pkmnMove.isElite = isElite;
                Logger.debug("pkmnMove", pkmnMove);
                return pkmnMove;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }


    /**
     * Update la liste des moves par pkmn pour mettre l'id du move sur l'asso Pkmn / Move
     * tout en retirant les asso qui ne correspondent à aucun pkmn ou aucun move
     *
     * @param listPkmnMove
     * @param listPkmn
     * @param listMove
     * @return
     */
    public static List<PkmnMove> updateListPkmnMove(List<PkmnMove> listPkmnMove, List<PkmnDesc> listPkmn, List<Move> listMove) {
        Logger.info("===== updateListPkmnMove");
        // remove pkmn-move without pkmn
        var result = listPkmnMove.stream().filter(pm -> listPkmn.stream().anyMatch(p -> p.id == pm.pokedexNum && p.form.equals(pm.form))).toList();
        // find associate move
        result.forEach(pm -> {
            // Find move by id str
            var move = Transformer.find(listMove, m -> m.idStr.equals(pm.moveIdStr));
            // update pkmn-move
            if (move != null) {
                pm.moveId = move.id;
                Logger.debug("Update PkmnMove id", pm);
            } else {
                Logger.warn("Move not found for PkmnMove", pm);
                pm.moveId = -1;
            }
        });
        // remove pkmn-move without move id
        return result.stream().filter(pm -> pm.moveId >= 0).collect(Collectors.toList());
    }

    /**
     * Ajoute à la liste des asso Pkmn/Move les asso des formes temporaires en copiant les asso des bases
     *
     * @param listEvol
     * @param listPkmnMove
     * @return
     */
    public static List<PkmnMove> buildListPkmnMoveMega(List<Evolution> listEvol, List<PkmnMove> listPkmnMove) {
        return listEvol.stream()
                .filter(Evolution::isTemporaire)
                .flatMap(ev -> {
                            Logger.debug("buildListPkmnMoveMega", ev);
                            // Liste les moves du pkmn base de l'évolution et les copies sur l'évolutions temporaires
                            return listPkmnMove.stream()
                                    .filter(pm -> pm.getUniquePkmnId().equals(ev.getUniqueIdBase()))
                                    .map(pmBase -> {
                                        Logger.debug("->", pmBase);
                                        var pkmnMove = new PkmnMove();
                                        pkmnMove.pkmnIdStr = ev.getEvolutionIdStr();
                                        pkmnMove.pokedexNum = ev.getEvolutionId();
                                        pkmnMove.form = ev.getEvolutionForm();
                                        pkmnMove.moveId = pmBase.getMoveId();
                                        pkmnMove.moveIdStr = pmBase.getMoveIdStr();
                                        pkmnMove.moveType = pmBase.getMoveType();
                                        pkmnMove.isElite = pmBase.isElite();
                                        return pkmnMove;
                                    });
                        }
                )
                .toList();
    }


}
