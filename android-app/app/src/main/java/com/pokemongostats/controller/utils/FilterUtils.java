package com.pokemongostats.controller.utils;

import com.pokemongostats.model.bean.Type;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilterUtils {

	private static final Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
	// Performance enorme de ne pas recréer le matcher à chaque fois (600ms -> 120ms)
	private static final Matcher matcher = pattern.matcher("");
	private static final Pattern pattern2 = Pattern.compile("[œ]+");
	// Performance enorme de ne pas recréer le matcher à chaque fois (600ms -> 120ms)
	private static final Matcher matcher2 = pattern2.matcher("");

	public static boolean isNameMatch(String nameFromFilterNormalizedLower, String nameFromItem) {
		if (nameFromFilterNormalizedLower == null || nameFromFilterNormalizedLower.isEmpty()) {
			return true;
		}
		if (nameFromItem == null) {
			return false;
		}
		// Shortcut
		boolean isMatch = nameFromItem.toLowerCase().startsWith(nameFromFilterNormalizedLower);
		if (!isMatch) {
			String normalizedNameItem = nameForFilter(nameFromItem);
			isMatch = normalizedNameItem.regionMatches(true, 0, nameFromFilterNormalizedLower, 0, nameFromFilterNormalizedLower.length());
		}
		return isMatch;
	}

	public static String nameForFilter(String name) {
		if (name == null) {
			return null;
		}
		String nfdNormalizedString = Normalizer.normalize(name, Normalizer.Form.NFD);
		String normalizedName = matcher.reset(nfdNormalizedString).replaceAll("");
		normalizedName = matcher2.reset(normalizedName).replaceAll("oe");
		return normalizedName.toLowerCase();
	}

	public static boolean isTypeMatch(Type typeFromFilter, Type typeFromItem) {
		return null == typeFromFilter || typeFromFilter == typeFromItem;
	}

	public static boolean isTypeMatch(Type typeFromFilter, Type type1FromItem, Type type2FromItem) {
		return null == typeFromFilter || typeFromFilter == type1FromItem || typeFromFilter == type2FromItem;
	}

	public static boolean isTypeMatch(Type type1FromFilter, Type type2FromFilter, Type type1FromItem, Type type2FromItem) {
		return isTypeMatch(type1FromFilter, type1FromItem, type2FromItem) && isTypeMatch(type2FromFilter, type1FromItem, type2FromItem);
	}
}