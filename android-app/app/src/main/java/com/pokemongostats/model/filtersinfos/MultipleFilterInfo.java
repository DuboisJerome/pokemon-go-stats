package com.pokemongostats.model.filtersinfos;

/**
 * Created by Jérôme on 05/03/2017.
 */

public interface MultipleFilterInfo {
    CharSequence toStringFilter();

    void updateFromStringFilter(final CharSequence stringFilter);

    boolean isEmpty();

    void reset();
}
