package com.pokemongostats.controller.utils;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Move.MoveType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zapagon on 31/03/2017.
 */
public final class MoveUtils {
    private MoveUtils() {
    }

    public static Map<MoveType, List<Move>> getMovesMap(final List<Move> moves) {
        if (moves == null || moves.isEmpty()) {
            return new HashMap<>();
        }

        final Map<MoveType, List<Move>> result = new HashMap<>();
        for (Move m : moves) {
            MoveType moveType = m.getMoveType();
            List<Move> list = result.get(moveType);
            if (list == null) {
                list = new ArrayList<>();
                result.put(moveType, list);
            }
            list.add(m);
        }
        return result;
    }

    public static List<Move> getMovesFromIds(final List<Move> moves, final List<Long> ids) {
        if (moves == null || moves.isEmpty() || ids == null || ids.isEmpty()) {
            return new ArrayList<>();
        }

        final List<Move> result = new ArrayList<>();
        for (Move m : moves) {
            if (ids.contains(m.getId())) {
                result.add(m);
            }
        }
        return result;
    }
}
