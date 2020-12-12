package com.pokemongostats.view.utils;

import com.pokemongostats.model.bean.Move;

import java.util.Comparator;
import java.util.function.Function;

public class ComparatorUtils {
    public static <T extends Comparable<T>> Comparator<Move> createComparatorNullCheck(Function<Move, T> f) {
        return Comparator.comparing(f, Comparator.nullsLast(Comparator.naturalOrder()));
    }
}
