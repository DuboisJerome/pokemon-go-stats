/**
 * 
 */
package com.pokemongostats.controller.utils;

import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.Move.MoveType;

/**
 * @author Zapagon
 *
 */
public class MoveUtils {

	private MoveUtils() {}

	public static double calculerDPS(final Move m) {
		double duration = m.getDuration();
		if (MoveType.CHARGE.equals(m.getMoveType())) {
			duration += 300;
		}
		return m.getPower() / (duration / 1000.0);
	}
}
