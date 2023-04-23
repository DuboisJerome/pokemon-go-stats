package fr.pokemon.parser.controller;

import com.google.gson.JsonObject;
import fr.pokemon.parser.controller.transformer.*;
import fr.pokemon.parser.model.Evolution;
import fr.pokemon.parser.model.PkmnDesc;
import fr.pokemon.parser.model.PkmnI18N;
import fr.pokemon.parser.model.PkmnMove;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    private static final Splitter splitter = new Splitter();

    private static <T> List<T> getListElem(String nom, Function<JsonObject, T> transform) {
        Logger.info("============= getListElem(", nom, ")");
        return splitter.getListGrp()
                .stream().filter(grp -> grp.name.equals(nom))
                .flatMap(grp -> grp.lst.stream())
                .map(t -> Transformer.tryTransform(t, transform))
                .collect(Collectors.toList());
    }

    private static <T> List<T> getListElemFromList(String nom, Function<JsonObject, List<T>> transform) {
        Logger.info("============= getListElem(", nom, ")");
        return splitter.getListGrp()
                .stream().filter(grp -> grp.name.equals(nom))
                .flatMap(grp -> grp.lst.stream())
                .map(t -> Transformer.tryTransform(t, transform))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        splitter.split();
        // GET LIST from GAME_MASTER
        var listPkmn = getListElemFromList("PKMN", TransformerPkmnDesc::toPkmn);
        var listPkmnI18N = getListElemFromList("PKMN", TransformerPkmnDesc::toPkmnI18N);
        var listPkmnMove = getListElemFromList("PKMN", TransformerPkmnMove::toPkmnMove);
        var listEvol = getListElemFromList("PKMN", TransformerEvol::toEvol);
        var listMovePve = getListElem("MOVE_PVE", TransformerMove::toMovePve);
        //    var listMovePvp = getListElem("MOVE_PVP", Transformer::toMovePvp);
        var listMoveI18N = getListElemFromList("MOVE_PVE", TransformerMove::toMoveI18N);

        // clean forms
        var listPkmnClean = TransformerForm.cleanForms(listPkmn, p -> p.id + "", PkmnDesc::getForm, PkmnDesc::setForm);
        var listPkmnI18NClean = TransformerForm.cleanForms(listPkmnI18N, p -> p.id + "__" + p.lang, PkmnI18N::getForm, PkmnI18N::setForm);
        var listPkmnMoveClean = TransformerForm.cleanForms(listPkmnMove, pm -> pm.pokedexNum + "__" + pm.moveIdStr, PkmnMove::getForm, PkmnMove::setForm);

        var listEvolClean1 = TransformerForm.cleanForms(listEvol, e -> e.basePkmnIdStr + "__" + e.evolutionIdStr + "_" + e.evolutionForm, Evolution::getBasePkmnForm, Evolution::setBasePkmnForm);
        var listEvolClean2 = TransformerForm.cleanForms(listEvolClean1, e -> e.basePkmnIdStr + "_" + e.basePkmnForm + "__" + e.evolutionIdStr, Evolution::getEvolutionForm, Evolution::setEvolutionForm);

        // update list from others
        var listPkmnMoveCleanUpdate = TransformerPkmnMove.updateListPkmnMove(listPkmnMoveClean, listPkmnClean, listMovePve);
        var listEvolCleanUpdate = TransformerEvol.updateListEvol(listEvolClean2, listPkmnClean);
        var listMovePveUpdate = TransformerMove.updateListMove(listMovePve, listPkmnMoveCleanUpdate);
        var listPkmnMoveEvolTemp = TransformerPkmnMove.buildListPkmnMoveMega(listEvolClean2, listPkmnMoveClean);
//        listMovePvp = Transformer.updateListMove(listMovePvp, listPkmnMoveCleanUpdate);

        // WRITER
        Writer reqWriter = new Writer();
        reqWriter.writeReqPkmn(listPkmnClean);
        reqWriter.writeReqPkmnI18N(listPkmnI18NClean);
        reqWriter.writeReqPkmnMove(listPkmnMoveCleanUpdate, "move_std");
        reqWriter.writeReqPkmnMove(listPkmnMoveEvolTemp, "move_evol_temp");
        reqWriter.writeReqEvol(listEvolCleanUpdate);
        reqWriter.writeReqMovePve(listMovePveUpdate);
//        reqWriter.writeReqMovePvp(listMovePvp);
        reqWriter.writeReqMoveI18N(listMoveI18N);
        Logger.info("FIN");
    }

}


//delete from pokedex_i18n where not exists (select 1 from pokedex b where pokedex_i18n.pokedex_num = b.pokedex_num and pokedex_i18n.form = b.form);
//delete from pokedex_move where not exists (select 1 from pokedex b where pokedex_move.pokedex_num = b.pokedex_num and pokedex_move.form = b.form);
//delete from evolution where not exists (select 1 from pokedex b where evolution.base_pkmn_id = b.pokedex_num and evolution.base_pkmn_form = b.form);
//delete from evolution where not exists (select 1 from pokedex b where evolution.evolution_id = b.pokedex_num and evolution.evolution_form = b.form);

