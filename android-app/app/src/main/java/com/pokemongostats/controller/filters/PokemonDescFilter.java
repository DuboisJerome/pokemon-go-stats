package com.pokemongostats.controller.filters;

import android.widget.Filter;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.PkmnDescFilterInfo;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Zapagon on 05/03/2017.
 */
public abstract class PokemonDescFilter extends Filter {

	private final PkmnDescFilterInfo filterInfo = new PkmnDescFilterInfo();

	public void updateFrom(CharSequence cs) {
        this.filterInfo.clear();
		if (cs != null && !cs.toString().isEmpty()) {
            this.filterInfo.fromFilter(cs);
		}
	}

	protected boolean isNameOk(String nameFromItem) {
		String nameFromFilter = this.filterInfo.getName();
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

	protected boolean isTypesOk(Type type1, Type type2) {
		Type type1FromFilter = this.filterInfo.getType1();
		Type type2FromFilter = this.filterInfo.getType2();
		if (type1FromFilter == null && type2FromFilter == null) {
			return true;
		}

		boolean filter1OK = true;
		if (type1FromFilter != null) {
			filter1OK = (type1FromFilter.equals(type1) || type1FromFilter.equals(type2));
		}
		boolean filter2OK = true;
		if (type2FromFilter != null) {
			filter2OK = (type2FromFilter.equals(type1) || type2FromFilter.equals(type2));
		}
		return filter1OK && filter2OK;
	}
}