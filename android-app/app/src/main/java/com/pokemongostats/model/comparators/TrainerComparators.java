/**
 *
 */
package com.pokemongostats.model.comparators;

import com.pokemongostats.model.bean.Trainer;

import java.util.Comparator;

/**
 * @author Zapagon
 */
public final class TrainerComparators {
    private static Comparator<Trainer> COMPARATOR_BY_NAME = new Comparator<Trainer>() {

        @Override
        public int compare(Trainer t1, Trainer t2) {
            Integer nullParams = CheckNullComparator.checkNull(t1, t2);
            if (nullParams != null) {
                return nullParams;
            }

            String name1 = t1.getName();
            String name2 = t2.getName();

            nullParams = CheckNullComparator.checkNull(name1, name2);
            if (nullParams != null) {
                return nullParams;
            }

            return name1.compareTo(name2);
        }

    };

    private static Comparator<Trainer> COMPARATOR_BY_LVL = new Comparator<Trainer>() {

        @Override
        public int compare(Trainer t1, Trainer t2) {
            Integer nullParams = CheckNullComparator.checkNull(t1, t2);
            if (nullParams != null) {
                return nullParams;
            }
            return -Long.compare(t1.getLevel(), t2.getLevel());
        }

    };

    private TrainerComparators() {
    }

    public static Comparator<Trainer> getComparatorByName() {
        return COMPARATOR_BY_NAME;
    }

    public static Comparator<Trainer> getComparatorByLvl() {
        return COMPARATOR_BY_LVL;
    }
}
