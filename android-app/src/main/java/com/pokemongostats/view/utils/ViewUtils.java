package com.pokemongostats.view.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.pokemongostats.R;
import com.pokemongostats.model.Pokemon;
import com.pokemongostats.model.Team;
import com.pokemongostats.model.Type;

/**
 * 
 * @author Zapagon
 *
 */
public class ViewUtils {

	private ViewUtils() {
	}

	/**
	 * @param t
	 *            team to test
	 * @return id color from R for given team
	 */
	public static Integer getTeamIdColor(Team t) {
		if (t == null) {
			return null;
		}

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

	public static Map<Long, Integer> mostUsedPokemons() {
		Map<Long, Integer> mostUsedPokemons = new HashMap<Long, Integer>();

		List<Pokemon> list = null;
		for (Pokemon p : list) {
			if (p == null) {
				continue;
			}
			Integer nbFound = mostUsedPokemons.get(p.getPokedexNum());
			mostUsedPokemons.put(p.getPokedexNum(), nbFound == null ? 1 : nbFound + 1);
		}

		return null;
	}

	public static class MultiType {

		private final Set<Type> types;

		public MultiType(final Set<Type> types) {
			this.types = types;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof MultiType) || this.types == null) {
				return false;
			}
			return types.equals(((MultiType) o).types);
		}

		@Override
		public int hashCode() {
			if (types == null) {
				return 0;
			}
			return types.hashCode();
		}
	}
}
