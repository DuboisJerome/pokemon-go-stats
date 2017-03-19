package com.pokemongostats.controller.filters;

import android.util.Log;
import android.widget.Filter;

import com.pokemongostats.model.bean.Type;
import com.pokemongostats.model.filtersinfos.PokemonDescFilterInfo;

import org.json.JSONException;
import org.json.JSONObject;

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

    protected boolean isTypeOk(final Type type){
        Type type1FromFilter = filterInfo.getType1();
        Type type2FromFilter = filterInfo.getType1();
        if(type1FromFilter == null && type2FromFilter == null){ return true; }
        if(type == null){ return false;}
        return (type.equals(type1FromFilter) || type.equals(type2FromFilter));
    }
}
