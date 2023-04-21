package com.pokemongostats.model.filtersinfos;

/**
 * Created by Jérôme on 05/03/2017.
 */

public interface MultipleFilterInfo {
	CharSequence toFilter();

	void fromFilter(CharSequence jsonFilter);

	void clear();
}