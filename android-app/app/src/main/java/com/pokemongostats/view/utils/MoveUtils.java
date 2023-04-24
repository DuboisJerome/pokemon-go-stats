package com.pokemongostats.view.utils;

import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;

public class MoveUtils {

	public static String getPowerPerSecond(Move move) {
		return getPowerPerSecond(move, null);
	}

	public static String getPowerPerSecond(Move move, PkmnDesc owner) {
		double pps = Math.floor(FightUtils.computePowerPerSecond(move, owner) * 100) / 100;
		return String.valueOf(pps);
	}

	public static String getEnergyPerSecond(Move move){
		return String.valueOf(move.getEnergyDelta());
	}

	public static String getDptxEpt(Move move) {
		return getDptxEpt(move, null);
	}

	public static String getDptxEpt(Move move, PkmnDesc owner) {
		double dptXEpt = Math.floor(FightUtils.computePowerEnergyPerTurn(move, owner) * 100) / 100;
		return String.valueOf(dptXEpt);
	}
}