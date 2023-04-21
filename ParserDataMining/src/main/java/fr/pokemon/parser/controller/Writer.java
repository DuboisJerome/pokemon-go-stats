package fr.pokemon.parser.controller;

import fr.pokemon.parser.model.*;
import fr.pokemon.parser.model.sql.SqlBuilder;
import fr.pokemon.parser.model.sql.SqlBuilderException;
import fr.pokemon.parser.model.sql.Table;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
            String query;
            try {
                query = builder.buildReq(t);
            } catch (SqlBuilderException e) {
                query = "-- " + e.getMessage();
            }
            lstQuery.add(query);
        }
        if (filename == null) {
            filename = table.getName();
        }
        File f = new File(ouputDir + filename + ".sql");
        try {
            Files.write(f.toPath(), lstQuery, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
