package com.pokemongostats.controller.utils;

import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;
import com.pokemongostats.model.bean.Type;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zapagon
 */
public final class FightUtils {

	public static final double STAB_MULTIPLIER = 1.2;

	private static final Map<Double,Double> MAP_CPM;

	static {
		MAP_CPM = new HashMap<>();
		MAP_CPM.put(1.0, 0.094);
		MAP_CPM.put(1.5, 0.135137432);
		MAP_CPM.put(2.0, 0.16639787);
		MAP_CPM.put(2.5, 0.192650919);
		MAP_CPM.put(3.0, 0.21573247);
		MAP_CPM.put(3.5, 0.236572661);
		MAP_CPM.put(4.0, 0.25572005);
		MAP_CPM.put(4.5, 0.273530381);
		MAP_CPM.put(5.0, 0.29024988);
		MAP_CPM.put(5.5, 0.306057377);
		MAP_CPM.put(6.0, 0.3210876);
		MAP_CPM.put(6.5, 0.335445036);
		MAP_CPM.put(7.0, 0.34921268);
		MAP_CPM.put(7.5, 0.362457751);
		MAP_CPM.put(8.0, 0.37523559);
		MAP_CPM.put(8.5, 0.387592406);
		MAP_CPM.put(9.0, 0.39956728);
		MAP_CPM.put(9.5, 0.411193551);
		MAP_CPM.put(10.0, 0.42250001);
		MAP_CPM.put(10.5, 0.432926419);
		MAP_CPM.put(11.0, 0.44310755);
		MAP_CPM.put(11.5, 0.4530599578);
		MAP_CPM.put(12.0, 0.46279839);
		MAP_CPM.put(12.5, 0.472336083);
		MAP_CPM.put(13.0, 0.48168495);
		MAP_CPM.put(13.5, 0.4908558);
		MAP_CPM.put(14.0, 0.49985844);
		MAP_CPM.put(14.5, 0.508701765);
		MAP_CPM.put(15.0, 0.51739395);
		MAP_CPM.put(15.5, 0.525942511);
		MAP_CPM.put(16.0, 0.53435433);
		MAP_CPM.put(16.5, 0.542635767);
		MAP_CPM.put(17.0, 0.55079269);
		MAP_CPM.put(17.5, 0.558830576);
		MAP_CPM.put(18.0, 0.56675452);
		MAP_CPM.put(18.5, 0.574569153);
		MAP_CPM.put(19.0, 0.58227891);
		MAP_CPM.put(19.5, 0.589887917);
		MAP_CPM.put(20.0, 0.59740001);
		MAP_CPM.put(20.5, 0.604818814);
		MAP_CPM.put(21.0, 0.61215729);
		MAP_CPM.put(21.5, 0.619399365);
		MAP_CPM.put(22.0, 0.62656713);
		MAP_CPM.put(22.5, 0.633644533);
		MAP_CPM.put(23.0, 0.64065295);
		MAP_CPM.put(23.5, 0.647576426);
		MAP_CPM.put(24.0, 0.65443563);
		MAP_CPM.put(24.5, 0.661214806);
		MAP_CPM.put(25.0, 0.667934);
		MAP_CPM.put(25.5, 0.674577537);
		MAP_CPM.put(26.0, 0.68116492);
		MAP_CPM.put(26.5, 0.687680648);
		MAP_CPM.put(27.0, 0.69414365);
		MAP_CPM.put(27.5, 0.700538673);
		MAP_CPM.put(28.0, 0.70688421);
		MAP_CPM.put(28.5, 0.713164996);
		MAP_CPM.put(29.0, 0.71939909);
		MAP_CPM.put(29.5, 0.725571552);
		MAP_CPM.put(30.0, 0.7317);
		MAP_CPM.put(30.5, 0.734741009);
		MAP_CPM.put(31.0, 0.73776948);
		MAP_CPM.put(31.5, 0.740785574);
		MAP_CPM.put(32.0, 0.74378943);
		MAP_CPM.put(32.5, 0.746781211);
		MAP_CPM.put(33.0, 0.74976104);
		MAP_CPM.put(33.5, 0.752729087);
		MAP_CPM.put(34.0, 0.75568551);
		MAP_CPM.put(34.5, 0.758630378);
		MAP_CPM.put(35.0, 0.76156384);
		MAP_CPM.put(35.5, 0.764486065);
		MAP_CPM.put(36.0, 0.76739717);
		MAP_CPM.put(36.5, 0.770297266);
		MAP_CPM.put(37.0, 0.7731865);
		MAP_CPM.put(37.5, 0.776064962);
		MAP_CPM.put(38.0, 0.77893275);
		MAP_CPM.put(38.5, 0.781790055);
		MAP_CPM.put(39.0, 0.78463697);
		MAP_CPM.put(39.5, 0.787473578);
		MAP_CPM.put(40.0, 0.79030001);
		// FIXME CP 40-50
	}

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
			ppt = (m.getDurationPvp() > 0) ? (double) m.getPowerPvp() / (double) m.getDurationPvp() : m.getPowerPvp();
		}
		if (owner != null) {
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