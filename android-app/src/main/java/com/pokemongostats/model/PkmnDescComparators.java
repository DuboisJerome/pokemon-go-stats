/**
 * 
 */
package com.pokemongostats.model;

import java.util.Comparator;

import com.pokemongostats.model.bean.PokemonDescription;

/**
 * @author Zapagon
 *
 */
public final class PkmnDescComparators {
	private PkmnDescComparators() {}

	public static Comparator<PokemonDescription> COMPARATOR_BY_NAME = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }

			String name1 = p1.getName();
			String name2 = p2.getName();

			nullParams = CheckNullComparator.checkNull(name1, name2);
			if (nullParams != null) { return nullParams; }

			return name1.compareTo(name2);
		}

	};

	public static Comparator<PokemonDescription> COMPARATOR_BY_ID = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }

			long id1 = p1.getPokedexNum();
			long id2 = p2.getPokedexNum();

			return (int) (id1 - id2);
		}

	};

	public static Comparator<PokemonDescription> COMPARATOR_BY_BASE_ATTACK = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }
			return -Double.compare(p1.getBaseAttack(), p2.getBaseAttack());
		}

	};

	public static Comparator<PokemonDescription> COMPARATOR_BY_BASE_DEFENSE = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }
			return -Double.compare(p1.getBaseDefense(), p2.getBaseDefense());
		}

	};

	public static Comparator<PokemonDescription> COMPARATOR_BY_BASE_STAMINA = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }
			return -Double.compare(p1.getBaseStamina(), p2.getBaseStamina());
		}

	};

	public static Comparator<PokemonDescription> COMPARATOR_BY_MAX_CP = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }
			return -Double.compare(p1.getMaxCP(), p2.getMaxCP());
		}

	};
}
