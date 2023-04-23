package fr.pokemon.parser.controller.transformer;

import com.google.gson.JsonObject;
import fr.pokemon.parser.controller.Logger;
import fr.pokemon.parser.model.Move;
import fr.pokemon.parser.model.MoveI18N;
import fr.pokemon.parser.model.PkmnMove;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.stream.Collectors;

public class TransformerMove {

    private static Move toMovePveBase(JsonObject bloc) {
        Logger.debug("toMovePveBase", bloc);
        var templateId = bloc.get("templateId").getAsString();
        var data = bloc.get("moveSettings").getAsJsonObject();
        var move = new Move();
        move.id = NumberUtils.toInt(templateId.substring(1, 1 + 4));
        move.idStr = data.get("movementId").getAsString();
        move.type = TransformerType.toType(data, "pokemonType");
        return move;
    }

    public static Move toMovePve(JsonObject bloc) {
        var data = bloc.get("moveSettings").getAsJsonObject();
        var move = toMovePveBase(bloc);
        move.power = JsonUtils.getAsInt(data, "power");
        move.staminaLossScalar = JsonUtils.getAsDouble(data, "staminaLossScalar");
        move.duration = JsonUtils.getAsInt(data, "durationMs");
        move.energyDelta = JsonUtils.getAsInt(data, "energyDelta");
        move.criticalChance = JsonUtils.getAsDouble(data, "criticalChance");
        return move;
    }

    public static Move toMovePvp(JsonObject bloc) {
        var templateId = bloc.get("templateId").getAsString();
        var data = bloc.get("combatMove").getAsJsonObject();
        var move = new Move();
        move.id = NumberUtils.toInt(templateId.substring(8, 8 + 4));
        move.idStr = data.get("uniqueId").getAsString();
        move.type = TransformerType.toType(data, "type");
        move.powerPvp = JsonUtils.getAsInt(data, "power");
        move.energyPvp = JsonUtils.getAsInt(data, "energyDelta");
        move.durationPvp = JsonUtils.getAsInt(data, "durationTurns");
        return move;
    }

    /**
     * Update la liste des Moves pour savoir si c'est rapide ou chargé et retire les moves qui n'ont pas de type
     *
     * @param listMove
     * @param listPkmnMove
     * @return
     */
    public static List<Move> updateListMove(List<Move> listMove, List<PkmnMove> listPkmnMove) {
        Logger.info("===== updateListMove");
        listMove.forEach(m -> {
            // find any pkmn-move with this id
            var found = Transformer.find(listPkmnMove, pm -> pm.moveId == m.id);
            m.moveType = found != null ? Move.MoveType.valueOfIgnoreCase(found.moveType) : null;
        });
        return listMove.stream().filter(m -> m.moveType != null).collect(Collectors.toList());
    }

    public static List<MoveI18N> toMoveI18N(JsonObject bloc) {
        var move = toMovePveBase(bloc);
        return Transformer.listLang.stream().map(l -> {
            var movei18n = new MoveI18N();
            movei18n.id = move.id;
            movei18n.lang = l;
            movei18n.name = move.idStr;
            return movei18n;
        }).collect(Collectors.toList());
    }


}
