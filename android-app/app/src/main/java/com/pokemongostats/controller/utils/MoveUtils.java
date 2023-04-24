package com.pokemongostats.controller.utils;

import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.Move.MoveType;

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

	public static Map<MoveType,List<Move>> getMovesMap(List<Move> moves) {
		if (moves == null || moves.isEmpty()) {
			return new HashMap<>();
		}

		Map<MoveType,List<Move>> result = new HashMap<>();
		for (Move m : moves) {
			MoveType moveType = m.getMoveType();
			List<Move> list = result.computeIfAbsent(moveType, k -> new ArrayList<>());
			list.add(m);
		}
		return result;
	}

	public static List<Move> getMovesFromIds(List<Move> moves, List<Long> ids) {
		if (moves == null || moves.isEmpty() || ids == null || ids.isEmpty()) {
			return new ArrayList<>();
		}

		List<Move> result = new ArrayList<>();
		for (Move m : moves) {
			if (ids.contains(m.getId())) {
				result.add(m);
			}
		}
		return result;
	}
}