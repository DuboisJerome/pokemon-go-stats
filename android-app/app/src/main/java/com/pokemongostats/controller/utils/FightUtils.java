package com.pokemongostats.controller.utils;

import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.Type;

/**
 * @author Zapagon
 */
public final class FightUtils {

	public static final double STAB_MULTIPLIER = 1.2;

	private FightUtils() {
	}

	/**
	 * Move power per second for owner
	 *
	 * @param m     Move
	 * @param owner Owner of the move
	 * @return Power per second
	 */
	public static double computePowerPerSecond(Move m, PkmnDesc owner) {

		if (m == null) {
			return 0.0;
		}
		double duration = m.getDuration();
		double dps = (duration > 0) ? m.getPower() / (duration / 1000.0) : 0d;
		if (owner != null) {
			dps = isSTAB(m, owner) ? dps * STAB_MULTIPLIER : dps;
			dps = Math.floor(dps * 100) / 100;
		}
		return dps;
	}

	public static double computeEnergyPerSecond(Move m) {

		if (m == null) {
			return 0.0;
		}
		double duration = m.getDuration();
		return (duration > 0) ? m.getEnergyDelta() / (duration / 1000.0) : 0d;
	}

	public static double computePowerPerTurn(Move m) {
		return computePowerPerTurn(m, null);
	}

	public static double computePowerPerTurn(Move m, PkmnDesc owner) {
		if (m == null) {
			return 0.0;
		}
		double ppt;
		if (Move.MoveType.CHARGE.equals(m.getMoveType())) {
			ppt = m.getPowerPvp();
		} else {
			ppt = (m.getDurationPvp() >= 0) ? (double) m.getPowerPvp() / (double) (m.getDurationPvp()+1) : -1D;
		}
		if (owner != null && ppt > -1D) {
			ppt = isSTAB(m, owner) ? ppt * STAB_MULTIPLIER : ppt;
			ppt = Math.floor(ppt * 100) / 100;
		}
		return ppt;
	}

	public static double computeEnergyPerTurn(Move m) {
		if (m == null) {
			return 0.0;
		}
		if (Move.MoveType.CHARGE.equals(m.getMoveType())) {
			return Math.abs(m.getEnergyPvp());
		}
		return (m.getDurationPvp() > 0)
				? (double) Math.abs(m.getEnergyPvp()) / (double) m.getDurationPvp() : Math.abs(m.getEnergyPvp());
	}

	public static double computePowerEnergyPerTurn(Move m, PkmnDesc owner) {
		if (m == null) {
			return 0.0;
		}
		double ppt = computePowerPerTurn(m, owner);
		if (Move.MoveType.CHARGE.equals(m.getMoveType())) {
			return ppt / computeEnergyPerTurn(m);
		}
		return ppt * computeEnergyPerTurn(m);
	}

	public static double computePowerEnergyPerTurn(Move m) {
		return computePowerEnergyPerTurn(m, null);
	}

	public static boolean isSTAB(Move m, PkmnDesc owner) {
		if (m == null || owner == null) {
			return false;
		}
		Type type = m.getType();
		if (type == null) {
			return false;
		}
		return type.equals(owner.getType1()) || type.equals(owner.getType2());
	}

	public static double computePowerPerEnergyPvE(Move m, PkmnDesc owner){
		double d = m.getPower();
		if (owner != null) {
			d = isSTAB(m, owner) ? d * STAB_MULTIPLIER : d;
			d = Math.floor(d * 100) / 100;
		}
		return d / m.getEnergyDelta();
	}
	public static double computePowerPerEnergyPvP(Move m, PkmnDesc owner){
		double d = m.getPowerPvp();
		if (owner != null) {
			d = isSTAB(m, owner) ? d * STAB_MULTIPLIER : d;
			d = Math.floor(d * 100) / 100;
		}
		return d / m.getEnergyPvp();
	}

}