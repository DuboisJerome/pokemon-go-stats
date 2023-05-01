package com.pokemongostats.controller.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class CollectionUtils {
	public static <K, V> Map<K,List<V>> groupBy(Collection<V> c, Function<V,K> keyExtractor) {
		if (c == null || c.isEmpty()) {
			return new HashMap<>();
		}
		Map<K,List<V>> result = new HashMap<>();
		for (V v : c) {
			K k = keyExtractor.apply(v);
			result.computeIfAbsent(k, k2 -> new ArrayList<>()).add(v);
		}
		return result;
	}

	public static <T> T find(List<T> l, Predicate<T> p) {
		return l.stream().filter(p).findAny().orElse(null);
	}
}