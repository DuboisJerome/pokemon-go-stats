/**
 * 
 */
package com.pokemongostats.model.comparators;

/**
 * @author Zapagon
 *
 */
public final class CheckNullComparator {
	private CheckNullComparator() {
	}

	public static Integer checkNull(Object p1, Object p2) {
		if (p1 == null && p2 == null) {
			return 0;
		}
		if (p1 == null) {
			return -1;
		}
		if (p2 == null) {
			return 1;
		}
		return null;
	}
}
