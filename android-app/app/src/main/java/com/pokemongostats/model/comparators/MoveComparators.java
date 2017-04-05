/**
 *
 */
package com.pokemongostats.model.comparators;

import com.pokemongostats.controller.utils.FightUtils;
import com.pokemongostats.model.bean.Move;
import com.pokemongostats.model.bean.PkmnDesc;

import java.util.Comparator;

/**
 * @author Zapagon
 */
public final class MoveComparators {
    private static Comparator<Move> COMPARATOR_BY_NAME = new Comparator<Move>() {

        @Override
        public int compare(Move m1, Move m2) {
            Integer nullParams = CheckNullComparator.checkNull(m1, m2);
            if (nullParams != null) {
                return nullParams;
            }

            String name1 = m1.getName();
            String name2 = m2.getName();

            nullParams = CheckNullComparator.checkNull(name1, name2);
            if (nullParams != null) {
                return nullParams;
            }

            return name1.compareTo(name2);
        }

    };
    private static Comparator<Move> COMPARATOR_BY_ID = new Comparator<Move>() {

        @Override
        public int compare(Move m1, Move m2) {
            Integer nullParams = CheckNullComparator.checkNull(m1, m2);
            if (nullParams != null) {
                return nullParams;
            }
            return Long.compare(m1.getId(), m2.getId());
        }

    };
    private static ComparatorMoveOwner COMPARATOR_BY_PPS = new ComparatorMoveOwner();
    private static Comparator<Move> COMPARATOR_BY_POWER = new Comparator<Move>() {

        @Override
        public int compare(Move m1, Move m2) {
            Integer nullParams = CheckNullComparator.checkNull(m1, m2);
            if (nullParams != null) {
                return nullParams;
            }
            return -Double.compare(m1.getPower(), m2.getPower());
        }

    };
    private static Comparator<Move> COMPARATOR_BY_SPEED = new Comparator<Move>() {

        @Override
        public int compare(Move m1, Move m2) {
            Integer nullParams = CheckNullComparator.checkNull(m1, m2);
            if (nullParams != null) {
                return nullParams;
            }
            return Double.compare(m1.getDuration(), m2.getDuration());
        }

    };

    ;

    private MoveComparators() {
    }

    public static Comparator<Move> getComparatorByName() {
        return COMPARATOR_BY_NAME;
    }

    public static Comparator<Move> getComparatorById() {
        return COMPARATOR_BY_ID;
    }

    public static Comparator<Move> getComparatorByPps() {
        COMPARATOR_BY_PPS.setOwner(null);
        return COMPARATOR_BY_PPS;
    }

    public static Comparator<Move> getComparatorByPps(final PkmnDesc owner) {
        COMPARATOR_BY_PPS.setOwner(owner);
        return COMPARATOR_BY_PPS;
    }

    public static Comparator<Move> getComparatorByPower() {
        return COMPARATOR_BY_POWER;
    }

    public static Comparator<Move> getComparatorBySpeed() {
        return COMPARATOR_BY_SPEED;
    }

    private static class ComparatorMoveOwner implements Comparator<Move> {

        private PkmnDesc owner;

        public void setOwner(final PkmnDesc owner) {
            this.owner = owner;
        }

        @Override
        public int compare(Move m1, Move m2) {
            Integer nullParams = CheckNullComparator.checkNull(m1, m2);
            if (nullParams != null) {
                return nullParams;
            }
            return -Double.compare(FightUtils.computePPS(m1, owner), FightUtils.computePPS(m2, owner));
        }

    }
}
