package com.pokemongostats.controller.filters;

import android.widget.Filter;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.MoveFilterInfo;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Created by Zapagon on 05/03/2017.
 */
public abstract class MoveFilter extends Filter {

    private final MoveFilterInfo filterInfo = new MoveFilterInfo();

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

    protected boolean isTypeOk(final Type type){
        Type typeFromFilter = filterInfo.getType();
        return (typeFromFilter == null) ? true : typeFromFilter.equals(type);
    }
}
