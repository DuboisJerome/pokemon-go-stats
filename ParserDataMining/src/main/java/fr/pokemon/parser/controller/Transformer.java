package fr.pokemon.parser.controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.pokemon.parser.model.*;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Transformer {

    private static final List<String> listLang = Arrays.asList("fr_FR", "en_US");
    private static final String NORMAL_TEMP = "NORMAL_TEMP";

    public static <T> T tryTransform(JsonObject bloc, Function<JsonObject, T> fcntTransform) throws RuntimeException {
        try {
            return fcntTransform.apply(bloc);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de transformer " + bloc.toString(), e);
        }
    }

    public static PkmnDesc toPkmnBase(JsonObject bloc) {
        // exclu des spécificité pokemon home
        if (bloc.has("pokemonHomeFormReversions")) {
            return null;
        }
        var templateId = bloc.get("templateId").getAsString();
        var data = bloc.get("pokemonSettings").getAsJsonObject();
        var pokedex_num = NumberUtils.toInt(templateId.substring(1, 1 + 4));
        PkmnDesc pkmn = new PkmnDesc();
        pkmn.id = pokedex_num;
        pkmn.idStr = data.get("pokemonId").getAsString();
        pkmn.form = toForm(pkmn.idStr, data.get("form"));
        return pkmn;
    }

    public static List<PkmnDesc> toPkmn(JsonObject bloc) {
        var pkmn = toPkmnBase(bloc);
        List<PkmnDesc> listPkmn = new ArrayList<>();
        if (pkmn == null || isFormExclu(pkmn.form)) {
            return listPkmn;
        }
        var data = bloc.get("pokemonSettings").getAsJsonObject();
        pkmn.type1 = toType(data.get("type"));
        pkmn.type2 = toType(data.get("type2"));
        //pkmn.kms_per_egg=data.;
        pkmn.kmsPerCandy = data.get("kmBuddyDistance").getAsDouble();
        var stats = data.get("stats").getAsJsonObject();
        pkmn.stamina = getAsInt(stats.get("baseStamina"));
        pkmn.attack = getAsInt(stats.get("baseAttack"));
        pkmn.defense = getAsInt(stats.get("baseDefense"));
        listPkmn.add(pkmn);
        if (data.has("tempEvoOverrides")) {
            data.get("tempEvoOverrides").getAsJsonArray().forEach(e2 -> {
                JsonObject e = (JsonObject) e2;
                PkmnDesc ev = new PkmnDesc(pkmn);
                ev.type1 = toType(e.get("typeOverride1"));
                ev.type2 = toType(e.get("typeOverride2"));
                ev.stamina = getAsInt(e.get("stats").getAsJsonObject().get("baseStamina"));
                ev.attack = getAsInt(e.get("stats").getAsJsonObject().get("baseAttack"));
                ev.defense = getAsInt(e.get("stats").getAsJsonObject().get("baseDefense"));
                ev.form = toForm("TEMP_EVOLUTION", e.get("tempEvoId").getAsString());
                listPkmn.add(ev);
            });
        }
        return listPkmn;
    }

    private static Move toMovePveBase(JsonObject bloc) {
        var templateId = bloc.get("templateId").getAsString();
        var data = bloc.get("moveSettings").getAsJsonObject();
        var move = new Move();
        move.id = NumberUtils.toInt(templateId.substring(1, 1 + 4));
        move.idStr = data.get("movementId").getAsString();
        move.type = toType(data.get("pokemonType").getAsString());
        return move;
    }

    public static Move toMovePve(JsonObject bloc) {
        var data = bloc.get("moveSettings").getAsJsonObject();
        var move = toMovePveBase(bloc);
        move.power = getAsInt(data.get("power"));
        move.staminaLossScalar = getAsDouble(data.get("staminaLossScalar"));
        move.duration = getAsInt(data.get("durationMs"));
        move.energyDelta = getAsInt(data.get("energyDelta"));
        move.criticalChance = getAsDouble(data.get("criticalChance"));
        return move;
    }

    public static Move toMovePvp(JsonObject bloc) {
        var templateId = bloc.get("templateId").getAsString();
        var data = bloc.get("combatMove").getAsJsonObject();
        var move = new Move();
        move.id = NumberUtils.toInt(templateId.substring(8, 8 + 4));
        move.idStr = data.get("uniqueId").getAsString();
        move.type = toType(data.get("type").getAsString());
        move.powerPvp = getAsInt(data.get("power"));
        move.energyPvp = getAsInt(data.get("energyDelta"));
        move.durationPvp = getAsInt(data.get("durationTurns"));
        return move;
    }

    public static List<MoveI18N> toMoveI18N(JsonObject bloc) {
        var move = toMovePveBase(bloc);
        return listLang.stream().map(l -> {
            var movei18n = new MoveI18N();
            movei18n.id = move.id;
            movei18n.lang = l;
            movei18n.name = move.idStr;
            return movei18n;
        }).collect(Collectors.toList());
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
                return pkmnMove;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public static List<PkmnMove> toPkmnMove(JsonObject bloc) {
        var p = toPkmnBase(bloc);
        var list = new ArrayList<PkmnMove>();
        if (p == null || isFormExclu(p.form)) {
            return new ArrayList<>();
        }
        var data = bloc.get("pokemonSettings").getAsJsonObject();
        list.addAll(toPkmnMove(p, data.get("quickMoves"), "QUICK", false));
        list.addAll(toPkmnMove(p, data.get("cinematicMoves"), "CHARGE", false));
        list.addAll(toPkmnMove(p, data.get("eliteQuickMove"), "QUICK", true));
        list.addAll(toPkmnMove(p, data.get("eliteCinematicMove"), "CHARGE", true));
        return list;
    }

    public static List<Evolution> toEvol(JsonObject bloc) {
        var p = toPkmnBase(bloc);
        var listEvol = new ArrayList<Evolution>();
        if (p == null || isFormExclu(p.form)) {
            return listEvol;
        }
        var data = bloc.get("pokemonSettings").getAsJsonObject();
        if (data.has("evolutionBranch")) {
            listEvol.addAll(StreamSupport.stream(data.get("evolutionBranch").getAsJsonArray().spliterator(), false)
                    .map(je -> (JsonObject) je)
                    // exclu les mega
                    .filter(evBranche -> evBranche.has("evolution"))
                    .map(evBranche -> toEvol(evBranche, p))
                    .filter(ev -> ev.evolutionIdStr != null && !ev.evolutionIdStr.isEmpty() && !isFormExclu(ev.evolutionForm)).toList());
        }
        if (data.has("tempEvoOverrides")) {
            data.get("tempEvoOverrides").getAsJsonArray().forEach(e2 -> {
                JsonObject e = (JsonObject) e2;
                var ev = new Evolution();
                ev.basePkmnId = p.id;
                ev.basePkmnIdStr = p.idStr;
                ev.basePkmnForm = p.form;
                ev.evolutionId = p.id;
                ev.evolutionIdStr = p.idStr;
                ev.evolutionForm = toForm("TEMP_EVOLUTION", e.get("tempEvoId").getAsString());
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
        ev.evolutionForm = toForm(ev.evolutionIdStr, evBranche.get("form"));
        ev.candyToEvolve = getAsInt(evBranche.get("candyCost"));
//            ev.object_to_evolve=evBranche.evolutionItemRequirement;
//            ev.gender_requirement=evBranche.genderRequirement;
//            ev.lure_item_requirement=evBranche.lureItemRequirement;
        return ev;
    }

    public static List<PkmnI18N> toPkmnI18N(JsonObject bloc) {
        var p = toPkmnBase(bloc);
        if (p == null || isFormExclu(p.form)) {
            return new ArrayList<>();
        }
        return listLang.stream().map(l -> {
            var pkmnI18N = new PkmnI18N();
            pkmnI18N.id = p.id;
            pkmnI18N.lang = l;
            pkmnI18N.form = p.form;
            pkmnI18N.name = p.idStr + " " + p.form;
            return pkmnI18N;
        }).collect(Collectors.toList());
    }

    public static Type toType(JsonElement elem) {
        if (elem == null) return null;
        return Type.valueOf(elem.getAsString().substring("POKEMON_TYPE_".length()));
    }

    public static Type toType(String type) {
        if (type == null || type.isEmpty()) return null;
        return Type.valueOf(type.substring("POKEMON_TYPE_".length()));
    }

    public static String toForm(String pokemonId, JsonElement elem) {
        if (elem == null) {
            return NORMAL_TEMP;
        }
        return toForm(pokemonId, elem.getAsString());
    }

    public static String toForm(String pokemonId, String form) {
        if (form == null || form.isEmpty()) {
            return NORMAL_TEMP;
        }
        String res;
        var idx = form.indexOf(pokemonId);
        if (idx < 0) {
            idx = form.indexOf("_");
            if (idx < 0) {
                error("Forme inconnue _ *0 ", form);
                res = form;
            } else {
                res = form.substring(idx + 1);
                if (res.contains("_")) {
                    error("Forme inconnue _ *2 ", form);
                    res = form;
                }
            }
            log("Forme ne contient pas l'id : ", form, pokemonId, ". Forme déterminé : ", res);
        } else {
            res = form.substring(idx + pokemonId.length() + 1);
        }
        return res;
    }


    public static boolean isFormExclu(String form) {
        return form == null || form.isEmpty() || "SHADOW".equals(form) || "PURIFIED".equals(form) || form.contains("201") || form.contains("202");
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
        log("===== updateListPkmnMove");
        // remove pkmn-move without pkmn
        var result = listPkmnMove.stream().filter(pm -> listPkmn.stream().anyMatch(p -> p.id == pm.pokedexNum && p.form.equals(pm.form))).toList();
        // find associate move
        result.forEach(pm -> {
            // Find move by id str
            var move = find(listMove, m -> m.idStr.equals(pm.moveIdStr));
            // update pkmn-move
            pm.moveId = move != null ? move.id : -1;
        });
        // remove pkmn-move without move id
        return result.stream().filter(pm -> pm.moveId >= 0).collect(Collectors.toList());
    }

    /**
     * Update la liste des Moves pour savoir si c'est rapide ou chargé et retire les moves qui n'ont pas de type
     *
     * @param listMove
     * @param listPkmnMove
     * @return
     */
    public static List<Move> updateListMove(List<Move> listMove, List<PkmnMove> listPkmnMove) {
        log("===== updateListMove");
        listMove.forEach(m -> {
            // find any pkmn-move with this id
            var found = find(listPkmnMove, pm -> pm.moveId == m.id);
            m.moveType = found != null ? Move.MoveType.valueOfIgnoreCase(found.moveType) : null;
        });
        return listMove.stream().filter(m -> m.moveType != null).collect(Collectors.toList());
    }

    /**
     * Update la liste des évolutions en retirant celle dont la base ou l'évolution n'existe pas
     * Met également à jour l'id de l'évolution
     *
     * @param listEvol
     * @param listPkmn
     * @return
     */
    public static List<Evolution> updateListEvol(List<Evolution> listEvol, List<PkmnDesc> listPkmn) {
        log("===== updateListEvol");
        // remove evol without pkmn
        var result = listEvol.stream()
                .filter(e -> listPkmn.stream().anyMatch(p -> p.id == e.basePkmnId && p.form.equals(e.basePkmnForm))).toList();

        result.forEach(ev -> {
            var pkmnEv = find(listPkmn, p -> p.idStr.equals(ev.evolutionIdStr) && p.form.equals(ev.evolutionForm));
            ev.evolutionId = pkmnEv != null ? pkmnEv.id : -1;
        });
        return result.stream().filter(ev -> ev.evolutionId > 0).collect(Collectors.toList());
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
                .flatMap(ev ->
                        // Liste les moves du pkmn base de l'évolution et les copies sur l'évolutions temporaires
                        listPkmnMove.stream()
                                .filter(pm -> pm.getUniquePkmnId().equals(ev.getUniqueIdBase()))
                                .map(pmBase -> {
                                    var pkmnMove = new PkmnMove();
                                    pkmnMove.pkmnIdStr = ev.getEvolutionIdStr();
                                    pkmnMove.pokedexNum = ev.getEvolutionId();
                                    pkmnMove.form = ev.getEvolutionForm();
                                    pkmnMove.moveId = pmBase.getMoveId();
                                    pkmnMove.moveIdStr = pmBase.getMoveIdStr();
                                    pkmnMove.moveType = pmBase.getMoveType();
                                    pkmnMove.isElite = pmBase.isElite();
                                    return pkmnMove;
                                })
                )
                .toList();
    }


    public static <T> Predicate<T> filterCleanForms(List<T> list, Function<T, String> funcId, Function<T, String> formParam) {
        return e -> {
            var form = formParam.apply(e);
            // Formes exclues obligatoirement
            var isExclu = isFormExclu(form);
            if (isExclu) {
                log(funcId.apply(e), " exclu forcé");
                return false;
            }
            // Formes autorisées obligatoirement
            var isAutorise = !NORMAL_TEMP.equals(form);
            if (isAutorise) {
                //log(funcId.apply(e), " inclu forcé");
                return true;
            }
            // Formes gérées au cas par cas
            var listForms = list.stream().filter(e2 -> e2 != e
                            && funcId.apply(e).equals(funcId.apply(e2))
                            && !NORMAL_TEMP.equals(formParam.apply(e2))
                            && !formParam.apply(e2).startsWith("MEGA"))
                    .map(formParam).distinct().toList();
            boolean isOk = listForms.size() <= 0;
            if (!isOk) {
                //console.log(funcId(e),"exclu car", listForms);
            } else {
                //console.log(funcId(e),"inclu");
            }
            return isOk;
        };
    }

    public static <T> List<T> cleanForms(List<T> list, Function<T, String> funcId, Function<T, String> formParam, BiConsumer<T, String> setterForm) {
        log("cleanForms ...........................................");
        // Filtre les formes qui ne sont pas de type NORMAL_TEMP OU qui le sont mais qu'il n'y a aucun autre element de la liste pour le meme id
        return list.stream().filter(filterCleanForms(list, funcId, formParam)).map(e -> {
            // Les NORMAL_TEMP n'ont pas d'autres formes
            var form = formParam.apply(e);
            if (NORMAL_TEMP.equals(form)) {
                setterForm.accept(e, "NORMAL");
            }
            return e;
        }).collect(Collectors.toList());
    }

    private static int getAsInt(JsonElement elem) {
        return elem == null ? -1 : elem.getAsInt();
    }

    private static double getAsDouble(JsonElement elem) {
        return elem == null ? -1D : elem.getAsDouble();
    }

    public static <T> T find(List<T> l, Predicate<T> p) {
        return l.stream().filter(p).findAny().orElse(null);
    }

    private static void error(String... s) {
        System.err.println(String.join("", s));
    }

    private static void log(String... s) {
        System.out.println(String.join("", s));
    }
}


