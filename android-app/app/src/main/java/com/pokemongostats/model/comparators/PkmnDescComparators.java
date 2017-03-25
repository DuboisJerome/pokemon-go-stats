/**
 * 
 */
package com.pokemongostats.model.comparators;

import com.pokemongostats.model.bean.PokemonDescription;

import java.util.Comparator;

/**
 * @author Zapagon
 *
 */
public final class PkmnDescComparators {
	private PkmnDescComparators() {}

	private static Comparator<PokemonDescription> COMPARATOR_BY_NAME = new Comparator<PokemonDescription>() {

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

	private static Comparator<PokemonDescription> COMPARATOR_BY_ID = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }
			return Long.compare(p1.getPokedexNum(), p2.getPokedexNum());
		}

	};

	private static Comparator<PokemonDescription> COMPARATOR_BY_BASE_ATTACK = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }
			return -Double.compare(p1.getBaseAttack(), p2.getBaseAttack());
		}

	};

	private static Comparator<PokemonDescription> COMPARATOR_BY_BASE_DEFENSE = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }
			return -Double.compare(p1.getBaseDefense(), p2.getBaseDefense());
		}

	};

	private static Comparator<PokemonDescription> COMPARATOR_BY_BASE_STAMINA = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }
			return -Double.compare(p1.getBaseStamina(), p2.getBaseStamina());
		}

	};

	private static Comparator<PokemonDescription> COMPARATOR_BY_MAX_CP = new Comparator<PokemonDescription>() {

		@Override
		public int compare(PokemonDescription p1, PokemonDescription p2) {
			Integer nullParams = CheckNullComparator.checkNull(p1, p2);
			if (nullParams != null) { return nullParams; }
			return -Double.compare(p1.getMaxCP(), p2.getMaxCP());
		}

	};

	public static Comparator<PokemonDescription> getComparatorByName() {
		return COMPARATOR_BY_NAME;
	}

	public static Comparator<PokemonDescription> getComparatorById() {
		return COMPARATOR_BY_ID;
	}

	public static Comparator<PokemonDescription> getComparatorByBaseAttack() {
		return COMPARATOR_BY_BASE_ATTACK;
	}

	public static Comparator<PokemonDescription> getComparatorByBaseDefense() {
		return COMPARATOR_BY_BASE_DEFENSE;
	}

	public static Comparator<PokemonDescription> getComparatorByBaseStamina() {
		return COMPARATOR_BY_BASE_STAMINA;
	}

	public static Comparator<PokemonDescription> getComparatorByMaxCp() {
		return COMPARATOR_BY_MAX_CP;
	}
}
