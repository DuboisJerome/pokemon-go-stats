package com.pokemongostats.model.comparators;

import com.pokemongostats.model.bean.PkmnDesc;

import java.util.Comparator;

/**
 * @author Zapagon
 */
public final class PkmnDescComparators {
	private static final Comparator<PkmnDesc> COMPARATOR_BY_NAME = (p1, p2) -> {
		Integer nullParams = CheckNullComparator.checkNull(p1, p2);
		if (nullParams != null) {
			return nullParams;
		}

		String name1 = p1.getName();
		String name2 = p2.getName();

		nullParams = CheckNullComparator.checkNull(name1, name2);
		if (nullParams != null) {
			return nullParams;
		}

		return name1.compareTo(name2);
	};
	private static final Comparator<PkmnDesc> COMPARATOR_BY_ID = PkmnDesc.COMPARATOR_BY_ID;
	private static final Comparator<PkmnDesc> COMPARATOR_BY_BASE_ATTACK = (p1, p2) -> {
		Integer nullParams = CheckNullComparator.checkNull(p1, p2);
		if (nullParams != null) {
			return nullParams;
		}
		return -Double.compare(p1.getBaseAttack(), p2.getBaseAttack());
	};
	private static final Comparator<PkmnDesc> COMPARATOR_BY_BASE_DEFENSE = (p1, p2) -> {
		Integer nullParams = CheckNullComparator.checkNull(p1, p2);
		if (nullParams != null) {
			return nullParams;
		}
		return -Double.compare(p1.getBaseDefense(), p2.getBaseDefense());
	};
	private static final Comparator<PkmnDesc> COMPARATOR_BY_BASE_STAMINA = (p1, p2) -> {
		Integer nullParams = CheckNullComparator.checkNull(p1, p2);
		if (nullParams != null) {
			return nullParams;
		}
		return -Double.compare(p1.getBaseStamina(), p2.getBaseStamina());
	};
	private static final Comparator<PkmnDesc> COMPARATOR_BY_MAX_CP = (p1, p2) -> {
		Integer nullParams = CheckNullComparator.checkNull(p1, p2);
		if (nullParams != null) {
			return nullParams;
		}
		return -Double.compare(p1.getMaxCP(), p2.getMaxCP());
	};

	private PkmnDescComparators() {
	}

	public static Comparator<PkmnDesc> getComparatorByName() {
		return COMPARATOR_BY_NAME;
	}

	public static Comparator<PkmnDesc> getComparatorById() {
		return COMPARATOR_BY_ID;
	}

	public static Comparator<PkmnDesc> getComparatorByBaseAttack() {
		return COMPARATOR_BY_BASE_ATTACK;
	}

	public static Comparator<PkmnDesc> getComparatorByBaseDefense() {
		return COMPARATOR_BY_BASE_DEFENSE;
	}

	public static Comparator<PkmnDesc> getComparatorByBaseStamina() {
		return COMPARATOR_BY_BASE_STAMINA;
	}

	public static Comparator<PkmnDesc> getComparatorByMaxCp() {
		return COMPARATOR_BY_MAX_CP;
	}
}