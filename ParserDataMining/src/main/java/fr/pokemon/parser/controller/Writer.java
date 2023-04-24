package fr.pokemon.parser.controller;

import fr.pokemon.parser.model.*;
import fr.pokemon.parser.model.sql.SqlBuilder;
import fr.pokemon.parser.model.sql.SqlBuilderException;
import fr.pokemon.parser.model.sql.Table;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Writer {

    private static final String ouputDir = "src/main/resources/output/";

    private static String toUpper(String str) {
        return str != null ? str.toUpperCase() : null;
    }

    private static String toUpper(Enum<?> en) {
        if (en == null) {
            return null;
        }
        return toUpper(en.name());
    }

    public void writeReqMovePve(List<Move> list) {
        var table = new Table<Move>("move");
        table.addColKey("_id", Move::getId);
        table.addColString("type", Move::getType, Writer::toUpper);
        table.addColString("move_type", Move::getMoveType, Writer::toUpper);
        table.addCol("power", Move::getPower);
        table.addCol("stamina_loss_scalar", Move::getStaminaLossScalar);
        table.addCol("duration", Move::getDuration);
        table.addCol("energy_delta", Move::getEnergyDelta);
        table.addCol("critical_chance", Move::getCriticalChance);
        writeReq(table, list, SqlBuilder.MODE_BOTH, null);
    }

    public void writeReqMovePvp(List<Move> list) {
        var table = new Table<Move>("move");
        table.addColKey("_id", Move::getId);
        table.addColString("type", Move::getType, Writer::toUpper);
        table.addColString("move_type", Move::getMoveType, Writer::toUpper);
        table.addCol("power_pvp", Move::getPowerPvp);
        table.addCol("energy_pvp", Move::getEnergyPvp);
        table.addCol("duration_pvp", Move::getDurationPvp);
        writeReq(table, list, SqlBuilder.MODE_UPDATE, "move_pvp");
    }

    public void writeReqMoveI18N(List<MoveI18N> list) {
        var table = new Table<MoveI18N>("move_i18n");
        table.addColKey("_id", MoveI18N::getId);
        table.addColKeyString("lang", MoveI18N::getLang);
        table.addColString("name", MoveI18N::getName);
        writeReq(table, list, SqlBuilder.MODE_INSERT, null);
    }

    public void writeReqPkmn(List<PkmnDesc> list) {

        var table = new Table<PkmnDesc>("pokemon");
        table.addColKey("_id", PkmnDesc::getId, null);
        table.addColKeyString("form", PkmnDesc::getForm, Writer::toUpper);
        table.addColString("type1", PkmnDesc::getType1, Writer::toUpper);
        table.addColString("type2", PkmnDesc::getType2, Writer::toUpper);
        table.addCol("kms_per_candy", PkmnDesc::getKmsPerCandy);
        //table.addCol("kms_per_egg", PkmnDesc::getKmsPerEgg);
        table.addCol("stamina", PkmnDesc::getStamina);
        table.addCol("attack", PkmnDesc::getAttack);
        table.addCol("defense", PkmnDesc::getDefense);
        table.addColString("tags", p -> p.getTags().isEmpty() ? null : p.getTags(), tags -> tags != null ? String.join(",", tags) : null);
        writeReq(table, list, SqlBuilder.MODE_BOTH, null);
    }

    public void writeReqPkmnMove(List<PkmnMove> list, String fileName) {
        var table = new Table<PkmnMove>("pokemon_move");
        table.addColKey("move_id", PkmnMove::getMoveId);
        table.addColKey("pokemon_id", PkmnMove::getPokedexNum);
        table.addColKeyString("form", PkmnMove::getForm, Writer::toUpper);
        table.addCol("is_elite", pm -> pm.isElite ? 1 : 0);
        writeReq(table, list, SqlBuilder.MODE_INSERT, fileName);
    }

    public void writeReqEvol(List<Evolution> list) {
        var table = new Table<Evolution>("evolution");
        table.addColKey("evolution_id", Evolution::getEvolutionId);
        table.addColKeyString("evolution_form", Evolution::getEvolutionForm, Writer::toUpper);
        table.addCol("base_pkmn_id", Evolution::getBasePkmnId);
        table.addColString("base_pkmn_form", Evolution::getBasePkmnForm, Writer::toUpper);
        table.addColString("object_to_evolve", t -> null); // TODO
        table.addCol("candy_to_evolve", Evolution::getCandyToEvolve); // TODO
        table.addCol("is_temporaire", e -> e.isTemporaire ? 1 : 0); // TODO
        writeReq(table, list, SqlBuilder.MODE_BOTH, null);
    }

    public void writeReqPkmnI18N(List<PkmnI18N> list) {
        var table = new Table<PkmnI18N>("pokemon_i18n");
        table.addColKey("_id", PkmnI18N::getId);
        table.addColKeyString("form", PkmnI18N::getForm, Writer::toUpper);
        table.addColKeyString("lang", PkmnI18N::getLang);
        table.addColString("name", PkmnI18N::getName);
        table.addColString("family", t -> null);// TODO
        table.addColString("description", t -> null);// TODO
        writeReq(table, list, SqlBuilder.MODE_INSERT, null);
    }

    public <T> void writeReq(Table<T> table, List<T> list, int mode, String filename) {
        var builder = new SqlBuilder<>(table, mode);
        var lstQuery = new ArrayList<String>();
        for (T t : list) {
            try {
                lstQuery.addAll(builder.buildReq(t));
            } catch (SqlBuilderException e) {
                lstQuery.add("-- " + e.getMessage());
            }
        }
        if (filename == null) {
            filename = table.getName();
        }

        File output = new File(ouputDir);
        String repNameCurrent = getNameDirHistoCurrent();
        Pattern patternDirHisto = Pattern.compile("\\d{4}_\\d{2}_\\d{2}");
        File[] lstDirHisto = output.listFiles((dir) -> dir.isDirectory() && patternDirHisto.matcher(dir.getName()).matches() && !repNameCurrent.equals(dir.getName()));
        File latestDirHisto = null;
        if (lstDirHisto != null) {
            latestDirHisto = Stream.of(lstDirHisto).max(Comparator.comparing(File::getName)).orElse(null);
        }

        try {
            // TODO faire une gestion de deletes mais peut etre plus en amont dans la génération du sql
            List<String> lstQueryDiff = new ArrayList<>(lstQuery);

            if (latestDirHisto != null) {
                System.out.println("Dernière version d'export sql trouvée : " + latestDirHisto.getName());
                // Lit le dernier fichier Sql qui contient toutes les requêtes existantes
                // puis détermine les lignes qui ont été ajoutées depuis et pou
                File fDirLastAll = new File(latestDirHisto, "all");
                if (fDirLastAll.exists()) {
                    File fSqlLastAll = new File(fDirLastAll, filename + ".sql");
                    if (fSqlLastAll.exists()) {
                        List<String> lstExistingQuery = Files.readAllLines(fSqlLastAll.toPath());
                        lstQueryDiff.removeAll(lstExistingQuery);
                    } else {
                        System.out.println("Aucun fichier '" + fSqlLastAll.getName() + "' n'a été trouvée dans le répertoire 'all' de la dernière version d'export sql");
                    }
                } else {
                    System.out.println("Aucun répertoire 'all' n'a été trouvée dans la dernière version d'export sql");
                }
            } else {
                System.out.println("Aucune dernière version d'export sql n'a été trouvée");
            }
            // Créer le repertoire de la version courante s'il n'existe pas
            File repCurrent = new File(ouputDir, repNameCurrent);
            if (!repCurrent.exists() && !repCurrent.mkdir()) {
                throw new Exception("Impossible de créer le répertoire output pour la version courante " + repNameCurrent);
            }
            // Créer le sous répertoire "diff" de la version courante
            File repDiffCurrent = new File(repCurrent, "diff");
            if (!repDiffCurrent.exists() && !repDiffCurrent.mkdir()) {
                throw new Exception("Impossible de créer le répertoire 'diff' pour la version courante");
            }

            // Génère le fichier de diff
            File fSqlDiff = new File(repDiffCurrent, filename + ".sql");
            Files.write(fSqlDiff.toPath(), lstQueryDiff, StandardCharsets.UTF_8);

            // Créer le sous répertoire "all" de la version courante
            File repAllCurrent = new File(repCurrent, "all");
            if (!repAllCurrent.exists() && !repAllCurrent.mkdir()) {
                throw new Exception("Impossible de créer le répertoire 'all' pour la version courante");
            }
            // Génère le fichier all
            File fSqlAll = new File(repAllCurrent, filename + ".sql");
            Files.write(fSqlAll.toPath(), lstQuery, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getNameDirHistoCurrent() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
    }
}
