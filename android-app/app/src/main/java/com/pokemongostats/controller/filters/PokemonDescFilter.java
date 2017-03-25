package com.pokemongostats.controller.filters;

import android.widget.Filter;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.PokemonDescFilterInfo;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Zapagon on 05/03/2017.
 */
public abstract class PokemonDescFilter extends Filter {

    private final PokemonDescFilterInfo filterInfo = new PokemonDescFilterInfo();

    public void updateFrom(final CharSequence cs) {
        filterInfo.reset();
        if(cs != null || !cs.toString().isEmpty()){
            filterInfo.updateFromStringFilter(cs);
        }
    }

    protected boolean isNameOk(final String nameFromItem){
        String nameFromFilter = filterInfo.getName();
        if(nameFromFilter == null || nameFromFilter.isEmpty()){ return true; }
        if(nameFromItem == null){ return false;}
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

    protected boolean isTypesOk(final Type type1, final Type type2){
        Type type1FromFilter = filterInfo.getType1();
        Type type2FromFilter = filterInfo.getType2();
        if(type1FromFilter == null && type2FromFilter == null){
            return true;
        }

        boolean filter1OK = true;
        if(type1FromFilter != null) {
            filter1OK =  (type1FromFilter.equals(type1) || type1FromFilter.equals(type2));
        }
        boolean filter2OK = true;
        if(type2FromFilter != null){
            filter2OK = (type2FromFilter.equals(type1) || type2FromFilter.equals(type2));
        }
        return filter1OK && filter2OK;
    }
}
