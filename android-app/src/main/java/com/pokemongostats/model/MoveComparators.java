/**
 * 
 */
package com.pokemongostats.model;

import java.util.Comparator;

import com.pokemongostats.model.bean.Move;

/**
 * @author Zapagon
 *
 */
public final class MoveComparators {
	private MoveComparators() {}

	public static Comparator<Move> COMPARATOR_BY_NAME = new Comparator<Move>() {

		@Override
		public int compare(Move m1, Move m2) {
			Integer nullParams = CheckNullComparator.checkNull(m1, m2);
			if (nullParams != null) { return nullParams; }

			String name1 = m1.getName();
			String name2 = m2.getName();

			nullParams = CheckNullComparator.checkNull(name1, name2);
			if (nullParams != null) { return nullParams; }

			return name1.compareTo(name2);
		}

	};

	public static Comparator<Move> COMPARATOR_BY_ID = new Comparator<Move>() {

		@Override
		public int compare(Move m1, Move m2) {
			Integer nullParams = CheckNullComparator.checkNull(m1, m2);
			if (nullParams != null) { return nullParams; }

			long id1 = m1.getId();
			long id2 = m2.getId();

			return (int) (id1 - id2);
		}

	};

	public static Comparator<Move> COMPARATOR_BY_DPS = new Comparator<Move>() {

		@Override
		public int compare(Move m1, Move m2) {
			Integer nullParams = CheckNullComparator.checkNull(m1, m2);
			if (nullParams != null) { return nullParams; }

			return 0; // TODO
		}

	};

	public static Comparator<Move> COMPARATOR_BY_POWER = new Comparator<Move>() {

		@Override
		public int compare(Move m1, Move m2) {
			Integer nullParams = CheckNullComparator.checkNull(m1, m2);
			if (nullParams != null) { return nullParams; }
			return 0; // TODO
		}

	};

	public static Comparator<Move> COMPARATOR_BY_SPEED = new Comparator<Move>() {

		@Override
		public int compare(Move m1, Move m2) {
			Integer nullParams = CheckNullComparator.checkNull(m1, m2);
			if (nullParams != null) { return nullParams; }
			return 0; // TODO
		}

	};
}
