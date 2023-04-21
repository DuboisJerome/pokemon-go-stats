package com.pokemongostats.view.utils;

import com.pokemongostats.controller.dao.PokedexDAO;
import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;
import com.pokemongostats.model.bean.PkmnMove;

public class MoveUtils {

	public static String getName(Move move, PkmnDesc owner) {
		PkmnMove pm = PokedexDAO.getInstance().getPkmnMoveFor(owner, move);
		if (pm == null || !pm.isElite()) {
			return move.getName();
		} else {
			return move.getName() + "*";
		}
	}

	public static String getPowerPerSecond(Move move) {
		return getPowerPerSecond(move, null);
	}

	public static String getPowerPerSecond(Move move, PkmnDesc owner) {
		double pps = Math.floor(FightUtils.computePowerPerSecond(move, owner) * 100) / 100;
		return String.valueOf(pps);
	}

	public static String getDptxEpt(Move move) {
		return getDptxEpt(move, null);
	}

	public static String getDptxEpt(Move move, PkmnDesc owner) {
		double dptXEpt = Math.floor(FightUtils.computePowerEnergyPerTurn(move, owner) * 100) / 100;
		return String.valueOf(dptXEpt);
	}
}