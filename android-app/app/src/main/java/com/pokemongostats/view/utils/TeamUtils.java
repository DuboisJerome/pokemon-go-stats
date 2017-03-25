package com.pokemongostats.view.utils;

import com.pokemongostats.R;
import com.pokemongostats.model.bean.Team;

/**
 * 
 * @author Zapagon
 *
 */
public final class TeamUtils {

	private TeamUtils() {}

	/**
	 * @param t
	 *            team to test
	 * @return id color from R for given team
	 */
	public static Integer getTeamIdColor(Team t) {
		if (t == null) { return null; }

		switch (t) {
		case INSTINCT:
			return R.color.instinct_color;
		case MYSTIC:
			return R.color.mystic_color;
		case VALOR:
			return R.color.valor_color;
		default:
			return null;
		}
	}
}
