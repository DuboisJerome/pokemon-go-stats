package com.pokemongostats.model.comparators;

import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.bdd.Move;
import com.pokemongostats.model.bean.bdd.PkmnDesc;

import java.util.Comparator;

/**
 * @author Zapagon
 */
public final class MoveComparators {

	private static final ComparatorMoveOwner COMPARATOR_BY_PPS = new ComparatorMoveOwner();

	private MoveComparators() {
	}

	public static Comparator<Move> getComparatorByPps() {
		COMPARATOR_BY_PPS.setOwner(null);
		return COMPARATOR_BY_PPS;
	}

	public static Comparator<Move> getComparatorByPps(PkmnDesc owner) {
		COMPARATOR_BY_PPS.setOwner(owner);
		return COMPARATOR_BY_PPS;
	}

	private static class ComparatorMoveOwner implements Comparator<Move> {

		private PkmnDesc owner;

		public void setOwner(PkmnDesc owner) {
			this.owner = owner;
		}

		@Override
		public int compare(Move m1, Move m2) {
			Integer nullParams = CheckNullComparator.checkNull(m1, m2);
			if (nullParams != null) {
				return nullParams;
			}
			return Double.compare(FightUtils.computePowerPerSecond(m1, this.owner), FightUtils.computePowerPerSecond(m2, this.owner));
		}

	}

	private static class ComparatorMoveOwnerPvp implements Comparator<Move> {

		private PkmnDesc owner;

		public void setOwner(PkmnDesc owner) {
			this.owner = owner;
		}

		@Override
		public int compare(Move m1, Move m2) {
			Integer nullParams = CheckNullComparator.checkNull(m1, m2);
			if (nullParams != null) {
				return nullParams;
			}
			return Double.compare(FightUtils.computePowerEnergyPerTurn(m1, this.owner), FightUtils.computePowerEnergyPerTurn(m2, this.owner));
		}

	}
}