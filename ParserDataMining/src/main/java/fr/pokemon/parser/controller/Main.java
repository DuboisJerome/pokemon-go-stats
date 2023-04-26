package fr.pokemon.parser.controller;

import com.google.gson.JsonObject;
import fr.pokemon.parser.controller.transformer.*;
import fr.pokemon.parser.model.Evolution;
import fr.pokemon.parser.model.PkmnDesc;
import fr.pokemon.parser.model.PkmnI18N;
import fr.pokemon.parser.model.PkmnMove;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Main {

    private static final Splitter splitter = new Splitter();
    private static final String REMOTE_FILE_URL = "https://raw.githubusercontent.com/PokeMiners/game_masters/master/latest/latest.json";
    private static final String INPUT_DIR = "src/main/resources/input/";
    private static final String INPUT_FILENAME = "latest.json";
    private static final String OUTPUT_DIR = "src/main/resources/output/";

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

    public static void main(String[] args) throws Exception {

        File inputFileJson = new File(INPUT_DIR, INPUT_FILENAME);
        long lastModif = Files.getLastModifiedTime(inputFileJson.toPath()).toMillis();
        long elapse = System.currentTimeMillis() - lastModif;
        // Le fichier a plus de 24h
        boolean isReadRemote = elapse > 1000 * 60 * 60 * 24;
        if (isReadRemote) {
            try {
                InputStream in = new URL(REMOTE_FILE_URL).openStream();
                Files.copy(in, inputFileJson.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        splitter.split(inputFileJson);
        // GET LIST from GAME_MASTER
        var listPkmn = getListElemFromList("PKMN", TransformerPkmnDesc::toPkmn);
        var listPkmnI18N = getListElemFromList("PKMN", TransformerPkmnDesc::toPkmnI18N);
        var listPkmnMove = getListElemFromList("PKMN", TransformerPkmnMove::toPkmnMove);
        var listEvol = getListElemFromList("PKMN", TransformerEvol::toEvol);
        var listMovePve = getListElem("MOVE_PVE", TransformerMove::toMovePve);
        var listMovePvp = getListElem("MOVE_PVP", TransformerMove::toMovePvp);
        TransformerMove.updatePveFromPvp(listMovePve, listMovePvp);
        var listMoveI18N = getListElemFromList("MOVE_PVE", TransformerMove::toMoveI18N);

        // clean forms
        var listPkmnClean = TransformerForm.cleanForms(listPkmn, p -> p.id + "", PkmnDesc::getId, PkmnDesc::getForm, PkmnDesc::setForm);
        var listPkmnI18NClean = TransformerForm.cleanForms(listPkmnI18N, p -> p.id + "__" + p.lang, PkmnI18N::getId, PkmnI18N::getForm, PkmnI18N::setForm);
        var listPkmnMoveClean = TransformerForm.cleanForms(listPkmnMove, pm -> pm.pokedexNum + "__" + pm.moveIdStr, PkmnMove::getPokedexNum, PkmnMove::getForm, PkmnMove::setForm);

        var listEvolClean1 = TransformerForm.cleanForms(listEvol, e -> e.basePkmnIdStr + "__" + e.evolutionIdStr + "_" + e.evolutionForm, Evolution::getBasePkmnId, Evolution::getBasePkmnForm, Evolution::setBasePkmnForm);
        var listEvolClean2 = TransformerForm.cleanForms(listEvolClean1, e -> e.basePkmnIdStr + "_" + e.basePkmnForm + "__" + e.evolutionIdStr, Evolution::getEvolutionId, Evolution::getEvolutionForm, Evolution::setEvolutionForm);

        // update list from others
        var listPkmnMoveCleanUpdate = TransformerPkmnMove.updateListPkmnMove(listPkmnMoveClean, listPkmnClean, listMovePve);
        var listEvolCleanUpdate = TransformerEvol.updateListEvol(listEvolClean2, listPkmnClean);
        var listMovePveUpdate = TransformerMove.updateListMove(listMovePve, listPkmnMoveCleanUpdate);
        var listPkmnMoveEvolTemp = TransformerPkmnMove.buildListPkmnMoveMega(listEvolClean2, listPkmnMoveClean);

        // Prépare le répertoire output
        File outputDir = new File(OUTPUT_DIR);
        RepertoireOutput currentVersion = new RepertoireOutput(outputDir);
        String nomSubDirLatestVersion = RepertoireOutput.getNomSubDirLatestVersion(outputDir);
        RepertoireOutput subDirLatestVersion = null;
        if (nomSubDirLatestVersion != null) {
            System.out.println("Dernière version d'export sql trouvée : " + nomSubDirLatestVersion);
            subDirLatestVersion = new RepertoireOutput(outputDir, nomSubDirLatestVersion);
        } else {
            System.out.println("Aucune dernière version d'export sql n'a été trouvée");
        }

        // WRITER
        Writer reqWriter = new Writer(currentVersion, subDirLatestVersion);
        reqWriter.writeReqPkmn(listPkmnClean, "pokemon");
        reqWriter.writeReqPkmnI18N(listPkmnI18NClean, "pokemon_i18n");
        reqWriter.writeReqPkmnMove(listPkmnMoveCleanUpdate, "asso_pokemon_move");
        reqWriter.writeReqPkmnMove(listPkmnMoveEvolTemp, "asso_pokemon_move_mega");
        reqWriter.writeReqEvol(listEvolCleanUpdate, "evolution");
        reqWriter.writeReqMovePve(listMovePveUpdate, "move_pve");
        reqWriter.writeReqMovePvp(listMovePveUpdate, "move_pvp");
        reqWriter.writeReqMoveI18N(listMoveI18N, " move_i18n");
        Logger.info("FIN");
    }

}