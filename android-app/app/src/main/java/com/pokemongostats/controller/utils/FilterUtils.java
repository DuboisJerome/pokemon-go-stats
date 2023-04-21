package com.pokemongostats.controller.utils;

import com.pokemongostats.model.bean.Type;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class FilterUtils {
	public static boolean isNameMatch(String nameFromFilter, String nameFromItem) {
		if (nameFromFilter == null || nameFromFilter.isEmpty()) {
			return true;
		}
		if (nameFromItem == null) {
			return false;
		}
		String nfdNormalizedString = Normalizer
				.normalize(nameFromItem, Normalizer.Form.NFD);
		Pattern pattern = Pattern
				.compile("\\p{InCombiningDiacriticalMarks}+");
		String normalizedName = pattern.matcher(nfdNormalizedString)
				.replaceAll("");
		normalizedName = normalizedName.replaceAll("œ", "oe");
		return (normalizedName.toLowerCase()
				.startsWith(nameFromFilter.toLowerCase()));
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